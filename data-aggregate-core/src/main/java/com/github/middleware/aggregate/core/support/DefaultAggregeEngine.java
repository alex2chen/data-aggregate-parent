package com.github.middleware.aggregate.core.support;

import com.github.middleware.aggregate.config.MergeProperties;
import com.github.middleware.aggregate.context.AggregeContext;
import com.github.middleware.aggregate.context.AggregeListener;
import com.github.middleware.aggregate.core.AggregeEngine;
import com.github.middleware.aggregate.core.AggregeException;
import com.github.middleware.aggregate.flow.ItemBinder;
import com.github.middleware.aggregate.flow.ItemCommand;
import com.github.middleware.aggregate.flow.builder.Steps;
import com.github.middleware.aggregate.flow.context.Invocation;
import com.github.middleware.aggregate.source.MetaHolderFactory;
import com.github.middleware.aggregate.source.bean.MetaHolder;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.github.middleware.aggregate.context.container.StartedAggregeEvent;
import com.github.middleware.aggregate.context.container.StopAggregeEvent;
import com.github.middleware.aggregate.annonation.AggregeEnable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/15.
 */
public final class DefaultAggregeEngine implements AggregeEngine {
    private AtomicBoolean start = new AtomicBoolean(false);
    private static final String PROPERTY_FILE_NAME = "data-aggrege.yml";
    private List<String> notBatchFilterRules = Lists.newArrayList(ItemCommand.BATCHPROXYARGBUILD, ItemCommand.BATCHPROXYFETCHDATA);
    private MergeProperties properties;
    private ThreadPoolExecutor threadPoolExecutor = null;
    private MetaHolderFactory metaHolderFactory;
    private ItemBinder itemBinder;
    private EventBus eventBus;

    @Override
    public void start() {
        if (start.compareAndSet(false, true)) {
            initComponent();
            eventBus.post(new StartedAggregeEvent("start"));
        }
    }

    /**
     * 支持spring或非spring环境下配置
     *
     * @param config
     */
    @Override
    public void loadConfig(MergeProperties config) {
        if (config != null) {
            properties = config;
        }
        if (properties == null) {
            //读配置
            properties = readProperty(MergeProperties.class, PROPERTY_FILE_NAME);
        }
    }

    private void initComponent() {
        loadConfig(null);
        threadPoolExecutor = new ThreadPoolExecutor(properties.getCorePoolSize(), properties.getCorePoolSize(), properties.getKeepAliveTime(),
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(properties.getWorkQueueSize()), new NamedThreadFactory("aggrege"), new ThreadPoolExecutor.AbortPolicy());
        //实例化元数据读取组件
        ExtensionLoaders.getExtensionLoader(MetaHolderFactory.class).flatMap(x -> x.getExtension()).ifPresent(x -> {
            metaHolderFactory = x;
            metaHolderFactory.lasyInit(properties);
        });
        //实例化数据聚合处理组件
        itemBinder = Steps.getBuilder().start().config(properties).loadAndSort().buildInvokerChain();
        //事件风暴组件
        eventBus = new EventBus();
        ExtensionLoaders.getExtensionLoader(AggregeListener.class).ifPresent(x -> {
            x.getExtensions().forEach(v -> eventBus.register(v));
        });
    }

    @Override
    public boolean isRunning() {
        return start.get();
    }

    @Override
    public void stop() {
        if (start.compareAndSet(true, false)) {
            eventBus.post(new StopAggregeEvent("stop"));
            threadPoolExecutor.shutdown();
        }
    }

    @Override
    public Object dataBind(AggregeEnable enable, Object item) {
        Preconditions.checkArgument(start.get(), "AggregeEngine未启动,请确认用法是否正确!");
        if (item == null) {
            return null;
        }
        Invocation invocation = new Invocation(enable, eventBus, AggregeContext.getContext());
        if (item instanceof List) {
            List<Object> list = (List<Object>) item;
            return mergeItems(list, invocation);
        }
        return mergeItem(item.getClass(), item, invocation);
    }

    private Object mergeItem(Class<?> sourceCls, Object item, Invocation invocation) {
        List<MetaHolder> metas = metaHolderFactory.create(sourceCls);
        if (metas == null || metas.isEmpty()) {
            return item;
        }
        boolean parallel = invocation.getEnable().parallel();
        if (parallel) {
            Flowable.fromIterable(() -> metas.iterator()).parallel().runOn(Schedulers.from(threadPoolExecutor)).map(x -> {
                realMerge(x, invocation, item);
                return 0;
            }).sequential().blockingLast();
        } else {
            Flowable.fromIterable(() -> metas.iterator()).subscribe(x -> {
                realMerge(x, invocation, item);
            });
        }
        return item;
    }

    private void realMerge(MetaHolder meta, Invocation invocation, Object item) {
        /**
         * 为何直接使用invocation.setItemElementMeta(meta)?
         * 使用它必须对realMerge添加synchronized，为了提高并发而采用invocation.getMetaContext()
         */
        //invocation.setItemElementMeta(meta);
        invocation.getMetaContext().setItemElementMeta(meta);
        try {
            if (invocation.getMetaContext().getFilterRules() == null && !invocation.getMetaContext().isBatch()) {
                //过滤某些链
                invocation.getMetaContext().setFilterRules(notBatchFilterRules);
            }
            itemBinder.handle(null, item, invocation);
        } finally {
            invocation.removeMetaContext();
        }
    }

    private Object mergeItems(List<Object> items, Invocation invocation) {
        invocation.setOrgItems(items);
        items.forEach(x -> mergeItem(x.getClass(), x, invocation));
        return items;
    }

    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

    private <T> T readProperty(Class<T> clz, String fileName) {
        try {
            Yaml yaml = new Yaml();
            return yaml.loadAs(this.getClass().getClassLoader().getResourceAsStream(fileName), clz);
        } catch (Exception ex) {
            throw new AggregeException("数据聚合组件读取配置失败，", ex);
        }
    }

    public static class NamedThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public NamedThreadFactory(String prefixName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = prefixName + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}

package com.github.middleware.aggregate.flow.context;

import com.github.middleware.aggregate.context.AggregeContext;
import com.github.middleware.aggregate.source.bean.MetaHolder;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.github.middleware.aggregate.annonation.AggregeEnable;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/11.
 */
public class Invocation {
    private AggregeEnable enable;
    private EventBus eventBus;
    /**
     * 为何要定义aggregeContext？主要是ItemBinder有多线程不能使用AggregeContext.getContext()
     */
    private AggregeContext aggregeContext;
    /**
     * META_LOCAL主要服务于单个Item 某个Meta，区别于AggregeContext
     */
    private final ThreadLocal<MetaItemContext> META_LOCAL = ThreadLocal.withInitial(MetaItemContext::new);
    private MetaHolder itemElementMeta;
    /**
     * 原始数据，批量时才有效
     */
    protected List<Object> orgItems;

    public Invocation(AggregeEnable enable, EventBus eventBus, AggregeContext aggregeContext) {
        this.enable = enable;
        this.eventBus = eventBus;
        this.aggregeContext = aggregeContext;
    }

    public AggregeContext getAggregeContext() {
        return aggregeContext;
    }

    public void setAggregeContext(AggregeContext aggregeContext) {
        this.aggregeContext = aggregeContext;
    }

    public MetaItemContext getMetaContext() {
        return META_LOCAL.get();
    }

    public void removeMetaContext() {
        META_LOCAL.remove();
    }

    public AggregeEnable getEnable() {
        return enable;
    }

    public void setEnable(AggregeEnable enable) {
        this.enable = enable;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public MetaHolder getItemElementMeta() {
        return itemElementMeta;
    }

    public void setItemElementMeta(MetaHolder itemElementMeta) {
        this.itemElementMeta = itemElementMeta;
    }

    public List<Object> getOrgItems() {
        return orgItems;
    }

    public void setOrgItems(List<Object> orgItems) {
        this.orgItems = orgItems;
    }

    /**
     * 作用于Field
     */
    public class MetaItemContext {
        private MetaHolder itemElementMeta;
        private List<Object> proxyParams;
        private Object proxyResult;
        private List<String> filterRules;
        private List<Object> batchProxyParams;
        private Object batchProxyResult;
        /**
         * 批量时缓存field的值
         */
        private HashMap<Integer, Object> filedValueCache;

        public MetaHolder getItemElementMeta() {
            return itemElementMeta;
        }

        public void setItemElementMeta(MetaHolder itemElementMeta) {
            this.itemElementMeta = itemElementMeta;
        }

        public List<Object> getProxyParams() {
            return proxyParams;
        }

        public void setProxyParams(List<Object> proxyParams) {
            this.proxyParams = proxyParams;
        }

        public Object getProxyResult() {
            return proxyResult;
        }

        public void setProxyResult(Object proxyResult) {
            this.proxyResult = proxyResult;
        }

        public List<String> getFilterRules() {
            return filterRules;
        }

        public void setFilterRules(List<String> filterRules) {
            this.filterRules = filterRules;
        }

        public List<Object> getBatchProxyParams() {
            return batchProxyParams;
        }

        public void setBatchProxyParams(List<Object> batchProxyParams) {
            this.batchProxyParams = batchProxyParams;
        }

        public Object getBatchProxyResult() {
            return batchProxyResult;
        }

        public void setBatchProxyResult(Object batchProxyResult) {
            this.batchProxyResult = batchProxyResult;
        }


        public HashMap<Integer, Object> getFiledValueCache() {
            return filedValueCache;
        }

        public void setFiledValueCache(HashMap<Integer, Object> filedValueCache) {
            this.filedValueCache = filedValueCache;
        }

        public Object getFiledValueCache(Integer key) {
            return filedValueCache.get(key);
        }

        public void addFiledValueCache(Integer key, Object value) {
            if (filedValueCache == null) {
                filedValueCache = Maps.newHashMap();
            }
            this.filedValueCache.putIfAbsent(key, value);
        }

        public boolean isBatch() {
            if (itemElementMeta == null || itemElementMeta.getBatchProxyMeta() == null
                    || itemElementMeta.getBatchProxyMeta().list().enable() == false || itemElementMeta.getBatchProxyMeta().item().enable() == false) {
                return false;
            }
            //从使用作用域中是否开启优化
            if (orgItems == null || orgItems.isEmpty()) {
                return false;
            }
            return true;
        }
    }
}

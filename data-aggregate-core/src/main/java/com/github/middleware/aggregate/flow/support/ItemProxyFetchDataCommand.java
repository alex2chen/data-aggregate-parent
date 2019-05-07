package com.github.middleware.aggregate.flow.support;

import com.github.middleware.aggregate.config.MergeProperties;
import com.github.middleware.aggregate.flow.ItemCommand;
import com.github.middleware.aggregate.util.Reflections;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.github.middleware.aggregate.contract.ResponseResolver;
import com.github.middleware.aggregate.contract.support.NothingResponseResolver;
import com.github.middleware.aggregate.core.AggregeException;
import com.github.middleware.aggregate.flow.context.Invocation;
import com.github.middleware.aggregate.context.session.ContractInvokeAfterAggregeEvent;
import com.github.middleware.aggregate.context.session.ContractInvokeBeforeAggregeEvent;
import com.github.middleware.aggregate.contract.support.Proxys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/11.
 */
public class ItemProxyFetchDataCommand extends AbstractItemCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemProxyFetchDataCommand.class);
    private Cache<String, Method> proxyMethodCache;
    private Cache<String, ResponseResolver> responseResolverCache;
    private String remoteErrorMsg = "调用远程服务出错[%s.%s]";

    @Override
    public void lasyInit(MergeProperties properties) {
        super.lasyInit(properties);
        proxyMethodCache = CacheBuilder.newBuilder().maximumSize(properties.getProxyMethodCacheSize()).build();
        responseResolverCache = CacheBuilder.newBuilder().maximumSize(properties.getResponseResolverCacheSize()).build();
    }

    @Override
    public String getCommandName() {
        return ItemCommand.PROXYFETCHDATA;
    }

    @Override
    public Integer getOrder() {
        return 20;
    }

    @Override
    public void handle(Object item, Invocation invocation) {
        Object response = request(invocation, item, true);
        Optional.ofNullable(response).ifPresent(x -> invocation.getMetaContext().setProxyResult(response));
    }

    protected Object request(Invocation invocation, Object item, boolean isItemOfBatch) {
        String fireSource = getFireSource(invocation.getMetaContext().getItemElementMeta().getSourceField());
        Object proxy = null;
        String methodName = null;
        Object[] args = null;
        Class<? extends ResponseResolver> resolver = null;
        if (invocation.getMetaContext().isBatch()) {
            if (isItemOfBatch) {
                invocation.getEventBus().post(new ContractInvokeBeforeAggregeEvent(fireSource, invocation.getMetaContext().getProxyParams(), invocation));
                proxy = Proxys.getBean(invocation.getMetaContext().getItemElementMeta().getBatchProxyMeta().item().name());
                methodName = invocation.getMetaContext().getItemElementMeta().getBatchProxyMeta().item().method();
                resolver = invocation.getMetaContext().getItemElementMeta().getBatchProxyMeta().item().resolver();
                if (invocation.getMetaContext().getProxyParams() != null) {
                    args = invocation.getMetaContext().getProxyParams().toArray();
                }
            } else {
                invocation.getEventBus().post(new ContractInvokeBeforeAggregeEvent(fireSource, invocation.getMetaContext().getBatchProxyParams(), invocation));
                proxy = Proxys.getBean(invocation.getMetaContext().getItemElementMeta().getBatchProxyMeta().list().name());
                methodName = invocation.getMetaContext().getItemElementMeta().getBatchProxyMeta().list().method();
                resolver = invocation.getMetaContext().getItemElementMeta().getBatchProxyMeta().list().resolver();
                if (invocation.getMetaContext().getBatchProxyParams() != null) {
                    args = invocation.getMetaContext().getBatchProxyParams().toArray();
                }
            }
        } else {
            invocation.getEventBus().post(new ContractInvokeBeforeAggregeEvent(fireSource, invocation.getMetaContext().getProxyParams(), invocation));
            if (invocation.getMetaContext().getItemElementMeta().getProxyMeta().enable() == false) {
                invocation.getEventBus().post(new ContractInvokeAfterAggregeEvent(fireSource, null, invocation));
                LOGGER.warn("{} 配置AggregeField[proxy]缺失，不做聚合填充!", fireSource);
                return null;
            }
            proxy = Proxys.getBean(invocation.getMetaContext().getItemElementMeta().getProxyMeta().name());
            methodName = invocation.getMetaContext().getItemElementMeta().getProxyMeta().method();
            resolver = invocation.getMetaContext().getItemElementMeta().getProxyMeta().resolver();
            if (invocation.getMetaContext().getProxyParams() != null) {
                args = invocation.getMetaContext().getProxyParams().toArray();
            }
        }
        Object response = request(proxy, methodName, args, invocation.getMetaContext().getItemElementMeta().getFieldMeta().ignoreError());
        return resolverResponse(response, resolver, invocation);
    }

    private Object resolverResponse(Object response, Class<? extends ResponseResolver> resolver, Invocation invocation) {
        if (response == null) {
            return null;
        }
        if (resolver == null || ClassUtils.isAssignable(resolver, NothingResponseResolver.class)) {
            return response;
        }
        try {
            ResponseResolver resolverObj = responseResolverCache.get(resolver.getName(), () -> resolver.newInstance());
            return resolverObj.resolve(response);
        } catch (ExecutionException e) {
            throw new AggregeException(e);
        }
    }

    private String getFireSource(Field field) {
        return String.format("%s.%s", field.getDeclaringClass().getTypeName(), field.getName());
    }

    private Object request(Object proxy, String methodName, Object[] args, boolean ignoreError) {
        Preconditions.checkNotNull(!Strings.isNullOrEmpty(methodName), "参数校验失败，AggregeProxy.method为必填项。");
        String cacheName = proxy.getClass().getName() + methodName;
        try {
            Method method = proxyMethodCache.get(cacheName, () -> Reflections.findMethod(proxy.getClass(), methodName));
            return ReflectionUtils.invokeMethod(method, proxy, args);
        } catch (Exception ex) {
            if (ignoreError) {
                LOGGER.error(String.format(remoteErrorMsg, proxy.getClass().getName(), methodName), ex);
                return null;
            } else {
                throw new AggregeException(String.format(remoteErrorMsg, proxy.getClass().getName(), methodName), ex);
            }
        }
    }

}

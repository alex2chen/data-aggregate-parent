package com.kxtx.middleware.aggregate.flow.support;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kxtx.middleware.aggregate.config.MergeProperties;
import com.kxtx.middleware.aggregate.contract.ResponseResolver;
import com.kxtx.middleware.aggregate.core.AggregeException;
import com.kxtx.middleware.aggregate.flow.context.Invocation;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/15.
 */
@Deprecated
public class ItemProxyResponseResolverCommand extends AbstractItemCommand {
    private Cache<String, ResponseResolver> responseResolverCache;

    @Override
    public void lasyInit(MergeProperties properties) {
        super.lasyInit(properties);
        responseResolverCache = CacheBuilder.newBuilder().maximumSize(properties.getResponseResolverCacheSize()).build();
    }

    @Override
    public String getCommandName() {
        return PROXYRESPONSERESOLVER;
    }

    @Override
    public Integer getOrder() {
        return 25;
    }

    @Override
    public void handle(Object item, Invocation invocation) {
        Class<? extends ResponseResolver> resolver = invocation.getMetaContext().getItemElementMeta().getProxyMeta().resolver();
        Optional.ofNullable(resolver).ifPresent(x -> {
            try {
                ResponseResolver resolverObj = responseResolverCache.get(resolver.getName(), () -> x.newInstance());
                invocation.getMetaContext().setProxyResult(resolverObj.resolve(invocation.getMetaContext().getProxyResult()));
            } catch (ExecutionException e) {
                throw new AggregeException(e);
            }
        });
    }
}

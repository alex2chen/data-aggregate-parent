package com.github.middleware.aggregate.flow.support;

import com.github.middleware.aggregate.config.MergeProperties;
import com.github.middleware.aggregate.flow.ItemCommand;
import com.github.middleware.aggregate.flow.context.Invocation;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.github.middleware.aggregate.contract.ResponseResolver;
import com.github.middleware.aggregate.core.AggregeException;

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
        return ItemCommand.PROXYRESPONSERESOLVER;
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

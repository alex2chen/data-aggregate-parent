package com.kxtx.middleware.aggregate.flow.support;

import com.kxtx.middleware.aggregate.core.AggregeException;
import com.kxtx.middleware.aggregate.flow.context.Invocation;
import com.kxtx.middleware.aggregate.flow.ItemBinder;
import com.kxtx.middleware.aggregate.source.bean.MetaHolder;

import java.util.Optional;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/15.
 */
public class ItemResponseCommand extends AbstractItemCommand {
    @Override
    public String getCommandName() {
        return ITEMRESPONSE;
    }

    @Override
    public Integer getOrder() {
        return 30;
    }

    @Override
    public void handle(Object item, Invocation invocation) {
        try {
            invocation.getMetaContext().getItemElementMeta().getSourceField().setAccessible(true);
            invocation.getMetaContext().getItemElementMeta().getSourceField().set(item, invocation.getMetaContext().getProxyResult());
        } catch (IllegalAccessException e) {
            throw new AggregeException(e);
        }
    }
}

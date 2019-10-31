package com.github.middleware.aggregate.flow.support;

import com.github.middleware.aggregate.flow.ItemCommand;
import com.github.middleware.aggregate.flow.context.Invocation;
import com.github.middleware.aggregate.core.AggregeException;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/15.
 */
public class ItemResponseCommand extends AbstractItemCommand {
    @Override
    public String getCommandName() {
        return ItemCommand.ITEMRESPONSE;
    }

    @Override
    public Integer getOrder() {
        return 30;
    }

    @Override
    public void handle(Object item, Invocation invocation) {
        try {
            invocation.getMetaContext().getItemElementMeta().getSourceField().setField(item, invocation.getMetaContext().getProxyResult());
        } catch (Exception e) {
            throw new AggregeException(e);
        }
    }
}

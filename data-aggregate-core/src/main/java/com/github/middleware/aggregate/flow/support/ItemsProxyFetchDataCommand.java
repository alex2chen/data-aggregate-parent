package com.github.middleware.aggregate.flow.support;

import com.github.middleware.aggregate.flow.ItemCommand;
import com.github.middleware.aggregate.flow.context.Invocation;

import java.util.Optional;


/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/14.
 */
public class ItemsProxyFetchDataCommand extends ItemProxyFetchDataCommand {

    @Override
    public String getCommandName() {
        return ItemCommand.BATCHPROXYFETCHDATA;
    }

    @Override
    public Integer getOrder() {
        return 10;
    }

    @Override
    public void handle(Object item, Invocation invocation) {
        Object response = request(invocation, item, false);
        Optional.ofNullable(response).ifPresent(x -> invocation.getMetaContext().setBatchProxyResult(response));
    }

}

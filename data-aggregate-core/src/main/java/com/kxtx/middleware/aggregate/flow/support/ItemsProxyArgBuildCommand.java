package com.kxtx.middleware.aggregate.flow.support;

import com.kxtx.middleware.aggregate.flow.context.Invocation;
import java.util.Optional;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/14.
 */
public class ItemsProxyArgBuildCommand extends ItemProxyArgBuildCommand {
    @Override
    public String getCommandName() {
        return BATCHPROXYARGBUILD;
    }

    @Override
    public Integer getOrder() {
        return 5;
    }

    @Override
    public void handle(Object item, Invocation invocation) {
        Optional.ofNullable(invocation.getMetaContext().getItemElementMeta().getBatchProxyMeta().list().params()).ifPresent(x -> {
            invocation.getMetaContext().setBatchProxyParams(getParams(x, invocation, item,false));
        });
    }
}

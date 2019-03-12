package com.kxtx.middleware.aggregate.flow.support;

import com.google.common.base.Strings;
import com.kxtx.middleware.aggregate.config.MergeProperties;
import com.kxtx.middleware.aggregate.flow.ItemBinder;
import com.kxtx.middleware.aggregate.flow.ItemCommand;
import com.kxtx.middleware.aggregate.flow.context.Invocation;

import java.util.List;
import java.util.Optional;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/18.
 */
public abstract class AbstractItemCommand implements ItemBinder {
    protected MergeProperties config;

    @Override
    public void lasyInit(MergeProperties properties) {
        this.config = properties;
    }

    @Override
    public void handle(ItemBinder itemBinder, Object item, Invocation invocation) {
        if (!filter(invocation.getMetaContext().getFilterRules())) {
            handle(item, invocation);
        }
        Optional.ofNullable(itemBinder).ifPresent(x -> x.handle(itemBinder, item, invocation));
    }

    public abstract void handle(Object item, Invocation invocation);

    /**
     * 过滤某些命令指令操作
     *
     * @param filterRules * 表示后续执行链全部终止，CommandName表示跳出某些，只有这两种场景
     * @return
     */
    @Override

    public boolean filter(List<String> filterRules) {
        if (filterRules == null || filterRules.isEmpty() || Strings.isNullOrEmpty(getCommandName())) {
            return false;
        }
        if (filterRules.size() == 1 && ItemCommand.ALL.equals(filterRules.get(0))) {
            return true;
        }
        return filterRules.contains(getCommandName());
    }
}

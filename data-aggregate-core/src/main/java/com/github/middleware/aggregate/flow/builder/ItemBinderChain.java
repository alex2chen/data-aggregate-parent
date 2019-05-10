package com.github.middleware.aggregate.flow.builder;

import com.github.middleware.aggregate.config.MergeProperties;
import com.github.middleware.aggregate.flow.ItemBinder;
import com.google.common.collect.Lists;
import com.github.middleware.aggregate.flow.context.Invocation;

import java.util.Collections;
import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/14.
 */
public class ItemBinderChain {
    private List<ItemBinder> itemHandles = Collections.synchronizedList(Lists.newArrayList());

    public void addItemBinder(ItemBinder binder) {
        itemHandles.add(binder);
    }

    public ItemBinder buildInvokerChain() {
        ItemBinder last = null;
        for (int i = itemHandles.size() - 1; i >= 0; i--) {
            ItemBinder handle = itemHandles.get(i);
            ItemBinder next = last;
            last = new ItemBinder() {
                @Override
                public boolean filter(List<String> filterRules) {
                    return handle.filter(filterRules);
                }

                @Override
                public Integer getOrder() {
                    return handle.getOrder();
                }

                @Override
                public String getCommandName() {
                    return handle.getCommandName();
                }

                @Override
                public void lasyInit(MergeProperties properties) {
                    handle.lasyInit(properties);
                }

                @Override
                public void handle(ItemBinder itemBinder, Object item, Invocation invocation) {
                    handle.handle(next, item, invocation);
                }
            };
        }
        return last;
    }

    public List<ItemBinder> getItemHandleChain() {
        return itemHandles;
    }

}

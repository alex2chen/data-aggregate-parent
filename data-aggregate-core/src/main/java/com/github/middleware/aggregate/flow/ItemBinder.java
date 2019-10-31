package com.github.middleware.aggregate.flow;

import com.github.middleware.aggregate.config.MergeProperties;
import com.github.middleware.aggregate.flow.context.Invocation;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/11.
 */
public interface ItemBinder extends ItemCommand, ItemOrder, ItemInterceptor {
    public static final String ISFAST = "aggregate.isFast";

    void lasyInit(MergeProperties properties);

    void handle(ItemBinder itemBinder, Object item, Invocation invocation);
}

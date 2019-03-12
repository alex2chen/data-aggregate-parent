package com.kxtx.middleware.aggregate.flow;

import com.kxtx.middleware.aggregate.flow.context.Invocation;
import com.kxtx.middleware.aggregate.config.MergeProperties;
import com.kxtx.middleware.aggregate.source.bean.MetaHolder;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/11.
 */
public interface ItemBinder extends ItemCommand, ItemOrder, ItemInterceptor {
    void lasyInit(MergeProperties properties);

    void handle(ItemBinder itemBinder, Object item, Invocation invocation);
}

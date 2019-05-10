package com.github.middleware.aggregate.context.session;

import com.github.middleware.aggregate.context.AggregeEvent;

/**
 * @Author: alex
 * @Description: ItemBinder.invoke 执行之前，其实也是MethodProceedCallback.proceed 之后
 * @Date: created in 2019/1/17.
 */
public class DataBindBeforeAggregeEvent extends AggregeEvent {
    private transient Object item;

    public DataBindBeforeAggregeEvent(Object source, Object item) {
        super(source);
        this.item = item;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
}

package com.kxtx.middleware.aggregate.context;
/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/17.
 */
public interface AggregeListener<T extends AggregeEvent> {
    void lister(T event);
}

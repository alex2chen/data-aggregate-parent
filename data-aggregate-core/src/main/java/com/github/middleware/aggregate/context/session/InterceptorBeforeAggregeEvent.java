package com.github.middleware.aggregate.context.session;

import com.github.middleware.aggregate.context.AggregeEvent;

/**
 * @Author: alex
 * @Description: AggregeEngineActivetor.intercept 进入后
 * @Date: created in 2019/1/17.
 */
public class InterceptorBeforeAggregeEvent extends AggregeEvent {

    public InterceptorBeforeAggregeEvent(Object source) {
        super(source);
    }
}

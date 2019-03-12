package com.kxtx.middleware.aggregate.context.container;

import com.kxtx.middleware.aggregate.context.AggregeEvent;

/**
 * @Author: alex
 * @Description: AggregeEngine.start，容器级事件
 * @Date: created in 2019/1/17.
 */
public class StartedAggregeEvent extends AggregeEvent {
    public StartedAggregeEvent(Object source) {
        super(source);
    }
}

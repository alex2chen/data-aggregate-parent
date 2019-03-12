package com.kxtx.middleware.aggregate.context.container;

import com.kxtx.middleware.aggregate.context.AggregeEvent;

/**
 * @Author: alex
 * @Description: AggregeEngine.stop 容器级事件
 * @Date: created in 2019/1/17.
 */
public class StopAggregeEvent extends AggregeEvent {
    public StopAggregeEvent(Object source) {
        super(source);
    }
}

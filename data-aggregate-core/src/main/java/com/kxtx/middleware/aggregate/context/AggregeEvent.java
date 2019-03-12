package com.kxtx.middleware.aggregate.context;

import java.util.Date;
import java.util.EventObject;
/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/17.
 */
public abstract class AggregeEvent extends EventObject {
    private final Date time;

    public AggregeEvent(Object source) {
        super(source);
        this.time = new Date();
    }

    public Date getTime() {
        return this.time;
    }
}

package com.github.middleware.aggregate.context.session;

import com.github.middleware.aggregate.context.AggregeEvent;
import com.github.middleware.aggregate.flow.context.Invocation;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/14.
 */
public class BreakDataBindAggregeEvent extends AggregeEvent {
    private Invocation invocation;
    public BreakDataBindAggregeEvent(Object source) {
        super(source);
    }

    public BreakDataBindAggregeEvent(Object source, Invocation invocation) {
        super(source);
        this.invocation = invocation;
    }

    public Invocation getInvocation() {
        return invocation;
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }
}

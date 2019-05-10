package com.github.middleware.aggregate.context.session;

import com.github.middleware.aggregate.context.AggregeEvent;

/**
 * @Author: alex
 * @Description: AggregeEngineActivetor.intercept 退出前
 * @Date: created in 2019/1/17.
 */
public class InterceptorAfterAggregeEvent extends AggregeEvent {
    private transient Object result;

    public InterceptorAfterAggregeEvent(Object source, Object result) {
        super(source);
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

package com.github.middleware.aggregate.context.session;

import com.github.middleware.aggregate.context.AggregeEvent;
import com.github.middleware.aggregate.flow.context.Invocation;

/**
 * @Author: alex
 * @Description: ItemProxyFetchDataCommand.invoke之后，后一个链之前
 * @Date: created in 2019/1/18.
 */
public class ContractInvokeAfterAggregeEvent extends AggregeEvent {
    private transient Object response;
    private transient Invocation invocation;

    public ContractInvokeAfterAggregeEvent(Object fireSource, Object response, Invocation invocation) {
        super(fireSource);
        this.response = response;
        this.invocation = invocation;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public Invocation getInvocation() {
        return invocation;
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }
}

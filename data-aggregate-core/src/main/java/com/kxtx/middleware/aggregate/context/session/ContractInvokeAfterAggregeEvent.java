package com.kxtx.middleware.aggregate.context.session;

import com.kxtx.middleware.aggregate.context.AggregeEvent;
import com.kxtx.middleware.aggregate.flow.context.Invocation;

/**
 * @Author: alex
 * @Description: ItemProxyFetchDataCommand.invoke之后，后一个链之前
 * @Date: created in 2019/1/18.
 */
public class ContractInvokeAfterAggregeEvent extends AggregeEvent {
    private Object response;
    private Invocation invocation;

    public ContractInvokeAfterAggregeEvent(Object source) {
        super(source);
    }

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

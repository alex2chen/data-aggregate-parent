package com.github.middleware.aggregate.context.session;

import com.github.middleware.aggregate.context.AggregeEvent;
import com.github.middleware.aggregate.flow.context.Invocation;

/**
 * @Author: alex
 * @Description: ItemProxyFetchDataCommand.invoke之前，前一个链（ItemProxyArgBuildCommand）之后
 * @Date: created in 2019/1/18.
 */
public class ContractInvokeBeforeAggregeEvent extends AggregeEvent {
    private Invocation invocation;
    private Object arg;

    public ContractInvokeBeforeAggregeEvent(Object source) {
        super(source);
    }

    public ContractInvokeBeforeAggregeEvent(Object source, Object arg, Invocation invocation) {
        super(source);
        this.arg = arg;
        this.invocation = invocation;
    }

    public Object getArg() {
        return arg;
    }

    public void setArg(Object arg) {
        this.arg = arg;
    }

    public Invocation getInvocation() {
        return invocation;
    }

    public void setInvocation(Invocation invocation) {
        this.invocation = invocation;
    }
}

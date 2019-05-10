package com.github.middleware.aggregate.context.session;

import com.github.middleware.aggregate.context.AggregeListener;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/18.
 */
public class ContractInvokeBeforeAggregeListener implements AggregeListener<ContractInvokeBeforeAggregeEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractInvokeBeforeAggregeListener.class);

    @Subscribe
    @Override
    public void lister(ContractInvokeBeforeAggregeEvent event) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("field={},arg={}",event.getSource(), event.getArg());
        if (isNullParams(event)) {
            //转发事件
            event.getInvocation().getEventBus().post(new BreakDataBindAggregeEvent(event.getSource(), event.getInvocation()));
        }

    }

    private boolean isNullParams(ContractInvokeBeforeAggregeEvent event) {
        if (event.getArg() == null) {
            return true;
        }
        if (event.getArg() instanceof List) {
            List eventArg = (List) event.getArg();
            long nullCount = eventArg.stream().filter(Objects::isNull).count();
            if (eventArg.size() == nullCount) {
                return true;
            }
        }
        return false;
    }
}

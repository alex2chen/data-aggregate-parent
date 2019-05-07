package com.github.middleware.aggregate.context.session;

import com.github.middleware.aggregate.context.AggregeListener;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/18.
 */
public class ContractInvokeAfterAggregeListener implements AggregeListener<ContractInvokeAfterAggregeEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContractInvokeAfterAggregeListener.class);

    @Subscribe
    @Override
    public void lister(ContractInvokeAfterAggregeEvent event) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("field={},response={}",event.getSource(),event.getResponse());
        if (event.getResponse() == null) {
            //转发事件
            event.getInvocation().getEventBus().post(new BreakDataBindAggregeEvent(event.getSource(), event.getInvocation()));
        }
    }
}

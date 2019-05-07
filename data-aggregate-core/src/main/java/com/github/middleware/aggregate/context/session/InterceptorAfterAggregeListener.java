package com.github.middleware.aggregate.context.session;

import com.github.middleware.aggregate.context.AggregeContext;
import com.github.middleware.aggregate.context.AggregeListener;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/17.
 */
public class InterceptorAfterAggregeListener implements AggregeListener<InterceptorAfterAggregeEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorAfterAggregeListener.class);

    @Subscribe
    @Override
    public void lister(InterceptorAfterAggregeEvent event) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("method={},data={}", event.getSource(), event.getResult());
        AggregeContext.removeContext();
    }
}

package com.kxtx.middleware.aggregate.context.session;

import com.google.common.eventbus.Subscribe;
import com.kxtx.middleware.aggregate.context.AggregeContext;
import com.kxtx.middleware.aggregate.context.AggregeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/17.
 */
public class InterceptorBeforeAggregeListener implements AggregeListener<InterceptorBeforeAggregeEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(InterceptorBeforeAggregeListener.class);

    @Subscribe
    @Override
    public void lister(InterceptorBeforeAggregeEvent event) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("method={}" ,event.getSource());
        AggregeContext.getContext().setFireSource(event.getSource().toString());
    }
}

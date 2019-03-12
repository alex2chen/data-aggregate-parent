package com.kxtx.middleware.aggregate.context.container;

import com.google.common.eventbus.Subscribe;
import com.kxtx.middleware.aggregate.context.AggregeListener;
import com.kxtx.middleware.aggregate.context.session.InterceptorBeforeAggregeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/17.
 */
public class StartedAggregeListener implements AggregeListener<StartedAggregeEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartedAggregeListener.class);

    @Subscribe
    @Override
    public void lister(StartedAggregeEvent event) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Started");
    }
}

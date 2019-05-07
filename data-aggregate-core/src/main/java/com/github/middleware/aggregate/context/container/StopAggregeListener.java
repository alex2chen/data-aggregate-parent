package com.github.middleware.aggregate.context.container;

import com.github.middleware.aggregate.context.AggregeListener;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/17.
 */
public class StopAggregeListener implements AggregeListener<StopAggregeEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StopAggregeListener.class);

    @Subscribe
    @Override
    public void lister(StopAggregeEvent event) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Stop");
    }
}

package com.kxtx.middleware.aggregate.context.session;

import com.google.common.eventbus.Subscribe;
import com.kxtx.middleware.aggregate.context.AggregeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/17.
 */
public class DataBindBeforeAggregeListener implements AggregeListener<DataBindBeforeAggregeEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataBindBeforeAggregeListener.class);

    @Subscribe
    @Override
    public void lister(DataBindBeforeAggregeEvent event) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("data={}", event.getItem());
    }
}

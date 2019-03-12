package com.kxtx.middleware.aggregate.context.session;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.kxtx.middleware.aggregate.context.AggregeListener;
import com.kxtx.middleware.aggregate.flow.ItemCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/14.
 */
public class BreakDataBindAggregeEventListener implements AggregeListener<BreakDataBindAggregeEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BreakDataBindAggregeEventListener.class);
    private List<String> breakFilterRules = Lists.newArrayList(ItemCommand.ALL);

    @Subscribe
    @Override
    public void lister(BreakDataBindAggregeEvent event) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("field={}", event.getSource());
        event.getInvocation().getMetaContext().setFilterRules(breakFilterRules);
    }
}

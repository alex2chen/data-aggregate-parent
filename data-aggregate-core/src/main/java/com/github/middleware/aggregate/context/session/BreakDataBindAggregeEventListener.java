package com.github.middleware.aggregate.context.session;

import com.github.middleware.aggregate.context.AggregeListener;
import com.github.middleware.aggregate.flow.ItemCommand;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
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

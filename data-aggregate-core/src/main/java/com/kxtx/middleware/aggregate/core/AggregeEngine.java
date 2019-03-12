package com.kxtx.middleware.aggregate.core;

import com.google.common.eventbus.EventBus;
import com.kxtx.middleware.aggregate.annonation.AggregeEnable;
import com.kxtx.middleware.aggregate.config.MergeProperties;

/**
 * @Author: alex
 * @Description: 功能描述：完成对数据项（Item）的聚合及绑定
 * @Date: created in 2019/1/15.
 */
public interface AggregeEngine {
    void loadConfig(MergeProperties config);

    void start();

    boolean isRunning();

    void stop();

    Object dataBind(AggregeEnable enable, Object item);

    EventBus getEventBus();
}

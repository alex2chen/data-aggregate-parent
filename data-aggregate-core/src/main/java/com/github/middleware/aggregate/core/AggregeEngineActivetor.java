package com.github.middleware.aggregate.core;


/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/15.
 */
public interface AggregeEngineActivetor {
    Object intercept(RequestPayLoad request);

    AggregeEngine getAggregeEngine();
}

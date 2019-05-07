package com.github.middleware.aggregate.core.support;

import com.github.middleware.aggregate.core.AggregeEngineActivetor;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/15.
 */
public class AggregeEngineActivetorFactory {

    public AggregeEngineActivetor create(Object clientService) {
        if (clientService == null) {
            //client aop方式
            return new DefaultAggregeEngineActivetor();
        }
        return new GenClzProxyAggregeEngineActivetor();
    }
}

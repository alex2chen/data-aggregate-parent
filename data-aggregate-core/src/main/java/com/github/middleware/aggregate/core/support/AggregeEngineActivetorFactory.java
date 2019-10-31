package com.github.middleware.aggregate.core.support;

import com.github.middleware.aggregate.core.AggregeEngineActivetor;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/15.
 */
public class AggregeEngineActivetorFactory {
    private static final AggregeEngineActivetorFactory FACTORY = new AggregeEngineActivetorFactory();

    private AggregeEngineActivetorFactory() {
    }

    public static AggregeEngineActivetorFactory getInstance() {
        return FACTORY;
    }

    public AggregeEngineActivetor create(Boolean sateful) {
        if (sateful == null) {
            return getResource();
        }
        return new GenClzProxyAggregeEngineActivetor();
    }

    public AggregeEngineActivetor getResource() {
        return ResourceHolder.AOP_ENGINE;
    }

    private static class ResourceHolder {
        // This will be lazily initialised
        public static AggregeEngineActivetor AOP_ENGINE = new DefaultAggregeEngineActivetor();
    }
}

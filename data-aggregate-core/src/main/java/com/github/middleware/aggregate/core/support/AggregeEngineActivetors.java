package com.github.middleware.aggregate.core.support;

import com.github.middleware.aggregate.core.AggregeEngineActivetor;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/15.
 */
public class AggregeEngineActivetors {
    private AggregeEngineActivetors() {

    }

    public static AggregeEngineActivetor getEngineAcitvetor() {
        return AggregeEngineActivetorFactory.getInstance().create(null);
    }

    public static AggregeEngineActivetor getEngineAcitvetorStateful() {
        return AggregeEngineActivetorFactory.getInstance().create(true);
    }

}

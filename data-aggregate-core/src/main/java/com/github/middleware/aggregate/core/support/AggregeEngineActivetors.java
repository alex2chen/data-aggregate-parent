package com.github.middleware.aggregate.core.support;

import com.github.middleware.aggregate.core.AggregeEngineActivetor;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/15.
 */
public class AggregeEngineActivetors {
    private static AggregeEngineActivetorFactory factory = new AggregeEngineActivetorFactory();
    private AggregeEngineActivetors(){
        
    }
    public static AggregeEngineActivetor getEngineAcitvetor() {
        return factory.create(null);
    }

    public static AggregeEngineActivetor getEngineAcitvetor(Object clientService) {
        return factory.create(clientService);
    }

}

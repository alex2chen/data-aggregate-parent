package com.kxtx.middleware.aggregate.core;

import java.lang.reflect.Method;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/15.
 */
public interface AggregeEngineActivetor {
    Object intercept(Method method, MethodProceedCallback callback);

    AggregeEngine getAggregeEngine();
}

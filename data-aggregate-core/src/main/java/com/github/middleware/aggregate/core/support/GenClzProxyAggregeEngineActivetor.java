package com.github.middleware.aggregate.core.support;


import com.github.middleware.aggregate.core.AggregeEngine;
import com.github.middleware.aggregate.core.AggregeEngineActivetor;
import com.github.middleware.aggregate.core.MethodProceedCallback;

import java.lang.reflect.Method;

/**
 * @Author: alex
 * @Description: 非spring-aop客户端（代理类不存在），需先先生成代理类
 * @Date: created in 2019/1/15.
 */
public class GenClzProxyAggregeEngineActivetor implements AggregeEngineActivetor {
    private AggregeEngineActivetor delegete;

    @Override
    public Object intercept(Method method, MethodProceedCallback callback) {
        /**
         * todo:待完善
         * 基于客户端生成代理类，缓存代理类
         * 思路：
         * 前置处理
         * 把后置处理，进入代码植入
         */
        //不需要返回值，一样不大
        return delegete;
    }

    @Override
    public AggregeEngine getAggregeEngine() {
        return null;
    }
}

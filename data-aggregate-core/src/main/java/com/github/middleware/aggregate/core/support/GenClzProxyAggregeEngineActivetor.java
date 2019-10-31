package com.github.middleware.aggregate.core.support;

import com.github.middleware.aggregate.core.RequestPayLoad;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * @Author: alex
 * @Description: 非spring-aop客户端（代理类不存在），需先先生成代理类
 * @Date: created in 2019/1/15.
 */
@NotThreadSafe
public class GenClzProxyAggregeEngineActivetor extends AbstractAggregeEngineActivetor {

    public GenClzProxyAggregeEngineActivetor() {
        initEngine();
    }

    private void initEngine() {
        if (!aggregeEngine.isRunning()) {
            aggregeEngine.start();
        }
    }

    @Override
    protected Object doDataBind(RequestPayLoad request) {
        return aggregeEngine.dataBind(request);
    }
}

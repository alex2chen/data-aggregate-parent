package com.github.middleware.aggregate.core.support;

import com.github.middleware.aggregate.core.*;
import com.google.common.base.Preconditions;
import com.github.middleware.aggregate.annonation.AggregeEnable;
import com.github.middleware.aggregate.context.session.DataBindBeforeAggregeEvent;
import com.github.middleware.aggregate.context.session.InterceptorAfterAggregeEvent;
import com.github.middleware.aggregate.context.session.InterceptorBeforeAggregeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.lang.reflect.Method;

/**
 * @Author: alex
 * @Description: 客户端已经使用AOP切入（代理类已存在）
 * @Date: created in 2019/1/15.
 */
@ThreadSafe
public class DefaultAggregeEngineActivetor extends AbstractAggregeEngineActivetor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAggregeEngineActivetor.class);

    @Override
    protected Object doDataBind(RequestPayLoad request) {
        return aggregeEngine.dataBind(request);
    }
}

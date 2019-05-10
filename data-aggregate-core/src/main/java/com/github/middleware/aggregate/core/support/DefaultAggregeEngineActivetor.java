package com.github.middleware.aggregate.core.support;

import com.github.middleware.aggregate.core.*;
import com.google.common.base.Preconditions;
import com.github.middleware.aggregate.annonation.AggregeEnable;
import com.github.middleware.aggregate.context.session.DataBindBeforeAggregeEvent;
import com.github.middleware.aggregate.context.session.InterceptorAfterAggregeEvent;
import com.github.middleware.aggregate.context.session.InterceptorBeforeAggregeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Author: alex
 * @Description: 客户端已经使用AOP切入（代理类已存在）
 * @Date: created in 2019/1/15.
 */
public class DefaultAggregeEngineActivetor implements AggregeEngineActivetor {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAggregeEngineActivetor.class);
    private AggregeEngine aggregeEngine;

    public DefaultAggregeEngineActivetor() {
        ExtensionLoaders.getExtensionLoader(AggregeEngine.class).flatMap(ExtensionLoader::getExtension).ifPresent(x -> aggregeEngine = x);
    }

    @Override
    public Object intercept(Method method, MethodProceedCallback callback) {
        Preconditions.checkNotNull(method, "target method is required.");
        Preconditions.checkNotNull(aggregeEngine, "aggregeEngine is required.");
        Preconditions.checkState(aggregeEngine.isRunning(), "aggregeEngine must be start.");
        String eventSource = String.format("%s.%s", method.getDeclaringClass().getName(), method.getName());
        try {
            aggregeEngine.getEventBus().post(new InterceptorBeforeAggregeEvent(eventSource));
            AggregeEnable enable = method.getAnnotation(AggregeEnable.class);
            Object item = callback.proceed();
            if (item == null) {
                LOGGER.warn("{}的返回值为null.", eventSource);
                return null;
            }
            aggregeEngine.getEventBus().post(new DataBindBeforeAggregeEvent(eventSource, item));
            Object result = aggregeEngine.dataBind(enable, item);
            aggregeEngine.getEventBus().post(new InterceptorAfterAggregeEvent(eventSource, result));
            return result;
        } catch (AggregeException ex1) {
            aggregeEngine.getEventBus().post(new InterceptorAfterAggregeEvent(eventSource, null));
            throw ex1;
        } catch (Exception ex) {
            aggregeEngine.getEventBus().post(new InterceptorAfterAggregeEvent(eventSource, null));
            throw new AggregeException(ex);
        }
    }

    @Override
    public AggregeEngine getAggregeEngine() {
        return aggregeEngine;
    }
}

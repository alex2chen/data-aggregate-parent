package com.github.middleware.aggregate.core.support;

import com.github.middleware.aggregate.annonation.AggregeEnable;
import com.github.middleware.aggregate.context.session.DataBindBeforeAggregeEvent;
import com.github.middleware.aggregate.context.session.InterceptorAfterAggregeEvent;
import com.github.middleware.aggregate.context.session.InterceptorBeforeAggregeEvent;
import com.github.middleware.aggregate.core.*;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/30
 */
public abstract class AbstractAggregeEngineActivetor implements AggregeEngineActivetor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAggregeEngineActivetor.class);
    protected AggregeEngine aggregeEngine;

    public AbstractAggregeEngineActivetor() {
        ExtensionLoaders.getExtensionLoader(AggregeEngine.class).flatMap(ExtensionLoader::getExtension).ifPresent(x -> aggregeEngine = x);
    }

    @Override
    public Object intercept(RequestPayLoad request) {
        Preconditions.checkNotNull(request.getEventSource(), "target method is required.");
        Preconditions.checkNotNull(aggregeEngine, "aggregeEngine is required.");
        Preconditions.checkState(aggregeEngine.isRunning(), "aggregeEngine must be start.");
        try {
            aggregeEngine.getEventBus().post(new InterceptorBeforeAggregeEvent(request.getEventSource()));
            Object item = request.getData();
            if (request.getLasyLoadData() != null) {
                item = request.getLasyLoadData().get();
            }
            if (item == null) {
                LOGGER.warn("{}的返回值为null.", request.getEventSource());
                return null;
            }
            request.setData(item);
            aggregeEngine.getEventBus().post(new DataBindBeforeAggregeEvent(request.getEventSource(), item));
            Object result = doDataBind(request);
            aggregeEngine.getEventBus().post(new InterceptorAfterAggregeEvent(request.getEventSource(), result));
            return result;
        } catch (AggregeException ex1) {
            aggregeEngine.getEventBus().post(new InterceptorAfterAggregeEvent(request.getEventSource(), null));
            throw ex1;
        } catch (Exception ex) {
            aggregeEngine.getEventBus().post(new InterceptorAfterAggregeEvent(request.getEventSource(), null));
            throw new AggregeException(ex);
        }
    }

    @Override
    public AggregeEngine getAggregeEngine() {
        return aggregeEngine;
    }

    protected abstract Object doDataBind(RequestPayLoad request);
}

package com.github.middleware.aggregate.spring.init;

import com.github.middleware.aggregate.annonation.AggregeEnable;
import com.github.middleware.aggregate.config.MergeProperties;
import com.github.middleware.aggregate.core.AggregeEngineActivetor;
import com.github.middleware.aggregate.core.RequestPayLoad;
import com.github.middleware.aggregate.core.support.AggregeEngineActivetors;
import com.github.middleware.aggregate.spring.util.AopUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/21.
 */
@Aspect
public class DataAggregeAspect implements InitializingBean, DisposableBean, ApplicationContextAware {
    private AggregeEngineActivetor ENGINE_ACTIVETOR;
    private MergeProperties config;
    private static ApplicationContext appContext;

    @Pointcut("@annotation(com.github.middleware.aggregate.annonation.AggregeEnable)")
    public void dataAggregeAnnotationPointcut() {
        //todo
    }

    @Around("dataAggregeAnnotationPointcut()")
    public Object methodsAnnotatedWithAggerege(final ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = AopUtils.getMethodFromTarget(joinPoint);
        Preconditions.checkNotNull(method, "failed to get method from joinPoint: %s", new Object[]{joinPoint});
        AggregeEnable enable = method.getAnnotation(AggregeEnable.class);
        RequestPayLoad request = new RequestPayLoad(method, appContext, () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                Throwables.throwIfUnchecked(throwable);
                return null;
            }
        },enable);
        return ENGINE_ACTIVETOR.intercept(request);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ENGINE_ACTIVETOR = AggregeEngineActivetors.getEngineAcitvetor();
        Optional.ofNullable(config).ifPresent(x -> ENGINE_ACTIVETOR.getAggregeEngine().loadConfig(config));
        ENGINE_ACTIVETOR.getAggregeEngine().start();
    }

    @Override
    public void destroy() throws Exception {
        ENGINE_ACTIVETOR.getAggregeEngine().stop();
    }

    public void setConfig(MergeProperties config) {
        this.config = config;
    }

    public AggregeEngineActivetor getEngineActivetor() {
        return ENGINE_ACTIVETOR;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }
}

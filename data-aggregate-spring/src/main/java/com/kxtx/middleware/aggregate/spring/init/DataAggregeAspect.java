package com.kxtx.middleware.aggregate.spring.init;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.kxtx.middleware.aggregate.config.MergeProperties;
import com.kxtx.middleware.aggregate.core.AggregeEngineActivetor;
import com.kxtx.middleware.aggregate.core.support.AggregeEngineActivetors;
import com.kxtx.middleware.aggregate.spring.util.AopUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/21.
 */
@Aspect
public class DataAggregeAspect implements InitializingBean, DisposableBean {
    private static AggregeEngineActivetor ENGINE_ACTIVETOR;
    private MergeProperties config;

    public DataAggregeAspect() {
    }

    @Pointcut("@annotation(com.kxtx.middleware.aggregate.annonation.AggregeEnable)")
    public void dataAggregeAnnotationPointcut() {

    }

    @Around("dataAggregeAnnotationPointcut()")
    public Object methodsAnnotatedWithAggerege(final ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = AopUtils.getMethodFromTarget(joinPoint);
        Preconditions.checkNotNull(method, "failed to get method from joinPoint: %s", new Object[]{joinPoint});
        return ENGINE_ACTIVETOR.intercept(method, () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                Throwables.propagate(throwable);
                return null;
            }
        });
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
}

package com.github.middleware.aggregate.spring.util;

import com.github.middleware.aggregate.core.AggregeException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/23.
 */
public class AopUtils {
    private AopUtils() {
    }

    public static Method getMethodFromTarget(JoinPoint joinPoint) {
        Method method = null;
        if (joinPoint.getSignature() instanceof MethodSignature) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            method = getDeclaredMethod(joinPoint.getTarget().getClass(), signature.getName(), getParameterTypes(joinPoint));
        }

        return method;
    }

    public static Class[] getParameterTypes(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getParameterTypes();
    }

    public static Method getDeclaredMethod(Class<?> targetClass, String methodName, Class... parameterTypes) {
        Method method = null;
        try {
            method = ReflectionUtils.findMethod(targetClass, methodName, parameterTypes);
        } catch (Exception ex) {
            throw new AggregeException(ex);
        }
        return method;
    }

}

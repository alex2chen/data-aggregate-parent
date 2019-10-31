package com.github.middleware.aggregate.source.bean;

import com.github.middleware.aggregate.flow.ItemBinder;
import com.github.middleware.aggregate.util.Reflections;
import com.google.common.base.Preconditions;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/30
 */
public class MethodVisitor {
    private Method source;
    private Boolean isFast = true;
    private Reflections.FastMethodInvoker fastMethod;

    public MethodVisitor(Class<?> clazz, String methodName, Object... args) {
        findMethod(clazz, methodName, args);
        if (Boolean.getBoolean(ItemBinder.ISFAST)) {
            isFast = true;
        }
    }

    private void findMethod(Class<?> clz, String methodName, Object... args) {
        Class[] parameterTypes = null;
        source = Reflections.getAccessibleMethodNotSafe(clz, methodName);
        Preconditions.checkNotNull(methodName, "Could not find method [" + methodName + "] on target [" + clz + "]");
        fastMethod = Reflections.create(clz, source);
    }

    public Object invokeMethod(Object proxy, Object... args) {
        if (isFast) {
            return fastMethod.invoke(proxy, args);
        }
        return ReflectionUtils.invokeMethod(source, proxy, args);
    }
}

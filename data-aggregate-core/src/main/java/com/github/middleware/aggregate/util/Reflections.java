package com.github.middleware.aggregate.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/12.
 */
public abstract class Reflections {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reflections.class);
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap(8);
    private static ConcurrentMap<Class<?>, FastClass> fastClassMap = Maps.newConcurrentMap();

    private Reflections() {
    }

    public static Method getAccessibleMethodNotSafe(Class<?> clazz, String methodName) {
        Preconditions.checkNotNull(clazz, "Class must not be null");
        Preconditions.checkNotNull(methodName, "Method name must not be null");
        HashMultiset<String> multiset = HashMultiset.create();
        for (Class searchType = clazz; searchType != null; searchType = searchType.getSuperclass()) {
            if (searchType.isInterface()) {
                continue;
            }
            Method[] methods = searchType.getDeclaredMethods();
            Method[] var5 = methods;
            Stream.of(var5).forEach(x -> multiset.add(x.getName()));
            int var6 = methods.length;
            for (int var7 = 0; var7 < var6; ++var7) {
                Method method = var5[var7];
                if (methodName.equals(method.getName())) {
                    if (multiset.count(methodName) != 1) {
                        throw new RuntimeException(String.format("%s暂时不支持方法[%s]重写.", clazz.getName(), methodName));
                    }
                    return method;
                }
            }
        }

        return null;
    }

    public static Method getAccessibleMethod(Class<?> clazz, String methodName, Class... parameterTypes) {
        Preconditions.checkNotNull(clazz, "Class must not be null");
        Preconditions.checkNotNull(methodName, "Method name must not be null");
        Class[] theParameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        wrapClassses(theParameterTypes);
        Class searchType = clazz;

        while (searchType != Object.class) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, theParameterTypes);
                makeAccessible(method);
                return method;
            } catch (NoSuchMethodException var6) {
                searchType = searchType.getSuperclass();
            }
        }
        return null;
    }

    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    private static void wrapClassses(Class<?>[] source) {
        for (int i = 0; i < source.length; ++i) {
            Class<?> wrapClass = (Class) primitiveWrapperTypeMap.get(source[i]);
            if (wrapClass != null) {
                source[i] = wrapClass;
            }
        }

    }

    public static FastMethodInvoker create(Class<?> clz, Method method) {
        FastClass fastClz = fastClassMap.get(clz);
        if (fastClz == null) {
            fastClz = FastClass.create(clz);
            fastClassMap.put(clz, fastClz);
        }
        return new FastMethodInvoker(fastClz.getMethod(method));
    }

    public static class FastMethodInvoker {
        private final FastMethod fastMethod;

        public FastMethodInvoker(FastMethod fastMethod) {
            this.fastMethod = fastMethod;
        }

        public Object invoke(Object obj, Object... args) {
            try {
                return fastMethod.invoke(obj, args);
            } catch (Exception e) {
                Throwables.throwIfUnchecked(e);
            }
            return null;
        }
    }
}

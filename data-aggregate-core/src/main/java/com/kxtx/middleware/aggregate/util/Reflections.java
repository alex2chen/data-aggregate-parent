package com.kxtx.middleware.aggregate.util;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/12.
 */
public class Reflections {
    private static final Logger LOGGER = LoggerFactory.getLogger(Reflections.class);

    public static Method findMethod(Class<?> clazz, String name) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
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
                if (name.equals(method.getName())) {
                    if (multiset.count(name) != 1) {
                        LOGGER.warn("{}中{}方法出现了多个匹配记录！", clazz.getName(), name);
                    }
                    return method;
                }
            }
        }

        return null;
    }
}

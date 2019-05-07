package com.github.middleware.aggregate.core.support;

import com.github.middleware.aggregate.core.ExtensionLoader;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/9.
 */
public class ExtensionLoaders {
    private static final ConcurrentMap<String, ExtensionLoader<?>> EXTLOADERS = Maps.newConcurrentMap();
    private static final Object monitor = new Object();

    public static <T> Optional<ExtensionLoader<T>> getExtensionLoader(Class<T> clz) {
        String key = loadClz(clz);
        return Optional.ofNullable((ExtensionLoader<T>) EXTLOADERS.get(key));
    }

    private static <T> String loadClz(Class<T> clz) {
        Preconditions.checkNotNull(clz, "clz is requird..");
        String key = clz.getName();
        if (!EXTLOADERS.containsKey(key)) {
            synchronized (monitor) {
                EXTLOADERS.put(key, new DefaultExtensionLoader<T>(clz));
            }
        }
        return key;
    }
}

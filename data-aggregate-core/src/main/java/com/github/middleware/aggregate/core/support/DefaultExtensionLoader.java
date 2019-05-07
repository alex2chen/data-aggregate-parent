package com.github.middleware.aggregate.core.support;

import com.github.middleware.aggregate.core.ExtensionLoader;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/9.
 */
public class DefaultExtensionLoader<T> implements ExtensionLoader<T> {
    private final ServiceLoader<T> serviceLoader;
    private List<T> extTypes;

    public DefaultExtensionLoader(Class<T> clz) {
        this(clz, Thread.currentThread().getContextClassLoader());
    }

    public DefaultExtensionLoader(Class<T> clz, ClassLoader classLoader) {
        serviceLoader = ServiceLoader.load(clz, classLoader);

    }

    @Override
    public List<T> getExtensions() {
        if (extTypes == null) {
            synchronized (this) {
                if (extTypes == null) {
                    extTypes = new ArrayList<T>();
                    for (T service : serviceLoader) {
                        extTypes.add(service);
                    }
                }
            }
        }
        return extTypes;
    }

    @Override
    public Optional<T> getExtension() {
        if (isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(getExtensions().get(0));
    }

    private boolean isEmpty() {
        List<T> list = getExtensions();
        return list == null || list.isEmpty();
    }

    @Override
    public Optional<T> getExtension(String regex) {
        if (isEmpty()) {
            return Optional.empty();
        }
        if (!Strings.isNullOrEmpty(regex)) {
            AtomicReference<T> result = new AtomicReference<T>();
            List<T> list = getExtensions();
            list.stream().filter(x -> x.getClass().getName().contains(regex)).findFirst().ifPresent(x -> result.set(x));
            return Optional.ofNullable(result.get());
        }
        return Optional.ofNullable(getExtensions().get(0));
    }
}


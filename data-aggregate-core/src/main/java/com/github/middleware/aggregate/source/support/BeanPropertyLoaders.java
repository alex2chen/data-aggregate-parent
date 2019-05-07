package com.github.middleware.aggregate.source.support;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.github.middleware.aggregate.core.AggregeException;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.stream.Stream;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/10.
 */
@Deprecated
public class BeanPropertyLoaders {
    private static Cache<String, BeanInfo> beanCahes;

    public static PropertyDescriptor getPropDesc(Class sourceClz, String propertyName, int cacheSize) {
        try {
            if (beanCahes == null) {
                beanCahes = CacheBuilder.newBuilder().maximumSize(cacheSize).build();
            }
            BeanInfo beanInfo = beanCahes.get(sourceClz.getName(), () -> Introspector.getBeanInfo(sourceClz));
            return Stream.of(beanInfo.getPropertyDescriptors()).filter(x -> x.getName().equals(propertyName)).findFirst().get();
        } catch (Exception ex) {
            throw new AggregeException(ex);
        }
    }
}

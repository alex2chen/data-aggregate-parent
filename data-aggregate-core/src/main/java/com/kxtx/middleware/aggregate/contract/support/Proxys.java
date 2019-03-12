package com.kxtx.middleware.aggregate.contract.support;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.kxtx.middleware.aggregate.contract.ServiceConsumerEndpoint;
import com.kxtx.middleware.aggregate.core.AggregeException;
import com.kxtx.middleware.aggregate.core.support.ExtensionLoaders;
import org.springframework.util.ClassUtils;

import java.util.Map;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/9.
 */
public final class Proxys {
    //todo:改掉
    private static final Map<String, Object> serviceEndpoint = Maps.newConcurrentMap();

    private static Object monitor = new Object();

    public static Object getBean(String proxyName) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(proxyName), "获取Client Proxy失败，参数AggregeProxy[name]为必填项。");
        if (serviceEndpoint.containsKey(proxyName)) {
            return serviceEndpoint.get(proxyName);
        }
        if (proxyName.startsWith("#")) {
            loadBean(proxyName);
        } else {
            newBean(proxyName);
        }
        return serviceEndpoint.get(proxyName);
    }

    private static void newBean(String cls) {
        Object proxy = ServiceEndpointCreators.instance(cls);
        synchronized (monitor) {
            serviceEndpoint.put(cls, proxy);
        }
    }

    private static void loadBean(String beanName) {
        synchronized (monitor) {
            ExtensionLoaders.getExtensionLoader(ServiceConsumerEndpoint.class).flatMap(x -> x.getExtension()).ifPresent(x -> {
                String name = beanName.substring(1);
                Object serviceBean = x.getServiceBean(name);
                serviceEndpoint.put(beanName, serviceBean);
            });
        }
    }

    public static class ServiceEndpointCreators {
        public static Object instance(String cls) {
            try {
                return ClassUtils.forName(cls, Thread.currentThread().getContextClassLoader()).newInstance();
            } catch (Exception e) {
                throw new AggregeException(e);
            }
        }
    }
}

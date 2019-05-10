package com.github.middleware.aggregate.spring.init;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Optional;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/21.
 */
public class SpringContextHolder implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringContextHolder.class);
    private static ApplicationContext appContext;

    public static final <T> T getBean(Class<T> requireType) {
        return appContext.getBean(requireType);
    }

    public static <T> Optional<T> getBean(String beanName) {
        if (Strings.isNullOrEmpty(beanName) || appContext == null) {
            return Optional.empty();
        }
        try {
            Object var1 = appContext.getBean(beanName);
            return (Optional<T>) Optional.ofNullable(var1);
        } catch (Exception var3) {
            LOGGER.error("获取spring bean[" + beanName + "]错误：" + var3.getMessage(), var3);
            return Optional.empty();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        appContext = applicationContext;
    }
}

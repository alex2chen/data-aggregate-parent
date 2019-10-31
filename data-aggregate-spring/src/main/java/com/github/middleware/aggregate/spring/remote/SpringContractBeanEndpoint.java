package com.github.middleware.aggregate.spring.remote;

import com.github.middleware.aggregate.core.AggregeException;
import com.github.middleware.aggregate.spring.init.SpringContextHolder;
import com.github.middleware.aggregate.contract.ServiceConsumerEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/9.
 */
public class SpringContractBeanEndpoint implements ServiceConsumerEndpoint {
    private ApplicationContext applicationContext;

    @Override
    public Object getServiceBean(String beanName, Object ioc) {
        Assert.notNull(ioc, "未找到对应的applicationContext.");
        if (applicationContext == null) {
            applicationContext = (ApplicationContext) ioc;
        }
        return SpringContextHolder.getBean(applicationContext, beanName).orElseThrow(() -> new AggregeException("找不到ID为[" + beanName + "]的spring bean实现!"));
    }
}

package com.github.middleware.aggregate.spring.remote;

import com.github.middleware.aggregate.core.AggregeException;
import com.github.middleware.aggregate.spring.init.SpringContextHolder;
import com.github.middleware.aggregate.contract.ServiceConsumerEndpoint;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/9.
 */
public class SpringContractBeanEndpoint implements ServiceConsumerEndpoint {
    @Override
    public Object getServiceBean(String beanName) {
        return SpringContextHolder.getBean(beanName).orElseThrow(() -> new AggregeException("找不到ID为[" + beanName + "]的spring bean实现!"));
    }
}
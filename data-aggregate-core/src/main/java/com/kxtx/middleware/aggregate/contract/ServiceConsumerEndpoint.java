package com.kxtx.middleware.aggregate.contract;


/**
 * @Author: alex
 * @Description: esb、eureka、tcp、普通的bean
 * @Date: created in 2019/1/9.
 */
public interface ServiceConsumerEndpoint {
    Object getServiceBean(String beanName);
}

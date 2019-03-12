package com.kxtx.middleware.aggregate.contract;

/**
 * 包含自定义convert，过滤
 */
/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/10.
 */
public interface ResponseResolver {
    Object resolve(Object response);
}

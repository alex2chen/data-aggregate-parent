package com.kxtx.middleware.aggregate.contract.support;

import com.kxtx.middleware.aggregate.contract.ResponseResolver;
/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/10.
 */
public class NothingResponseResolver implements ResponseResolver {
    @Override
    public Object resolve(Object response) {
        return response;
    }
}

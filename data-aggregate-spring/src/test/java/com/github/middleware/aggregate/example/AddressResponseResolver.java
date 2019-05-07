package com.github.middleware.aggregate.example;

import com.github.middleware.aggregate.contract.ResponseResolver;
import com.github.middleware.aggregate.example.domain.Address;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/15.
 */
public class AddressResponseResolver implements ResponseResolver {
    @Override
    public Object resolve(Object response) {
        if (response == null) return response;
        if (response instanceof Address) {
            Address address = (Address) response;
            address.setPhone("15201829272");
        }
        return response;
    }
}

package com.github.middleware.aggregate.example.domain;

import com.github.middleware.aggregate.example.AddressResponseResolver;
import com.github.middleware.aggregate.annonation.AggregeField;
import com.github.middleware.aggregate.annonation.AggregeProxy;
import com.github.middleware.aggregate.annonation.AggregeProxyArg;
import com.github.middleware.aggregate.constant.ArgGetMode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/23.
 */
@ToString
@Getter
@Setter
public class Order {
    private Integer id;
    private String orderNo;
    private Integer orderSourceType;
    @AggregeField(proxy = @AggregeProxy(name = "com.github.middleware.aggregate.example.GlobalDictionaryCode", method = "getDictValue", params = {@AggregeProxyArg(paramValue = "orderSource"), @AggregeProxyArg(key = "orderSourceType")}))
    private String orderSource;
    private Integer addressId;
    @AggregeField(proxy = @AggregeProxy(name = "#addressRepository", method = "getAddressById", params = {@AggregeProxyArg(argGetMode = ArgGetMode.SESSION, key = "addressId")}, resolver = AddressResponseResolver.class))
    private Address address;
    private List<Integer> productIds;
    @AggregeField(proxy = @AggregeProxy(name = "#productService", method = "listByIds", params = {@AggregeProxyArg(key = "productIds", paramName = "ids")}))
    private List<Product> products;
    public Order(int id) {
        this.id = id;
    }

}

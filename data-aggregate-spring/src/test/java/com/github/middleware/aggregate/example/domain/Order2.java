package com.github.middleware.aggregate.example.domain;

import com.github.middleware.aggregate.annonation.AggregeBatchProxy;
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
 * @Date: created in 2019/2/14.
 */
@ToString
@Getter
@Setter
public class Order2 {
    private Integer id;
    private String orderNo;
    private Integer orderSourceType;
    private String orderSource;
    private Integer addressId;
    @AggregeField(batchProxy = @AggregeBatchProxy(list = @AggregeProxy(name = "#addressRepository", method = "getAddressByIds", params = {@AggregeProxyArg(argGetMode = ArgGetMode.item, key = "addressId")}),
            item = @AggregeProxy(name = "#addressRepository", method = "getAddressById2", params = {@AggregeProxyArg(argGetMode = ArgGetMode.batch), @AggregeProxyArg(argGetMode = ArgGetMode.item, key = "addressId")})))
    private Address address;
    private List<Integer> productIds;
    @AggregeField(proxy = @AggregeProxy(name = "#productService", method = "listByIds", params = {@AggregeProxyArg(key = "productIds", paramName = "ids")}), batchProxy = @AggregeBatchProxy(
            list = @AggregeProxy(name = "#productService", method = "listByIds", params = {@AggregeProxyArg(key = "productIds")}),
            item = @AggregeProxy(name = "#productService", method = "listByIds2", params = {@AggregeProxyArg(argGetMode = ArgGetMode.batch), @AggregeProxyArg(key = "productIds")})))
    private List<Product> products;
    public Order2(int id) {
        this.id = id;
    }
}

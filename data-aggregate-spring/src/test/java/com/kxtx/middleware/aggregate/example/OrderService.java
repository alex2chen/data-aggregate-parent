package com.kxtx.middleware.aggregate.example;

import com.google.common.collect.Lists;
import com.kxtx.middleware.aggregate.annonation.AggregeEnable;
import com.kxtx.middleware.aggregate.context.AggregeContext;
import com.kxtx.middleware.aggregate.example.domain.Order;
import com.kxtx.middleware.aggregate.example.domain.Order2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/23.
 */
@Service
public class OrderService {

    @AggregeEnable
    public Order getOrder(int id) {
        Order order = new Order(id);
        order.setProductIds(Lists.newArrayList(1));
        order.setOrderSourceType(1);
        AggregeContext.getContext().setAttachment("addressId", 2);
        return order;
    }

    @AggregeEnable(parallel = true)
    public Order getOrder2(int id) {
        Order order = new Order(id);
        AggregeContext.getContext().setAttachment("addressId", 2);
        order.setOrderSourceType(1);
        return order;
    }

    @AggregeEnable(parallel = true)
    public List<Order> getOrders() {
        Order order = new Order(2);
        AggregeContext.getContext().setAttachment("addressId", 2);
        order.setOrderSourceType(1);
        Order order2 = new Order(3);
        order2.setOrderSourceType(2);
        return Lists.newArrayList(order, order2);
    }

    @AggregeEnable
    public List<Order2> getOrders1() {
        Order2 order = new Order2(2);
        order.setAddressId(3);
//        order.setProductIds(Lists.newArrayList(7,8));
        Order2 order2 = new Order2(3);
        order2.setAddressId(5);
//        order2.setProductIds(Lists.newArrayList(12,32));
        return Lists.newArrayList(order, order2);
    }

    @AggregeEnable
    public Order2 getOrder22() {
        Order2 order = new Order2(2);
        order.setAddressId(3);
        order.setProductIds(Lists.newArrayList(12, 32));
        return order;
    }

    @AggregeEnable(parallel = true)
    public List<Order2> getOrders3() {
        Order2 order = new Order2(2);
        order.setAddressId(3);
        Order2 order2 = new Order2(3);
        order2.setAddressId(5);
        return Lists.newArrayList(order, order2);
    }
}

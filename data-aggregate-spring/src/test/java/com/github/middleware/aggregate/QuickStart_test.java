package com.github.middleware.aggregate;

import com.github.middleware.aggregate.example.OrderService;
import com.github.middleware.aggregate.example.domain.Order;
import com.github.middleware.aggregate.example.domain.Order2;
import com.github.middleware.aggregate.spring.init.DataAggregeAspect;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class QuickStart_test {
    @Autowired
    private OrderService orderService;
    @Autowired
    private DataAggregeAspect aggregeAspect;

    /**
     * 手动的方式调用
     */
    @Test
    public void manualFire() {
        Method target = null;
        aggregeAspect.getEngineActivetor().intercept(target, () -> new Object());
    }

    /**
     * 单VO同步
     */
    @Test
    public void getOrder() {
        Order order = orderService.getOrder(1);
        Assert.assertNotNull(order);
        System.out.printf("result:%s", order);
    }

    /**
     * 单VO并发
     */
    @Test
    public void getOrderAsync() {
        Order order = orderService.getOrder2(1);
        Assert.assertNotNull(order);
        System.out.printf("result:%s", order);
    }

    /**
     * List<VO>并发，不开启（批量）优化
     */
    @Test
    public void getOrders() {
        List<Order> orders = orderService.getOrders();
        Assert.assertNotNull(orders);
        System.out.printf("result:%s", orders);
    }

    /**
     * 批量聚合填充
     */
    @Test
    public void getOrders2() {
        List<Order2> orders = orderService.getOrders1();
        Assert.assertNotNull(orders);
        System.out.printf("result:%s", orders);
    }

    /**
     * 单VO和批量同上注解存在，兼容性
     */
    @Test
    public void getOrder22() {
        Order2 orders = orderService.getOrder22();
        Assert.assertNotNull(orders);
        System.out.printf("result:%s", orders);
    }

    /**
     * 批量聚合填充，，不开启优化
     */
    @Test
    public void getOrders3() {
        List<Order2> orders = orderService.getOrders3();
        Assert.assertNotNull(orders);
        System.out.printf("result:%s", orders);
    }
}

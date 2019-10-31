package com.github.middleware.aggregate;

import com.github.middleware.aggregate.context.AggregeContext;
import com.github.middleware.aggregate.core.RequestPayLoad;
import com.github.middleware.aggregate.core.support.AggregeEngineActivetors;
import com.github.middleware.aggregate.example.OrderService;
import com.github.middleware.aggregate.example.domain.Order;
import com.github.middleware.aggregate.example.domain.Order2;
import com.github.middleware.aggregate.spring.init.DataAggregeAspect;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    @Autowired
    private ApplicationContext applicationContext;

    @BeforeClass
    public static void init() {
        System.setProperty("aggregate.isFast", "true");
    }

    /**
     * 手动的方式调用
     * 很多时候，我们并不需要AOP的模式运行的，提供工具模式
     */
    @Test()
    public void manualFire() {
        Order order = new Order(1);
        order.setProductIds(Lists.newArrayList(1));
        order.setOrderSourceType(1);
        RequestPayLoad<Order> request = new RequestPayLoad("manualFire", applicationContext, order, true);
        AggregeEngineActivetors.getEngineAcitvetorStateful().intercept(request);
        System.out.println(order);
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

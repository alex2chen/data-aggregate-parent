# 数据聚合组件

标签（空格分隔）： 服务化 代码重构 数据聚合

[TOC] 目录

---
# 项目背景
	随着服务化越来越多，单个VO或BO属性拆分之痛已成为业务系统日益突出的问题。大量重复的代码逻辑都是用来处理依赖（查询方面的数据聚合操作），而通讯方式通常都是rest-http。
	
# 功能特性	
- [x]	多数据源支持：springBean和普通类（理论上包含http-eureka、esb）
- [x]   后置填充（目标方法执行后介入）
- [x]	单VO及List<VO>支持
- [x]	并发填充支持
- [x]   List<VO>开启性能优化
- [x]   接入方的参数传递支持Item模式（VO提供的Field）和Session模式（目标方法中提供）
- [x]	解决强弱依赖问题，弱依赖（如果请求服务发生错误，忽略错误，不进行结果集填充）强依赖（任何一属性填充失败快速失败）
- [x]	自动转化及手动转换；
- [x]   本地缓存支持（有限的缓存），未缓存目标方法的业务数据

## 约束
	1.多个数据项同时依赖一个服务的数据，为了性能，建议将这几个数据项封装为一个新的VO，避免多次填充
	2.它不是一个服务编排，而是数据依赖解析服务
	
## 尚且未支持
	1.数据填充目前仅支持一层依赖结构（比如：Order中属性Product，Product中也有@AggregateField注解，它不会对Product再做填充处理）
	2.表达式支持
	
# 使用说明
## 聚合标志说明
```java
public @interface AggregeEnable {
    /**
     * 开启并行填充
     *
     * @return
     */
    boolean parallel() default false;
}
public @interface AggregeField {
    /**
     * 远程或本地服务，服务于单VO
     *
     * @return
     */
    AggregeProxy proxy() default @AggregeProxy(enable = false);

    /**
     * 开启性能优化(批量问题)，执行顺序是batchProxy->proxy
     *
     * @return
     */
    AggregeBatchProxy batchProxy() default @AggregeBatchProxy;

    /**
     * 是否忽略错误
     *
     * @return
     */
    boolean ignoreError() default false;
}
public @interface AggregeProxy {
    /**
     * 是否启用
     *
     * @return
     */
    boolean enable() default true;

    /**
     * 目标服务名
     * 名称以#开头，代表从当前IOC中获取，否则类全名，如下：
     * #omsProductServcie
     * com.kxtx.oms.contract.ProductServcie
     *
     * @return
     */
    String name() default "";

    /**
     * 调用方法
     *
     * @return
     */
    String method() default "";

    /**
     * 查询值
     *
     * @return
     */
    AggregeProxyArg[] params() default {};

    /**
     * 是否缓存
     *
     * @return
     */
    @Deprecated
    boolean cache() default false;

    /**
     * @return
     */
    Class<? extends ResponseResolver> resolver() default NothingResponseResolver.class;
}
public @interface AggregeProxyArg {
    /**
     * 参数获取模式
     *
     * @return
     */
    ArgGetMode argGetMode() default ArgGetMode.item;

    /**
     * contract的参数名
     *
     * @return
     */
    @Deprecated
    String paramName() default "";

    /**
     * @return
     */
    String paramValue() default "";

    /**
     * 为vo的属性名或session中的key
     *
     * @return
     */
    String key() default "";
}
public @interface AggregeBatchProxy {
    /**
     * @return
     */
    AggregeProxy list() default @AggregeProxy(enable = false);

    /**
     *
     * @return
     */
    AggregeProxy item() default @AggregeProxy(enable = false);
}
```
## spring3.x环境
```xml
<dependency>
    <groupId>com.kxtx.middleware</groupId>
    <artifactId>data-aggregate-spring</artifactId>
    <version>1.0.0</version>
</dependency>
``` 
更多示例，请参考[快速开始](data-aggregate-spring/src/test/java/com/kxtx/middleware/aggregate/QuickStart_test.java)  
```java
@ToString
@Getter
@Setter
public class Order {
    private Integer id;
    private String orderNo;
    private Integer orderSourceType;   
    @AggregeField(proxy = @AggregeProxy(name = "com.kxtx.middleware.aggregate.example.GlobalDictionaryCode", method = "getDictValue", params = {@AggregeProxyArg(paramValue = "orderSource"), @AggregeProxyArg(key = "orderSourceType")}))
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

    public Order(int id) {
        this.id = id;
    }
}
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
    public List<Order> getOrders() {
        Order order = new Order(2);
        AggregeContext.getContext().setAttachment("addressId", 2);
        order.setOrderSourceType(1);
        Order order2 = new Order(3);
        order2.setOrderSourceType(2);
        return Lists.newArrayList(order, order2);
    }
}    
``` 
配置说明：  
1. data-aggrege.yml为框架配置文件
2. spring.xml 为配置加载类

## spring boot环境
### 1.Maven添加依赖
```xml
<dependency>
    <groupId>com.kxtx.middleware</groupId>
    <artifactId>data-aggregate-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```
### 2.启动类加注解
	@EnableDataAggregate
###	3.application.yml配置
```html
kxtx:
    aggregate:
        clzMetasCacheSize: 10000
        responseResolverCacheSize: 1000
        proxyMethodCacheSize: 10000
        maxBlockTimeout: 30000
        corePoolSize: 6
        keepAliveTime: 500
        workQueueSize: 800
```

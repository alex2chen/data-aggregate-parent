package com.github.middleware.aggregate.annonation;

import com.github.middleware.aggregate.contract.ResponseResolver;
import com.github.middleware.aggregate.contract.support.NothingResponseResolver;

import java.lang.annotation.*;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/7.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
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
     * com.github.oms.contract.ProductServcie
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

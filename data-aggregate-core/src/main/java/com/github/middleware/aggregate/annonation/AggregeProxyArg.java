package com.github.middleware.aggregate.annonation;

import com.github.middleware.aggregate.constant.ArgGetMode;

import java.lang.annotation.*;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/7.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
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

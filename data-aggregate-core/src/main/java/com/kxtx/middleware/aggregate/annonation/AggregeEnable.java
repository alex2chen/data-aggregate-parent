package com.kxtx.middleware.aggregate.annonation;


import java.lang.annotation.*;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/7.
 */
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AggregeEnable {
    /**
     * 开启并行填充
     *
     * @return
     */
    boolean parallel() default false;
}

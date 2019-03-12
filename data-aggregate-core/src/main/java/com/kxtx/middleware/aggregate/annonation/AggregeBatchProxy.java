package com.kxtx.middleware.aggregate.annonation;

import java.lang.annotation.*;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/14.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
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

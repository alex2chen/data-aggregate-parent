package com.github.middleware.aggregate.annonation;

import java.lang.annotation.*;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/7.
 */
@Target(value = {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
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

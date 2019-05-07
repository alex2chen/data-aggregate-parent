package com.github.middleware.aggregate.spring.boot.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/13.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AggregateAutoConfiguration.class)
public @interface EnableDataAggregate {
}

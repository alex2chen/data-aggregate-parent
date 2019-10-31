package com.github.middleware.aggregate.core;

import java.util.function.Supplier;

/**
 * @Author: alex
 * @Description: 封装被拦截的业务方法，提供触发点
 * @Date: created in 2019/1/11.
 */
@FunctionalInterface
public interface LasyLoadData extends Supplier {
}

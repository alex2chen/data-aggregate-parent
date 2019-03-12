package com.kxtx.middleware.aggregate.flow;

import java.util.List;

/**
 * @Author: alex
 * @Description: 过滤某些Command
 * @Date: created in 2019/2/14.
 */
public interface ItemInterceptor {
    boolean filter(List<String> filterRules);
}

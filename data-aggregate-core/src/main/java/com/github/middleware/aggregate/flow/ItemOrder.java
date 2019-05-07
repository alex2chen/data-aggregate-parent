package com.github.middleware.aggregate.flow;

/**
 * @Author: alex
 * @Description: 排序
 * @Date: created in 2019/1/18.
 */
public interface ItemOrder {
    /**
     * 尽可能以x*5的倍数，便于拓展（可对任何链进行介入）
     *
     * @return
     */
    Integer getOrder();
}

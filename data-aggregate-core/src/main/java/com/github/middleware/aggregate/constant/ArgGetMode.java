package com.github.middleware.aggregate.constant;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/10.
 */
public enum ArgGetMode {

    /**
     * 从当前VO中获取
     */
    ITEM,
    /**
     * 从session中获取
     */
    SESSION,
    /**
     * 从batch中获取
     */
    BATCH
}
package com.kxtx.middleware.aggregate.constant;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/10.
 */
public enum ArgGetMode {

    /**
     * 从当前VO中获取
     */
    item,
    /**
     * 从session中获取
     */
    session,
    /**
     * 从batch中获取
     */
    batch
}
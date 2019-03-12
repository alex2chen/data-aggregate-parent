package com.kxtx.middleware.aggregate.context;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: alex
 * @Description: 它主要服务于客户接入端与本组件的交互，致力于解决参数传递问题：
 * 1.支持从item（DataVo）中提取属性
 * 2.支持复杂参数（item中可能不存在的）从session（ThreadLocal）中获取
 * @Date: created in 2019/1/14.
 */
public class AggregeContext {
    private static final ThreadLocal<AggregeContext> LOCAL = new ThreadLocal<AggregeContext>() {
        @Override
        protected AggregeContext initialValue() {
            return new AggregeContext();
        }
    };
    /**
     * prxoy(contract)所需的参数
     */
    private final Map<String, Object> attachments = new HashMap();
    /**
     * 触发源
     */
    private String fireSource;

    public static AggregeContext getContext() {
        return LOCAL.get();
    }

    public static void removeContext() {
        LOCAL.remove();
    }

    public Object getAttachment(String key) {
        return this.attachments.get(key);
    }

    public AggregeContext setAttachment(String key, Object value) {
        if (value == null) {
            this.attachments.remove(key);
        } else {
            this.attachments.put(key, value);
        }
        return this;
    }

    public AggregeContext setAttachments(Map<String, String> attachment) {
        this.attachments.clear();
        if (attachment != null && attachment.size() > 0) {
            this.attachments.putAll(attachment);
        }
        return this;
    }

    public String getFireSource() {
        return fireSource;
    }

    public void setFireSource(String fireSource) {
        this.fireSource = fireSource;
    }
}

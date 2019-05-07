package com.github.middleware.aggregate.config;

/**
 * @Author: alex
 * @Description: 全局配置
 * @Date: created in 2019/1/18.
 */
public class MergeProperties {
    private Integer clzMetasCacheSize;
    private Integer responseResolverCacheSize;
    private Integer proxyMethodCacheSize;
    private Integer corePoolSize;
    /**
     * 秒
     */
    private Integer keepAliveTime = 300;
    private Integer workQueueSize = 500;

    public MergeProperties() {
        corePoolSize = Runtime.getRuntime().availableProcessors();
    }

    /**
     * 异步获取结果时最大阻塞时间（默认：20秒）
     */
    private Integer maxBlockTimeout = 20000;

    private String[] scanPackages;

    public Integer getClzMetasCacheSize() {
        return clzMetasCacheSize;
    }

    public void setClzMetasCacheSize(Integer clzMetasCacheSize) {
        this.clzMetasCacheSize = clzMetasCacheSize;
    }

    public Integer getResponseResolverCacheSize() {
        return responseResolverCacheSize;
    }

    public void setResponseResolverCacheSize(Integer responseResolverCacheSize) {
        this.responseResolverCacheSize = responseResolverCacheSize;
    }

    public Integer getProxyMethodCacheSize() {
        return proxyMethodCacheSize;
    }

    public void setProxyMethodCacheSize(Integer proxyMethodCacheSize) {
        this.proxyMethodCacheSize = proxyMethodCacheSize;
    }
    @Deprecated
    public String[] getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    public Integer getMaxBlockTimeout() {
        return maxBlockTimeout;
    }

    public void setMaxBlockTimeout(Integer maxBlockTimeout) {
        this.maxBlockTimeout = maxBlockTimeout;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Integer keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public Integer getWorkQueueSize() {
        return workQueueSize;
    }

    public void setWorkQueueSize(Integer workQueueSize) {
        this.workQueueSize = workQueueSize;
    }
}

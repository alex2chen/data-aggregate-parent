package com.github.middleware.aggregate.flow;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/12.
 */
public interface ItemCommand {
    String getCommandName();

    public static final String ALL = "*";
    public static final String REQUESTVALIDATE = "RequestValidate";
    public static final String PROXYARGBUILD = "ProxyArgBuild";
    public static final String PROXYFETCHDATA = "ProxyFetchData";
    public static final String PROXYRESPONSERESOLVER = "ProxyResponseResolver";
    public static final String ITEMRESPONSE = "ItemResponse";
    public static final String BATCHPROXYARGBUILD = "batchProxyArgBuild";
    public static final String BATCHPROXYFETCHDATA = "batchProxyFetchData";
}

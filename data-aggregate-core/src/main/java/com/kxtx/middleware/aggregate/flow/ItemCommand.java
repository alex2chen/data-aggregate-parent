package com.kxtx.middleware.aggregate.flow;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/12.
 */
public interface ItemCommand {
    String getCommandName();

    String ALL = "*";
    String REQUESTVALIDATE = "RequestValidate";
    String PROXYARGBUILD = "ProxyArgBuild";
    String PROXYFETCHDATA = "ProxyFetchData";
    String PROXYRESPONSERESOLVER = "ProxyResponseResolver";
    String ITEMRESPONSE = "ItemResponse";
    String BATCHPROXYARGBUILD = "batchProxyArgBuild";
    String BATCHPROXYFETCHDATA = "batchProxyFetchData";
}

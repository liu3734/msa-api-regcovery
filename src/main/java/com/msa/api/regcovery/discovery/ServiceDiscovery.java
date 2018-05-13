package com.msa.api.regcovery.discovery;

/**
 * The interface Service discovery.
 *
 * @ClassName: ServiceDiscovery
 * @Description: 服务发现接口类
 * @Author: sxp
 * @Date: 15 :30 2018/4/28
 * @Version: 1.0.0
 */
public interface ServiceDiscovery {

    /**
     * 服务发现
     * Discover string.
     *
     * @param name the name
     * @return the string
     */
    String discover(String name);
}

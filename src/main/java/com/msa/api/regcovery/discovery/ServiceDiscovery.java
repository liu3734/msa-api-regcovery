package com.msa.api.regcovery.discovery;

/**
 * The interface Service discovery.
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

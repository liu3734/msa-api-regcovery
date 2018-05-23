package com.msa.api.regcovery.endpoint;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.msa.api.regcovery.Constant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 * The type Service operation endpoint.
 */
@Path("/")
public class ApiResource {

    /**
     * The Zk servers.
     */
    @Value("${spring.rpc.server.registry-address}")
    private String zkServers = "localhost:2181";

    /**
     * Sets zk servers.
     *
     * @param zkServers the zk servers
     */
    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }

    /**
     * Gets services.
     *
     * @return the services
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/services")
    public Map<String, Object> getServices() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("result", "no");
        map.put("message", "服务不存在！");
        ZkClient zkClient = new ZkClient(zkServers, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
        try {
            if (zkClient.exists(Constant.ZK_REGISTRY)) {
                List<ServiceNode> serviceNodes = Lists.newArrayList();
                List<String> serviceNameNodes = zkClient.getChildren(Constant.ZK_REGISTRY);
                if (CollectionUtils.isEmpty(serviceNameNodes)) {
                    return map;
                }
                serviceNameNodes.parallelStream().forEach(serviceNameNode -> {
                    List<String> serviceAddrNodes = zkClient.getChildren(Constant.ZK_REGISTRY + "/" + serviceNameNode);
                    if (CollectionUtils.isEmpty(serviceAddrNodes)) {
                        return;
                    }
                    serviceAddrNodes.parallelStream().map(serviceAddrNode -> {
                        String serviceAddr = zkClient.readData(Constant.ZK_REGISTRY + "/" + serviceNameNode + "/" + serviceAddrNode);
                        ServiceNode serviceNode = ServiceNode.builder()
                                .rootNode(Constant.ZK_REGISTRY)
                                .childNode(serviceNameNode)
                                .subChildNode(serviceAddrNode)
                                .serviceAddr(serviceAddr)
                                .build();
                        return serviceNode;
                    }).forEach(serviceNodes::add);
                });
                map.put("result", "ok");
                map.put("data", serviceNodes);
                map.remove("message");
            } else {
                return map;
            }
        } finally {
            zkClient.close();
        }
        return map;
    }

    /**
     * The type Service node.
     */
    @Setter
    @Getter
    @Builder
    private static final class ServiceNode {
        /**
         * The Root node.
         */
        private String rootNode;

        /**
         * The Child node.
         */
        private String childNode;

        /**
         * The Sub child node.
         */
        private String subChildNode;

        /**
         * The Service addr.
         */
        private String serviceAddr;
    }
}

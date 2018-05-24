package com.msa.api.regcovery.registry;

import com.msa.api.regcovery.Constant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

import java.util.Objects;

/**
 * The type Service registry.
 */
@Data
@Slf4j
public class ServiceRegistry {
    /**
     * The Zk address.
     */
    private String zkAddress;
    /**
     * The Zk client.
     */
    private ZkClient zkClient;

    /**
     * Init zk client.
     */
    private void initZkClient() {
        zkClient = new ZkClient(zkAddress,  Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
        log.debug(">>>>>>>>>===connect to zookeeper");
    }

    /**
     * 服务注册
     * Registry.
     *
     * @param serviceName    the service name
     * @param serviceAddress the service address
     */
    public void registry(String serviceName, String serviceAddress) {
        if (Objects.isNull(zkClient)) {
            initZkClient();
        }
        // 创建registry节点（持久）
        String registryPath = Constant.ZK_REGISTRY;
        if (!zkClient.exists(registryPath)) {
            zkClient.createPersistent(registryPath);
            log.debug(">>>>>>>>>===create registry  node: {}" , registryPath);
        }

        // 创建service节点（持久）
        String servicePath = registryPath + "/" + serviceName;
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            log.debug(">>>>>>>>>===create service node: {}", servicePath);
        }

        // 创建address节点（临时）
        String addressPath = servicePath + "/address-";
        String addressNode = zkClient.createEphemeralSequential(addressPath, serviceAddress);
        log.debug(">>>>>>>>>===create address node: {}", addressNode);
    }
}

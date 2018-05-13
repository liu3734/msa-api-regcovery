package com.msa.api.regcovery.discovery;

import com.google.common.collect.Lists;
import com.msa.api.regcovery.Constant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Zk service discovery.
 *
 * @ClassName: ZkServiceDiscovery
 * @Description: 基于zookeeper的服务发现类
 * @Author: sxp
 * @Date: 15 :29 2018/4/28
 * @Version: 1.0.0
 */
@Slf4j
@Data
public class ZkServiceDiscovery implements ServiceDiscovery {
    /**
     * The Zk address.
     */
    private String zkAddress;

    /**
     * 缓存所有的服务IP和port
     * The Address cache.
     */
    private final List<String> addressCache = Lists.newCopyOnWriteArrayList();

    /**
     * The Zk client.
     */
    private ZkClient zkClient;

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        zkClient = new ZkClient(zkAddress, Constant.ZK_SESSION_TIMEOUT, Constant.ZK_CONNECTION_TIMEOUT);
        log.debug(">>>>>>>>>===connect to zookeeper");
    }

    /**
     * 服务发现
     * Discover string.
     *
     * @param name the name
     * @return the string
     */
    @Override
    public String discover(String name) {
        try {
            String servicePath = Constant.ZK_REGISTRY + "/" + name;
            // 获取service node
            if (!zkClient.exists(servicePath)) {
                throw new RuntimeException(String.format(">>>>>>>>>===can not find any service node on path {}", servicePath));
            }

            // 从本地缓存获取某个服务地址
            String address;
            int addressCacheSize = addressCache.size();
            if (addressCacheSize > 0) {
                if (addressCacheSize == 1) {
                    address = addressCache.get(0);
                } else {
                    address = addressCache.get(ThreadLocalRandom.current().nextInt(addressCacheSize));
                    log.debug(">>>>>>>>>===get only address node: {}", address);
                }

                // 从zk服务注册中心获取某个服务地址
            } else {
                List<String> addressList = zkClient.getChildren(servicePath);
                addressCache.addAll(addressList);
                // 监听servicePath下的子文件是否发生变化
                zkClient.subscribeChildChanges(servicePath, (parentPath, currentChilds) -> {
                        log.info(">>>>>>>>>===servicePath[{}] is changed", parentPath);
                        addressCache.clear();
                        addressCache.addAll(currentChilds);
                });
                if (CollectionUtils.isEmpty(addressList)) {
                    throw new RuntimeException(String.format(">>>>>>>>>===can not find any address node on path {}", servicePath));
                }
                int nodes = addressList.size();
                if (nodes == 1) {
                    address = addressList.get(0);
                } else {
                    address = addressList.get(ThreadLocalRandom.current().nextInt(nodes));
                    log.debug(">>>>>>>>>===get only address node: {}", address);
                }
            }

            // 获取ip和端口号
            String addressPath = servicePath + "/" + address;
            String hostAndPort = zkClient.readData(addressPath);
            return hostAndPort;
        } catch (Exception e) {
            log.error(">>>>>>>>>===service discovery exception", e);
            zkClient.close();
        }
        return null;
    }
}

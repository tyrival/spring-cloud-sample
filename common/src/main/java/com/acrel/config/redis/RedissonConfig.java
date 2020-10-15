package com.acrel.config.redis;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableConfigurationProperties(RedisConfig.class)
public class RedissonConfig {

    @Autowired
    RedisConfig redisConfig;

    @Configuration
    @ConditionalOnClass({Redisson.class})
    @ConditionalOnExpression("'${spring.redis.mode}'=='single' or '${spring.redis.mode}'=='cluster' or '${spring.redis.mode}'=='sentinel'")
    protected class RedissonSingleClientConfiguration {

        /**
         * 单机模式 redisson 客户端
         */
        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "single")
        RedissonClient redissonSingle() {
            Config config = new Config();
            String host = redisConfig.getHost();
            Integer port = redisConfig.getPort();
            String node = "redis://" + host + ":" + port;
            SingleServerConfig serverConfig = config.useSingleServer()
                    .setAddress(node)
                    .setTimeout(redisConfig.getRedisson().getConnTimeout())
                    .setConnectionPoolSize(redisConfig.getRedisson().getSize())
                    .setConnectionMinimumIdleSize(redisConfig.getRedisson().getMinIdle());
            if (StringUtils.isNotBlank(redisConfig.getPassword())) {
                serverConfig.setPassword(redisConfig.getPassword());
            }
            return Redisson.create(config);
        }

        /**
         * 集群模式的 redisson 客户端
         */
        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "cluster")
        RedissonClient redissonCluster() {
            System.out.println("cluster redisConfigProperties:" + redisConfig.getCluster());

            Config config = new Config();
            String[] nodes = redisConfig.getCluster().getNodes().split(",");
            List<String> newNodes = new ArrayList(nodes.length);
            Arrays.stream(nodes).forEach((node) -> newNodes.add(
                    node.startsWith("redis://") ? node : "redis://" + node));

            ClusterServersConfig serverConfig = config.useClusterServers()
                    .addNodeAddress(newNodes.toArray(new String[0]))
                    .setScanInterval(redisConfig.getRedisson().getScanInterval())
                    .setIdleConnectionTimeout(redisConfig.getRedisson().getSoTimeout())
                    .setConnectTimeout(redisConfig.getRedisson().getConnTimeout())
                    .setRetryAttempts(redisConfig.getRedisson().getRetryAttempts())
                    .setRetryInterval(redisConfig.getRedisson().getRetryInterval())
                    .setMasterConnectionPoolSize(redisConfig.getRedisson()
                            .getMasterConnectionPoolSize())
                    .setSlaveConnectionPoolSize(redisConfig.getRedisson()
                            .getSlaveConnectionPoolSize())
                    .setTimeout(redisConfig.getTimeout());
            if (StringUtils.isNotBlank(redisConfig.getPassword())) {
                serverConfig.setPassword(redisConfig.getPassword());
            }
            return Redisson.create(config);
        }

        /**
         * 哨兵模式 redisson 客户端
         */
        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "sentinel")
        RedissonClient redissonSentinel() {
            System.out.println("sentinel redisConfigProperties:" + redisConfig.getSentinel());
            Config config = new Config();
            String[] nodes = redisConfig.getSentinel().getNodes().split(",");
            List<String> newNodes = new ArrayList(nodes.length);
            Arrays.stream(nodes).forEach((node) -> newNodes.add(
                    node.startsWith("redis://") ? node : "redis://" + node));

            SentinelServersConfig serverConfig = config.useSentinelServers()
                    .addSentinelAddress(newNodes.toArray(new String[0]))
                    .setMasterName(redisConfig.getSentinel().getMaster())
                    .setReadMode(ReadMode.SLAVE)
                    .setTimeout(redisConfig.getTimeout())
                    .setMasterConnectionPoolSize(redisConfig.getRedisson().getSize())
                    .setSlaveConnectionPoolSize(redisConfig.getRedisson().getSize());

            if (StringUtils.isNotBlank(redisConfig.getPassword())) {
                serverConfig.setPassword(redisConfig.getPassword());
            }

            return Redisson.create(config);
        }
    }
}
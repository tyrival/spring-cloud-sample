package com.acrel.config.redis;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ZhouChenyu
 **/
@Data
@ToString
@Component
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {

    /**
     * redis模式：single-单机 cluster-集群 sentinel-哨兵
     */
    private String mode = "cluster";

    private String host = "localhost";

    private int port = 6379;

    /**
     * 密码
     */
    private String password;

    /**
     * 数据库
     */
    private int database = 0;

    /**
     * 等待节点回复命令的时间。该时间从命令发送成功时开始计时
     */
    private int timeout;

    /**
     * Redisson配置
     */
    private RedissonProperties redisson;

    /**
     * 集群配置
     */
    private RedisClusterProperties cluster;

    /**
     * 哨兵配置
     */
    private RedisSentinelProperties sentinel;
}
package com.acrel.config.redis;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RedisClusterProperties {

    /**
     * 集群节点
     */
    private String nodes;
}
package com.acrel.config.redis;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RedisSentinelProperties {

    /**
     * 哨兵master名称
     */
    private String master;

    /**
     * 哨兵节点
     */
    private String nodes;

}
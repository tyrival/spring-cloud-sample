package com.acrel.config.redis;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @des redis 池配置
 * @author ZhouChenyu
 **/
@Data
@ToString
public class RedissonProperties {

    private int maxIdle;

    private int minIdle;

    private int maxActive;

    private int maxWait;

    private int connTimeout;

    private int soTimeout;

    /**
     * 池大小
     */
    private  int size;

    /**
     * 集群状态扫描间隔时间，单位是毫秒
     */
    private int scanInterval;

    /**
     * 默认值： SLAVE（只在从服务节点里读取）设置读取操作选择节点的模式。 可用值为： SLAVE - 只在从服务节点里读取。
     * MASTER - 只在主服务节点里读取。 MASTER_SLAVE - 在主从服务节点里都可以读取
     */
    private String readMode;
    /**
     * （从节点连接池大小） 默认值：64
     */
    private int slaveConnectionPoolSize;
    /**
     * 主节点连接池大小）默认值：64
     */
    private int masterConnectionPoolSize;

    /**
     * （命令失败重试次数） 默认值：3
     */
    private int retryAttempts;

    /**
     *命令重试发送时间间隔，单位：毫秒 默认值：1500
     */
    private int retryInterval;

    /**
     * 执行失败最大次数默认值：3
     */
    private int failedAttempts;

    /* 哨兵配置 */
    private boolean masterOnlyWrite;

    private int failMax;
}
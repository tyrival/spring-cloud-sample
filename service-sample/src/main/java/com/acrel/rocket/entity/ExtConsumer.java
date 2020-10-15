package com.acrel.rocket.entity;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 另一个外部rocketmq集群的消费者
 * @RocketMQMessageListener
 * - topic 必填
 * - consumerGroup 必填需要声明
 * - nameServer 必填，从配置文件读取
 */
@Service
@RocketMQMessageListener(
        nameServer = "${rocketmq.anotherRMQ}", topic = "${rocketmq.consumer.topic}", consumerGroup = "acrel_ext_consumer")
public class ExtConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("acrel_ext_consumer 收到消息：" + message);
    }
}

package com.acrel.rocket.entity;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 带tag的消费者
 * @RocketMQMessageListener
 * - topic 必填
 * - consumerGroup 必填需要声明
 * - selectorExpression tag的匹配规则，不填则默认为*
 */
@Service
@RocketMQMessageListener(
        topic = "${rocketmq.consumer.topic}", selectorExpression = "test1", consumerGroup = "acrel_tag_consumer")
public class TagConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("acrel_tag_consumer 收到消息：" + message);
    }
}
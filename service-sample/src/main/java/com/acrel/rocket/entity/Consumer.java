package com.acrel.rocket.entity;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 普通的消费者
 * @RocketMQMessageListener
 * - topic 必填
 * - consumerGroup 必填
 */
@Service
@RocketMQMessageListener(topic = "${rocketmq.consumer.topic}", consumerGroup = "acrel_consumer")
public class Consumer implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        System.out.println("acrel_consumer 收到消息：" + new String(messageExt.getBody()));
    }
}
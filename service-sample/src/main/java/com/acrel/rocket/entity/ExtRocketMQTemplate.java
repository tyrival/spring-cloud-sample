package com.acrel.rocket.entity;


import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

/**
 * 对接外部RocketMQ集群使用的Template
 */
@ExtRocketMQTemplateConfiguration(nameServer = "${rocketmq.anotherRMQ}")
public class ExtRocketMQTemplate extends RocketMQTemplate {

}

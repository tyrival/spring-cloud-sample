package com.acrel.rocket.controller;

import com.acrel.api.ControllerName;
import com.acrel.rocket.entity.ExtRocketMQTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(ControllerName.SAMPLE_ROCKET)
public class ProducerController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.producer.topic}")
    private String topic;

    @Resource(name = "extRocketMQTemplate")
    private ExtRocketMQTemplate extRocketMQTemplate;

    /**
     * 发送同步消息，收到反馈结果以后才会结束，有消息发送的结果；
     */
    @RequestMapping("/send_sync")
    public void sendSync() {
        // 第一个参数是 topic 或 topic:tag
        // 第二个参数是消息内容
        SendResult sendResult = rocketMQTemplate.syncSend(topic, "Hello, I am sync message!");
        log.info("同步消息的结果：" + sendResult);
    }

    /**
     * 发送异步消息，发送即结束，但是有消息发送结果的回调；
     */
    @RequestMapping("/send_async")
    public void sendAsync() {
        // 第一个参数是 topic 或 topic:tag
        // 第二个参数是消息内容
        // 第三个参数是异步消息发送结果的回调
        rocketMQTemplate.asyncSend(topic, "Hello, I am async message!", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("异步消息发送成功，发送结果：" + sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.info("异步消息发送失败，消息回调");
            }
        });
    }

    /**
     * 发送oneway消息，只管发送，不管发送的结果如何，不返回结果；
     */
    @RequestMapping("/send_oneway")
    public void sendOneway() {
        rocketMQTemplate.sendOneWay(topic, "Hello, I am oneway message!");
    }

    /**
     * 发送带有tag的消息，只能有一个tag
     */
    @RequestMapping("/send_tag")
    public void sendTag() {
        // syncSend发送带tag的消息，有消息发送的结果。
        SendResult tagResult1 = rocketMQTemplate.syncSend(topic + ":test1", "Hello, I am tags:test1 message!");
        log.info("带有tag:test1的消息发送结果：" + tagResult1);

        // convertAndSend发送带tag的消息，没有消息发送的结果
        rocketMQTemplate.convertAndSend(topic + ":test2", "Hello, I am tags:test2 message!");
    }

    /**
     * 发送消息到另外一个集群
     */
    @RequestMapping("/send_another")
    public void sendAnotherRmq() {
        SendResult result = extRocketMQTemplate.syncSend(topic, "Hello, another rocketmq!");
        log.info("非标消息发送的结果：" + result);
    }
}

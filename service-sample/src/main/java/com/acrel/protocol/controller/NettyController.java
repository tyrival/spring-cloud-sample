package com.acrel.protocol.controller;

import com.acrel.api.ControllerName;
import com.acrel.protocol.entity.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: Netty示例
 * @Author: ZhouChenyu
 */
@RestController
@RequestMapping(ControllerName.SAMPLE_NETTY)
public class NettyController {

    @Autowired
    private NettyClient client;

    @RequestMapping("/send")
    public String send() {
        client.sendMsg("{\"id\": 1}|");
        return "success";
    }
}

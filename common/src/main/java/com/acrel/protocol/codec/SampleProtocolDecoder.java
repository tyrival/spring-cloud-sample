package com.acrel.protocol.codec;

import com.acrel.entity.auth.User;
import com.acrel.protocol.ProtocolDecoder;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SampleProtocolDecoder implements ProtocolDecoder {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void decode(ChannelHandlerContext ctx, Object msg, List list) {
        log.info("接收到消息：{}", msg + " " + this.toString());
        User user = JSON.parseObject((String) msg, User.class);
        list.add(user);
    }

}

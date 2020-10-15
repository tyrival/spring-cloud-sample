package com.acrel.protocol.codec;

import com.acrel.protocol.ProtocolEncoder;
import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SampleProtocolEncoder implements ProtocolEncoder {

    @Override
    public void encode(ChannelHandlerContext ctx, Object msg, List list) {
        String res = JSON.toJSONString(msg);
        list.add(res);
    }
}

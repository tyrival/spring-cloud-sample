package com.acrel.protocol;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public interface ProtocolEncoder {

    void encode(ChannelHandlerContext ctx, Object msg, List list);

}

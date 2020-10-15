package com.acrel.protocol;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public interface ProtocolDecoder {

    void decode(ChannelHandlerContext ctx, Object msg, List list);

}

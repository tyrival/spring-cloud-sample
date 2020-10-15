package com.acrel.protocol.entity.dynamic;

import com.acrel.protocol.ProtocolDecoder;
import com.acrel.protocol.ProtocolFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 动态协议编码器
 */
public class DynamicProtocolEncode extends MessageToMessageEncoder {

    private ProtocolDecoder decoder;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List list) throws Exception {
        if (decoder == null) {
            this.decoder = ProtocolFactory.parseDecoder(msg);
        }
        this.decoder.decode(ctx, msg, list);
    }
}

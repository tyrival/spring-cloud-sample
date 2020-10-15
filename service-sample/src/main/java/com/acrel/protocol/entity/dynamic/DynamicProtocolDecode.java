package com.acrel.protocol.entity.dynamic;

import com.acrel.protocol.ProtocolDecoder;
import com.acrel.protocol.ProtocolFactory;
import io.netty.channel.*;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 动态协议解码器
 */
@Slf4j
public class DynamicProtocolDecode extends MessageToMessageDecoder {

    private ProtocolDecoder decoder;

    private int readIdleTimes = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List list) throws Exception {
        if (decoder == null) {
            this.decoder = ProtocolFactory.parseDecoder(msg);
        }
        this.decoder.decode(ctx, msg, list);
    }

    /**
     * 由于此解码器紧跟在IdleStateHandler之后，所以需要实现userEventTriggered方法，以处理心跳超时
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        IdleStateEvent event = (IdleStateEvent) evt;
        String eventType = null;
        switch (event.state()) {
            case READER_IDLE:
                eventType = "读空闲";
                readIdleTimes++; // 读空闲的计数加1
                break;
            case WRITER_IDLE:
                eventType = "写空闲";
                // 不处理
                break;
            case ALL_IDLE:
                eventType = "读写空闲";
                // 不处理
                break;
        }

        log.info(ctx.channel().remoteAddress() + "超时事件：" + eventType);
        if (readIdleTimes > 5) {
            System.out.println(" [server]读空闲超过5次，关闭连接，释放更多资源");
            ctx.channel().writeAndFlush("idle close");
            ctx.channel().close();
        }
    }

}

package com.acrel.protocol.entity;

import com.acrel.protocol.entity.dynamic.DynamicProtocolDecode;
import com.acrel.protocol.entity.dynamic.DynamicProtocolEncode;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 分隔符
     */
    private final static ByteBuf SEPARATOR = Unpooled.copiedBuffer("|".getBytes());

    /**
     * 在读到分隔符之前，解码器读取的最大长度，避免错误的信息
     */
    private final static int MAX_LENGTH = 10240;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 基于分隔符的解码器，将数据流进行拆包
        pipeline.addLast(new DelimiterBasedFrameDecoder(MAX_LENGTH, SEPARATOR));
        // 拆包后的数据流转为字符串
        pipeline.addLast(new StringDecoder());
        // IdleStateHandler的readerIdleTime参数指定超过5秒还没收到客户端的连接，
        // 会触发IdleStateEvent事件并且交给下一个handler处理，下一个handler必须
        // 实现userEventTriggered方法处理对应事件
        pipeline.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
        // 动态协议编解码器，会根据数据格式，选择对应的协议编解码器
        pipeline.addLast(new DynamicProtocolDecode());
        pipeline.addLast(new DynamicProtocolEncode());
        // 添加字符串转流数据的编码器
        pipeline.addLast(new StringEncoder());
    }
}
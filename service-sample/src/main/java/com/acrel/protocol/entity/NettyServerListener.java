package com.acrel.protocol.entity;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerListener implements ChannelFutureListener {

    private ChannelFuture future;

    public NettyServerListener(ChannelFuture future) {
        this.future = future;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (future.isSuccess()) {
            log.info("监听端口成功");
        } else {
            log.info("监听端口失败");
        }
    }
}

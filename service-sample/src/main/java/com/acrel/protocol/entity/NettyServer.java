package com.acrel.protocol.entity;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
@Slf4j
public class NettyServer {

    /**
     * bossGroup只是处理连接请求,真正的和客户端业务处理，会交给workerGroup完成，
     * 含有的子线程NioEventLoop的个数默认为cpu核数的两倍
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    /**
     * work 线程组用于数据处理
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    @Value("${netty.port}")
    private Integer port;

    /**
     * 启动Netty Server
     *
     * @throws InterruptedException
     */
    @PostConstruct
    public void start() throws InterruptedException {
        //创建服务器端的启动对象
        ServerBootstrap bootstrap = new ServerBootstrap();
        //使用链式编程来配置参数
        bootstrap.group(bossGroup, workerGroup)
                //使用NioServerSocketChannel作为服务器的通道实现
                .channel(NioServerSocketChannel.class)
                //使用指定的端口设置套接字地址
                .localAddress(new InetSocketAddress(port))
                // 服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                // 服务端处理客户端连接请求是顺序处理的,所以同一时间只能处理一个客户端连接。
                // 多个客户端同时来的时候,服务端将不能处理的客户端连接请求放在队列中等待处理
                .option(ChannelOption.SO_BACKLOG, 1024)
                //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //将小的数据包包装成更大的帧进行传送，提高网络的负载
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ServerChannelInitializer());
        // 生成了一个ChannelFuture异步对象，通过isDone()等方法可以判断异步事件的执行情况
        // 启动服务器，bind是异步操作，sync方法是等待异步操作执行完毕
        ChannelFuture future = bootstrap.bind().sync();
        future.addListener(new NettyServerListener(future));
        channel = future.channel();
        if (future.isSuccess()) {
            log.info("启动 NettyServer");
        }
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        log.info("关闭 NettyServer...");
        if (channel != null) {
            // 对通道关闭进行监听，closeFuture是异步操作，监听通道关闭
            // 通过sync方法同步等待通道关闭处理完毕，这里会阻塞等待通道关闭完成
            channel.closeFuture().sync();
        }
        bossGroup.shutdownGracefully().sync();
        workerGroup.shutdownGracefully().sync();
        log.info("成功关闭 NettyServer。");
    }
}
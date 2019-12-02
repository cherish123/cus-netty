package com.cus.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 实现客户端发送一个请求，服务器会返回hello netty
 */
public class HelloServer {
    
    public static void main(String args[]) throws Exception {
        //构建一对主线程组
        //构建主线程组，只负责客户端的连接，不干活
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //从线程组，老板线程组会把任务丢给他，让手下线程组去做任务
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //定义netty服务启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)          //设置主从线程组
                    .channel(NioServerSocketChannel.class) //设置nio的双向通道
                    .childHandler(new HelloServerInitializer());  //字处理器，用于处理workerGroup

            //启动server,并且设置8088为启动的端口号，同时启动方式为同步
            ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();

            //监听关闭的channel，设置为同步方式
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
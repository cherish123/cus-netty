package com.cus.netty.webSocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame)
            throws Exception {
        //获取客户端传来的消息
        String content = textWebSocketFrame.text();
        System.out.println("接收到的数据： "+content);

        for (Channel channel:clients) {
            channel.writeAndFlush(new TextWebSocketFrame("【服务器在】" + LocalDateTime.now()+"接收到消息： " + content));
        }

        //与for循环等价
//        clients.writeAndFlush(new TextWebSocketFrame("【服务器在】" + LocalDateTime.now()+"接收到消息： " + content));

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    }
}

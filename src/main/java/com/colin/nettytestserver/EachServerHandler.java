package com.colin.nettytestserver;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * Sharable表示此对象在channel间共享
 * handler类是我们的具体业务类
 * <p>
 * 要想处理接收到的数据，我们必须继承ChannelInboundHandlerAdapter接口，
 * 重写里面的MessageReceive方法，每当有数据到达，此方法就会被调用（一般是Byte类型数组），我们就在这里写我们的业务逻辑：
 */
@ChannelHandler.Sharable//注解@Sharable可以让它在channels间共享
public class EachServerHandler extends ChannelInboundHandlerAdapter {

    //服务端收到消息
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("服务器收到了客户端的消息 : " + msg);
        ctx.write(msg);//写回数据，
    }

    //当服务端将收到的消息读取完毕后
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER) //flush掉所有写回的数据
                .addListener(ChannelFutureListener.CLOSE); //当flush完成后关闭channel
    }


    //发生异常
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();//捕捉异常信息
        ctx.close();//出现异常时关闭channel
    }
}

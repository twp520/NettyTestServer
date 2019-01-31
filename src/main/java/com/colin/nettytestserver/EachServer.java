package com.colin.nettytestserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * 1. 创建一个ServerBootstrap实例
 *      2. 创建一个EventLoopGroup来处理各种事件，如处理链接请求，发送接收数据等。
 *      3. 定义本地InetSocketAddress( port)好让Server绑定
 *      4. 创建childHandler来处理每一个链接请求   
 *      5. 所有准备好之后调用ServerBootstrap.bind()方法绑定Server
 */
public class EachServer {
    private static final int port = 8050;

    public void start() throws InterruptedException {
        ServerBootstrap b = new ServerBootstrap();// 引导辅助程序
        EventLoopGroup group = new NioEventLoopGroup();// 通过nio方式来接收连接和处理连接
        try {
            b.group(group)
                    .channel(NioServerSocketChannel.class)// 设置nio类型的channel
                    .localAddress(new InetSocketAddress(port))// 设置监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {//有连接到达时会创建一个channel
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // pipeline管理channel中的Handler，在channel队列中添加一个handler来处理业务
                            ch.pipeline().addLast("stringEncoder", new StringEncoder())
                                    .addLast("stringDecoder", new StringDecoder())
                                    .addLast("myHandler", new EachServerHandler());
                        }
                    });
            ChannelFuture f = b.bind().sync();// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            System.out.println(EachServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();// 应用程序会一直等待，直到channel关闭
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();//关闭EventLoopGroup，释放掉所有资源包括创建的线程
        }
    }


}

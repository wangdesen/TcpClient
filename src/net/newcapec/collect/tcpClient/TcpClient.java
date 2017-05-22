package net.newcapec.collect.tcpClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * TCP客户端
 *
 * @author 陈飞飞    2016年10月19日 上午11:17:08
 */
public class TcpClient implements Runnable {
    private ChannelHandler[] ctx = null;

    private boolean connFlag = false;
    private ChannelFuture cFuture = null;

    /**
     * 构造函数
     * @param ctx 消息处理类
     */
    public TcpClient(ChannelHandler[] ctx) {
        this.ctx = ctx;
    }

    /**
     * 连接银联服务器
     */
    public void connect() {
        try {
            Thread upThread = new Thread(this);
            upThread.setDaemon(true);
            upThread.setName("TCP线程");
            upThread.start();

            while (!connFlag) {
                Thread.sleep(100 * 2);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭银联TCP连接
     */
    public void close() {
        if (null != cFuture) {
            cFuture.channel().close();
            cFuture = null;
        }
    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group) // 注册线程池
                    .option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000).channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
                    .remoteAddress(new InetSocketAddress(TcpParam.IP, TcpParam.port)) // 绑定连接端口和host信息
                    .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                        @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new MessageDecoder(1024 * 2, 0, 4, -4, 0));
                                ch.pipeline().addLast(new MessageEncoder());
                            if (null != ctx) {
                                ch.pipeline().addLast(ctx);
                            }
                        }
                    });

            cFuture = b.connect(TcpParam.IP, TcpParam.port).sync(); // 异步连接服务器
            connFlag = true;
            Log.getInstance().write(String.format("连接服务器%s:%d成功",TcpParam.IP,TcpParam.port));
            cFuture.channel().closeFuture().sync(); // 异步等待关闭连接channel
        } catch (InterruptedException e) {
            Log.getInstance().write(String.format("连接服务器%s:%d 异常:%s",TcpParam.IP,TcpParam.port, MyException.getStackTrace(e)));
            connFlag = true;
        } finally {
            group.shutdownGracefully(); // 释放线程池资源
        }
    }
}

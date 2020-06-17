package com.zeaho.TCP.domain.service.impl;

import com.zeaho.TCP.domain.model.OpenApiShhkMachine;
import com.zeaho.TCP.domain.service.TCPService;
import com.zeaho.TCP.handler.EchoHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.stereotype.Service;

@Service
public class TCPServiceImpl implements TCPService {

    @Override
    public void init(String ip, int port, String machineCode,Long machineId) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.
                    group(group).
                    channel(NioSocketChannel.class).
                    remoteAddress(ip, port)
                    .handler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast("logging", new LoggingHandler("DEBUG"));
                            ch.pipeline().addLast(new EchoHandler(machineCode,machineId));
                        }
                    });

            ChannelFuture future = bootstrap.connect().sync();
            //future.channel().closeFuture().sync();阻塞
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}

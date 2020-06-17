package com.zeaho.TCP.handler;

import com.zeaho.TCP.utils.BBC;
import com.zeaho.TCP.utils.JointBytes;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;

public class EchoHandler extends ChannelInboundHandlerAdapter {

    //连接成功后发送消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        JointBytes jointBytes = new JointBytes();
        ArrayList<Byte> bytes = jointBytes.JointBytes();

        //结果转换
        int size = bytes.size();
        byte[] by = new byte[size + 1];//多一位用于存储BBC
        byte[] xors = new byte[size - 2];//去除前两位计算BCC

        for (int i = 0; i < size; i++) {
            if (i > 1) {
                xors[i - 2] = bytes.get(i);
            }
            by[i] = bytes.get(i);
        }

        //校验码
        byte xor = BBC.getXor(xors);
        by[size] = xor;

        ctx.writeAndFlush(Unpooled.copiedBuffer(by));
    }
}

package com.zeaho.TCP.handler;

import com.zeaho.TCP.domain.protocol.TCPBytes;
import com.zeaho.TCP.utils.BBCUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;

public class EchoHandler extends ChannelInboundHandlerAdapter {

    private String machineCode;
    private Long machineId;

    public EchoHandler(String machineCode, Long machineId) {
        this.machineCode = machineCode;
        this.machineId = machineId;
    }

    //连接成功后发送消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        TCPBytes tcpBytes = new TCPBytes();
        ArrayList<Byte> bytes = tcpBytes.JointBytes(machineCode, machineId);

        if (bytes != null) {
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
            byte xor = BBCUtil.getXor(xors);
            by[size] = xor;
            ctx.writeAndFlush(Unpooled.copiedBuffer(by));
        }
    }
}

package com.zeaho.TCP.handler;

import com.zeaho.TCP.domain.model.OpenApiShhkMachine;
import com.zeaho.TCP.domain.repo.OpenApiShhkMachineRepo;
import com.zeaho.TCP.utils.BBC;
import com.zeaho.TCP.utils.StringIntoBytes;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class EchoHandler extends ChannelInboundHandlerAdapter {


    @Autowired
    private OpenApiShhkMachineRepo openApiShhkMachineRepo;

    //连接成功后发送消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

this.getBytes();

        ArrayList<Byte> bytes = new ArrayList<>();

        //起始符
        String header = "##";
        StringIntoBytes.intoBytes(bytes, header);

        //命令单元
        bytes.add((byte) 0x02);

        //车辆识别号
        //String headingCode = "HCM1HH00L00036634";
        String headingCode = "0000SY005EBK85008";
        StringIntoBytes.intoBytes(bytes, headingCode);

        //终端软件版本号
        bytes.add((byte) '3');

        //数据加密方式
        bytes.add((byte) 0x01);


        /**----------------------拼接数据----------------------*/
        ArrayList<Byte> addBytes = new ArrayList<>();
        //数据采集时间
        addBytes.add((byte) 20);//年
        addBytes.add((byte) 6);//月
        addBytes.add((byte) 3);//日
        addBytes.add((byte) 12);//时
        addBytes.add((byte) 0);//分
        addBytes.add((byte) 0);//秒
        //信息类型标志
        addBytes.add((byte) 0x87);//国三及以下数据流
        //信息流水号
        addBytes.add((byte) 0);
        addBytes.add((byte) 1);
        /**------------信息体------------*/
        //油箱液位
        addBytes.add((byte) 100);
        //定位状态
        addBytes.add((byte) 0);
        //精度
        addBytes.add((byte) 0x42);
        addBytes.add((byte) 0xF3);
        addBytes.add((byte) 0x0C);
        addBytes.add((byte) 0x4A);
        //纬度
        addBytes.add((byte) 0x41);
        addBytes.add((byte) 0xF7);
        addBytes.add((byte) 0xfb);
        addBytes.add((byte) 0x0B);
        //工作状态
        addBytes.add((byte) 0x02);
        /**------------信息体------------*/
        /**----------------------拼接数据----------------------*/

        //计算数据长度
        int addBytesSize = addBytes.size();

        //追加数据长度
        bytes.add((byte) 0);
        bytes.add((byte) addBytesSize);

        //追加数据
        for (byte b : addBytes) {
            bytes.add(b);
        }


        //结果转换
        int size = bytes.size();
        byte[] by = new byte[size + 1];
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

    private void getBytes() {
        List<OpenApiShhkMachine> list = openApiShhkMachineRepo.findAll();
        ArrayList<Byte> bytes = new ArrayList<>();//
        for (OpenApiShhkMachine oasm : list) {
            String machineCode = oasm.getMachineCode();
            if (!"".equals(machineCode) && machineCode != null) {//设备编号不为空
                System.out.println(machineCode);
                //拼接数据包,一次传输
               // byte[] bytes = bytesService.getBytes(oasm);

            }

        }
    }
}

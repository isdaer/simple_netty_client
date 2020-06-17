package com.zeaho.TCP.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

//返回ArrayList<Byte>类型的最终传入数据
@Component
public class JointBytes {
    public static ArrayList<Byte> JointBytes() {
        ArrayList<Byte> bytes = new ArrayList<>();
        //起始符
        String header = "##";
        StringIntoBytes.intoBytes(bytes, header);

        //命令单元
        bytes.add((byte) 0x02);
        return bytes;

    }
}

package com.zeaho.utils;

//BBC计算
public class BBC {
    public static byte getXor(byte[] datas) {

        byte temp = datas[0];
        for (int i = 1; i < datas.length; i++) {
            temp ^= datas[i];
        }
        return temp;
    }
}

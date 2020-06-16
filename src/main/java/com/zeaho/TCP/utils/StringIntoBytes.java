package com.zeaho.TCP.utils;

import java.util.ArrayList;

//将传入的字符串转化为字节追加进传入的字节数组
public class StringIntoBytes {
    public static void intoBytes(ArrayList<Byte> bytes, String s) {
        char[] chars = s.toCharArray();
        for (char c : chars) {
            bytes.add((byte) c);
        }
    }
}

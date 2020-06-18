package com.zeaho.TCP.utils;

import java.util.ArrayList;

/**
 * @author chen
 */
//将传入的字符串转化为字节追加进传入的字节数组
public class BytesUtil {
    public static void stringIntoBytes(ArrayList<Byte> bytes, String s) {
        char[] chars = s.toCharArray();
        for (char c : chars) {
            bytes.add((byte) c);
        }
    }

    /**
     * 将int类型数据转换为指定个数的字节数组
     *
     * @param number:指定原数据
     * @param count:指定字节数
     * @return
     */
    public static int[] int2bytes(int number, int count) {
        String s = Integer.toBinaryString(number);//转换成二进制字符串
        int originLength = s.length();
        int resultLength = count * 8;
        String cs = "";
        if (originLength < resultLength) {//位数不够左补0
            for (int i = 0; i < resultLength - originLength; i++) {
                cs += "0";
            }
            s = cs + s;
        }

        String[] strings = splitStringByCount(s, 8);//返回分割好的二进制字符串数组
        int[] ints = new int[count];

        for (int i = 0; i < strings.length; i++) {
            ints[i] = Integer.valueOf(strings[i], 2);//转换回十进制
        }
        return ints;
    }

    /**
     * 将指定字符串按指定大小分割
     *
     * @param str:分割字符串
     * @param count:分割大小
     * @return
     */
    public static String[] splitStringByCount(String str, int count) {

        int length = str.length();
        int splitCount;//分割数

        if (length >= count) {//字符串长度大于指定位数
            char[] chars = str.toCharArray();
            if (length % count == 0) {//整除
                splitCount = length / count;
            } else {
                splitCount = length / count + 1;
            }

            String[] strings = new String[splitCount];

            String s = "";
            for (int i = 1; i <= length; i++) {
                s += chars[i - 1];
                if (i % count == 0) {//达到字符数
                    strings[(i / count) - 1] = s;
                    s = "";
                }
            }
            return strings;
        } else {
            String[] strings = {str};
            return strings;
        }
    }

}

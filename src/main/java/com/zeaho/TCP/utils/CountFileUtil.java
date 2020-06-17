package com.zeaho.TCP.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CountFileUtil {
    public static void updateCountFile() {
        //创建txt记录流水号,定时任务12点删除
        try {
            File file = new File("count.txt");
            if (!file.exists()) {//文件不存在,当天第一次,默认为1
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write("1".getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } else {
                //不是第一次,读取文件中的计数
                List<String> strings = Files.readAllLines(Paths.get(file.toURI()));
                String strCount = strings.get(0);
                int count = Integer.parseInt(strCount);
                count = count + 1;
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(("" + count).getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.zeaho.TCP.schedule;

import com.zeaho.TCP.domain.service.TCPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@EnableScheduling  //定时任务
@RestController
public class TCPInit {

    @Value("${ip}")
    private String ip;

    @Value("${port}")
    private int port;

    @Autowired
    private TCPService tcpService;

    @Scheduled(fixedRate = 1000)//30秒
    public void init() {
        try {

            File file = new File("count.txt");
            if (!file.exists()) {
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
                System.out.println(count);
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


//        System.out.println("定时任务执行");
//        try
//
//    {
//        tcpService.init(ip, port);
//    } catch(
//    InterruptedException e)
//
//    {
//        e.printStackTrace();
//    }
}


package com.zeaho.TCP.schedule;

import com.zeaho.TCP.service.TCPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@EnableScheduling  //定时任务
public class TCPInit {

    @Value("${ip}")
    static private String ip;

    @Value("${port}")
    static private int port;

    @Autowired
    private TCPService tcpService;

    @Scheduled(fixedRate = 30000)
    public void init() {
        System.out.println("定时任务执行");
        try {
            tcpService.init(ip, port);

            tcpService.init(ip, port);
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            System.out.println(df2.format(new Date()));// new Date()为获取当前系统时间
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

package com.zeaho.TCP.schedule;

import com.zeaho.TCP.service.TCPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@EnableScheduling  //定时任务
@RestController
public class TCPInit {

    @Value("${ip}")
    private String ip;

    @Value("${port}")
    private int port;

    @Autowired
    private TCPService tcpService;

    @Scheduled(fixedRate = 5000)
    public void init() {
        System.out.println("定时任务执行");
        try {
            long l1 = System.currentTimeMillis();
            tcpService.init(ip, port);
            long l2 = System.currentTimeMillis();
            System.out.println("耗时:" + (l2 - l1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

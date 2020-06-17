package com.zeaho.TCP.schedule;

import com.zeaho.TCP.domain.service.TCPService;
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

    @Scheduled(fixedRate = 30000)//30秒
    public void init() {
        System.out.println("定时任务执行");
        try {
            tcpService.init(ip, port);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

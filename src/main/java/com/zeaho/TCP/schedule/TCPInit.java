package com.zeaho.TCP.schedule;

import com.zeaho.TCP.domain.model.MachineDataRealTime;
import com.zeaho.TCP.domain.repo.MachineDataRealTimeRepo;
import com.zeaho.TCP.domain.service.TCPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@EnableScheduling  //定时任务
@RestController
public class TCPInit {

    @Value("${ip}")
    private String ip;

    @Value("${port}")
    private int port;


    @Autowired
    private TCPService tcpService;

    @Autowired
    private MachineDataRealTimeRepo machineDataRealTimeRepo;


    @Scheduled(fixedRate = 30000)//30秒
    public void init() {
        MachineDataRealTime byMachineId = machineDataRealTimeRepo.findByMachineId(1l);
        System.out.println(byMachineId);


        System.out.println("定时任务执行" );
//        try {
//            long l1 = System.currentTimeMillis();
//            tcpService.init(ip, port);
//            long l2 = System.currentTimeMillis();
//            System.out.println("耗时:" + (l2 - l1));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }
}

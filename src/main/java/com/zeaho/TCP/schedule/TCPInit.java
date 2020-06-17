package com.zeaho.TCP.schedule;

import com.zeaho.TCP.domain.model.OpenApiShhkMachine;
import com.zeaho.TCP.domain.repo.OpenApiShhkMachineRepo;
import com.zeaho.TCP.service.TCPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableScheduling  //定时任务
@RestController
public class TCPInit {

    @Value("${ip}")
    private String ip;

    @Value("${port}")
    private int port;

    @Autowired
    private OpenApiShhkMachineRepo openApiShhkMachineRepo;

    @Autowired
    private TCPService tcpService;

    @Scheduled(fixedRate = 30000)//30秒
    public void init() {
        List<OpenApiShhkMachine> list = openApiShhkMachineRepo.findAll();
        for (OpenApiShhkMachine oasm : list) {
            String machineCode = oasm.getMachineCode();
            if (!"".equals(machineCode) && machineCode != null) {//设备编号不为空
                //拼接数据包,一次传输

            }
            System.out.println("定时任务执行,设备编号为:" + machineCode);
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
}

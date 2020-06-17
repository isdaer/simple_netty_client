package com.zeaho.TCP.schedule;

import com.zeaho.TCP.domain.model.OpenApiShhkMachine;
import com.zeaho.TCP.domain.repo.OpenApiShhkMachineRepo;
import com.zeaho.TCP.domain.service.TCPService;
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
    private TCPService tcpService;

    @Autowired
    private OpenApiShhkMachineRepo openApiShhkMachineRepo;

    @Scheduled(fixedRate = 30000)//30秒
    public void init() {
        System.out.println("定时任务执行");
        List<OpenApiShhkMachine> list = openApiShhkMachineRepo.findAll();
        for (OpenApiShhkMachine oasm : list) {
            String machineCode = oasm.getMachineCode();
            Long machineId = oasm.getMachineId();

            if (!"".equals(machineCode) && machineCode != null && !"".equals(machineId) && machineId != null) {//设备编号和机械id不为空
                try {
                    tcpService.init(ip, port, machineCode, machineId);
                } catch (
                        InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


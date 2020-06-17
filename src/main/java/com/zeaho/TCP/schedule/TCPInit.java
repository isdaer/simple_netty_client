package com.zeaho.TCP.schedule;

import com.zeaho.TCP.domain.model.OpenApiShhkMachine;
import com.zeaho.TCP.domain.repo.OpenApiShhkMachineRepo;
import com.zeaho.TCP.domain.service.TCPService;
import com.zeaho.TCP.utils.JointBytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
                    tcpService.init(ip, port, machineCode,machineId);
                } catch (
                        InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


//        //创建txt记录流水号,定时任务12点删除
//        try {
//            File file = new File("count.txt");
//            if (!file.exists()) {//文件不存在,当天第一次,默认为1
//                file.createNewFile();
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
//                fileOutputStream.write("1".getBytes());
//                fileOutputStream.flush();
//                fileOutputStream.close();
//            } else {
//                //不是第一次,读取文件中的计数
//                List<String> strings = Files.readAllLines(Paths.get(file.toURI()));
//                String strCount = strings.get(0);
//                int count = Integer.parseInt(strCount);
//                count = count + 1;
//                FileOutputStream fileOutputStream = new FileOutputStream(file);
//                fileOutputStream.write(("" + count).getBytes());
//                fileOutputStream.flush();
//                fileOutputStream.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }
}


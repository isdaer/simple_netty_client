package com.zeaho.TCP.utils;

import com.zeaho.TCP.domain.model.OpenApiShhkMachine;
import com.zeaho.TCP.domain.repo.OpenApiShhkMachineRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

//返回ArrayList<Byte>类型的最终传入数据
//工具类调用repo
@Component
public class JointBytes {

    @Autowired
    private OpenApiShhkMachineRepo openApiShhkMachineRepo;

    private static JointBytes jointBytes;

    @PostConstruct
    public void init() {
        jointBytes = this;
        jointBytes.openApiShhkMachineRepo = this.openApiShhkMachineRepo;
    }

    public ArrayList<Byte> JointBytes() {
        ArrayList<Byte> bytes = new ArrayList<>();
        List<OpenApiShhkMachine> list = jointBytes.openApiShhkMachineRepo.findAll();
        for (OpenApiShhkMachine oasm : list) {
            String machineCode = oasm.getMachineCode();
            if (!"".equals(machineCode) && machineCode != null) {//设备编号不为空
                StringIntoBytes.intoBytes(bytes, machineCode);

            }

        }

        return bytes;
    }
}

package com.zeaho.TCP.domain.service.impl;

import com.zeaho.TCP.domain.model.OpenApiShhkMachine;
import com.zeaho.TCP.domain.service.BytesService;
import org.springframework.stereotype.Service;

@Service
public class BytesServiceImpl implements BytesService {


    @Override
    public byte[] getBytes(OpenApiShhkMachine oasm) {
        Long machineId = oasm.getMachineId();
        if ("".equals(machineId) || machineId == null) {//无法查实时状态
            return null;
        } else {
            return null;
        }

    }
}

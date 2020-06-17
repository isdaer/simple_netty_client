package com.zeaho.TCP.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class MachineDataRealTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long machineId;

    private String deviceSn;
    private String sensorSn;
    private String environmentCode;
    private String emissionStandard;
    private String category;
    private String brand;
    private String machineModel;
    private String machineCode;
    private String ownerName;
    private String ownerPhone;
    private Long deviceId;
    private Long tenantId;
    private Long machineId;

}

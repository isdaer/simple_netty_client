package com.zeaho.TCP.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Data
public class OpenApiShhkMachine extends AbstractModel {

    private Date installAt;
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

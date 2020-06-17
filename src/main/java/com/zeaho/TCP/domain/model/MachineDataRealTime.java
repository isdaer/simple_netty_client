package com.zeaho.TCP.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class MachineDataRealTime {

    @Id
    private Long machineId;

    private String state;

    private float fuelPercentage;//油量百分比

    private int stateUpdateTs;//状态修改时间

}

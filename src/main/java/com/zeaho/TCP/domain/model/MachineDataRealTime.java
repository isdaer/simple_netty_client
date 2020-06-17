package com.zeaho.TCP.domain.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class MachineDataRealTime {

    @Id
    private Long machineId;

    @Enumerated(EnumType.STRING)
    private State state;//状态

    private Float fuelVolume;//油量

    private Long lastLocationId;//最后一次定位


}

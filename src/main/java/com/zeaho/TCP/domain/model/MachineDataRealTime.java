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

    private Float fuelVolume;//油量

    private Long lastLocationId;//最后一次定位

}

package com.zeaho.TCP.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class MachineLastLocation {

    @Id
    private Long machineId;

    private Double longitude;//精度

    private Double latitude;//纬度

}

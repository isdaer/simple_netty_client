package com.zeaho.TCP.domain.repo;

import com.zeaho.TCP.domain.model.MachineLastLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MachineLastLocationRepo extends JpaRepository<MachineLastLocation, Long>, JpaSpecificationExecutor<MachineLastLocation> {
    MachineLastLocation findByMachineId(Long machineId);
}

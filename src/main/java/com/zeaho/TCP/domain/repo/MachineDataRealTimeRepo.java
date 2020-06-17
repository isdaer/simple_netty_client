package com.zeaho.TCP.domain.repo;

import com.zeaho.TCP.domain.model.MachineDataRealTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MachineDataRealTimeRepo extends JpaRepository<MachineDataRealTime, Long>, JpaSpecificationExecutor<MachineDataRealTime> {
}

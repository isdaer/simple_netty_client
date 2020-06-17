package com.zeaho.TCP.domain.repo;

import com.zeaho.TCP.domain.model.OpenApiShhkMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OpenApiShhkMachineRepo extends JpaRepository<OpenApiShhkMachine, Long>, JpaSpecificationExecutor<OpenApiShhkMachine> {
}

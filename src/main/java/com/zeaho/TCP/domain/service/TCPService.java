package com.zeaho.TCP.domain.service;

public interface TCPService {

    public void init(String ip, int port, String machineCode,Long machineId) throws InterruptedException;
}

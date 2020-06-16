package com.zeaho.TCP.service;

import org.springframework.stereotype.Service;

@Service
public interface TCPService {

    public void init(String ip, int port) throws InterruptedException;
}

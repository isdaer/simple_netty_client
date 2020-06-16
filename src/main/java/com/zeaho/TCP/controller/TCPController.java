package com.zeaho.TCP.controller;

import com.zeaho.TCP.service.TCPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TCPController {

    @Value("${ip}")
    private String ip;

    @Value("${port}")
    private int port;

    @Autowired
    private TCPService tcpService;

    @PostMapping("init")
    public void init() {
        try {
            tcpService.init(ip, port);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

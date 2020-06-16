package com.zeaho.TCP.controller;

import com.zeaho.TCP.service.TCPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TCPController {

    private String ip;
    private int port;

    @Autowired
    private TCPService tcpService;

    public void init() {
        try {
            tcpService.init(ip, port);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

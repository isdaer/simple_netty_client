package com.zeaho.TCP.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TCPController {

    @PostMapping("/tcp/send")
    public String tcpSend() {
        return "111";
    }
}

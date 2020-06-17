package com.zeaho.TCP.domain.model;

//机械工作状态枚举类
public enum State {
    WORKING("运行"),
    OFF("静止"),
    IDLE("怠速"),
    OUT_WATCHING("离线");

    private String state;

    State(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

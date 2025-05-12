package com.monitoring.smmt.sendmetrics;

import java.time.LocalDateTime;

//Pojo class
public class Metric {

    private int id;
    private String ip_address;
    private float cpuUsage;
    private float ramUsage;
    private float freeRam;
    private LocalDateTime timestamp;

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public float getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(float cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public float getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(float ramUsage) {
        this.ramUsage = ramUsage;
    }

    public float getFreeRam() {
        return freeRam;
    }

    public void setFreeRam(float freeRam) {
        this.freeRam = freeRam;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

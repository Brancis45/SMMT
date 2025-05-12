package com.monitoring.smmt.sendmetrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class MetricsController {

    @Autowired
    private MetricRepository metricsRepository;

    //response to GET requests to the endpoint
    @GetMapping("/allLatestMetrics")
    public List<Map<String, Object>> getAllLatestMetrics() {
        List<Metric> records = metricsRepository.getAllLatestMetrics();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Metric record : records) {
            Map<String, Object> machine = new HashMap<>();
            machine.put("ip_address", record.getIp_address());
            machine.put("cpu_usage", record.getCpuUsage());
            machine.put("ram_usage", record.getRamUsage());
            machine.put("free_ram", record.getFreeRam());
            machine.put("recorded_at", record.getTimestamp().toString());
            response.add(machine);
        }
        return response;
    }
}

package com.monitoring.smmt.sendmetrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MetricRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //gets most recent metrics for each IP
    public List<Metric> getAllLatestMetrics() {
        String getSQL = "SELECT t1.ip_address, t1.cpu_usage, t1.ram_usage, t1.free_ram, t1.timestamp " +
                "FROM system_metrics t1 " +
                "INNER JOIN ( " +
                "    SELECT ip_address, MAX(timestamp) AS max_time " +
                "    FROM system_metrics " +
                "    GROUP BY ip_address " +
                ") t2 ON t1.ip_address = t2.ip_address AND t1.timestamp = t2.max_time " +
                "ORDER BY t1.ip_address";
        return jdbcTemplate.query(getSQL, (rs, rowNum) -> {
            Metric record = new Metric();
            record.setIp_address(rs.getString("ip_address"));
            record.setCpuUsage(rs.getFloat("cpu_usage"));
            record.setRamUsage(rs.getFloat("ram_usage"));
            record.setFreeRam(rs.getFloat("free_ram"));
            record.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
            return record;
        });
    }
}

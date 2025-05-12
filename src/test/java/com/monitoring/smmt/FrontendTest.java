package com.monitoring.smmt;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.*;

public class FrontendTest {

    @Test
    void testTimestampFormatting() {
        String iso = "2023-10-15T14:30:45";
        LocalDateTime time = LocalDateTime.parse(iso, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String formatted = formatTimestamp(time);
        assertEquals("14:30:45 2023-10-15", formatted);
    }

    @Test
    void testMachineDisplayFormat() {
        Machine mockMachine = new Machine(
                "192.168.1.1",
                75.5f,
                25.3f,
                30.0f,
                LocalDateTime.now()
        );

        String display = renderMachine(mockMachine);
        assertTrue(display.contains("192.168.1.1"));
        assertTrue(display.contains("75.50%"));
        assertTrue(display.contains("25.30 GB"));
    }

    //mirroring JS logic
    private String formatTimestamp(LocalDateTime timestamp) {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd"));
    }

    private String renderMachine(Machine machine) {
        return String.format("%s - CPU: %.2f%%, RAM: %.2f GB, Free: %.2f%% (%s)",
                machine.ipAddress(),
                machine.cpuUsage(),
                machine.ramUsage(),
                machine.freeRam(),
                formatTimestamp(machine.timestamp())
        );
    }

    //record to mimic the data structure
    record Machine(String ipAddress, float cpuUsage, float ramUsage,
                   float freeRam, LocalDateTime timestamp) {}
}
package com.monitoring.smmt;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

public class DBEntryTest {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/sys";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "12345678";

    @BeforeEach
    void clearDatabase() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM system_metrics")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            fail("Failed to clear database: " + e.getMessage());
        }
    }

    @Test
    public void testDatabaseInsertion() {
        String insertSQL = "INSERT INTO system_metrics (cpu_usage, ram_usage, free_ram, ip_address, timestamp) VALUES (?, ?, ?, ?, ?)";
        String selectSQL = "SELECT cpu_usage, ram_usage, free_ram, ip_address, timestamp FROM system_metrics WHERE ip_address = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {

            // insert test metric
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSQL)) {
                insertStmt.setDouble(1, 20.0);
                insertStmt.setDouble(2, 50.0);
                insertStmt.setDouble(3, 50.0);
                insertStmt.setString(4, "192.168.1.69");
                insertStmt.setString(5, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                int rowsInserted = insertStmt.executeUpdate();
                assertEquals(1, rowsInserted, "1 row should be inserted");
            }

            // retrieve and validate inserted metric
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
                selectStmt.setString(1, "192.168.1.69");
                ResultSet rs = selectStmt.executeQuery();

                assertTrue(rs.next(), "Inserted metric should exist in the database");
                assertEquals(20.0, rs.getDouble("cpu_usage"), "CPU usage should match");
                assertEquals(50.0, rs.getDouble("ram_usage"), "RAM usage should match");
                assertEquals(50.0, rs.getDouble("free_ram"), "Free RAM should match");
                assertEquals("192.168.1.69", rs.getString("ip_address"), "IP address should match");
            }

        } catch (SQLException e) {
            fail("Database test failed: " + e.getMessage());
        }
    }
}

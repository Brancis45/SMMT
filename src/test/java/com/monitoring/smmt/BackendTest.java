package com.monitoring.smmt;

import com.monitoring.smmt.metricsreceiver.ReceiverServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;

public class BackendTest {

    private static final int PORT = 55440;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sys";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345678";
    private static Thread serverThread;

    @BeforeClass
    public static void setUp() throws Exception {
        serverThread = new Thread(() -> ReceiverServer.main(new String[]{}));
        serverThread.setDaemon(true);
        serverThread.start();
        Thread.sleep(2000);
    }

    @Test
    public void testServerReceivesAndStoresData() throws Exception {
        InetAddress address = InetAddress.getByName("localhost");
        String jsonData = "{\"cpu_usage\": 75.5, \"ram_usage\": 25.3, \"free_ram\": 30.0}";
        byte[] buffer = jsonData.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.send(packet);
        }

        Thread.sleep(2000);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM system_metrics WHERE ip_address = '127.0.0.1' ORDER BY timestamp DESC LIMIT 1"
            );

            assertTrue("No data inserted into the database", rs.next());
            assertEquals(75.5f, rs.getFloat("cpu_usage"), 0.01);
            assertEquals(25.3f, rs.getFloat("ram_usage"), 0.01);
            assertEquals(30.0f, rs.getFloat("free_ram"), 0.01);
        }
    }

    @AfterClass
    public static void tearDown() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM system_metrics WHERE ip_address = '127.0.0.1'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.monitoring.smmt.metricsreceiver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

//receiver server for metric data
public class ReceiverServer {
    private static final int PORT = 55440;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sys";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345678";

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Server is listening on port " + PORT);
            ObjectMapper objectMapper = new ObjectMapper();

            while (true) {
                //receive the packet
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                //extract data
                String receivedData = new String(packet.getData(), 0, packet.getLength());
                InetAddress senderAddress = packet.getAddress();
                String senderIP = senderAddress.getHostAddress();
                System.out.println("Received metrics from " + senderIP + ": " + receivedData);

                //parse JSON data
                JsonNode jsonNode = objectMapper.readTree(receivedData);
                float cpuUsage = (float) jsonNode.get("cpu_usage").asDouble();
                float ramUsage = (float) jsonNode.get("ram_usage").asDouble();
                float freeRam = (float) jsonNode.get("free_ram").asDouble();

                //insert into database
                insertMetricsIntoDB(senderIP, cpuUsage, ramUsage, freeRam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertMetricsIntoDB(String ipAddress, float cpuUsage, float ramUsage, float freeRam) {
        String insertSQL = "INSERT INTO system_metrics (ip_address, cpu_usage, ram_usage, free_ram) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(insertSQL)) {
            stmt.setString(1, ipAddress);
            stmt.setFloat(2, cpuUsage);
            stmt.setFloat(3, ramUsage);
            stmt.setFloat(4, freeRam);
            stmt.executeUpdate();
            System.out.println("Metrics saved to database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
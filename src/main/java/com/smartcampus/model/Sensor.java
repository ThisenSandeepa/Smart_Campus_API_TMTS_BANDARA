package com.smartcampus.model;

// Sensor model - represents an IoT sensor on campus
public class Sensor {
    private String id;
    private String type;        // Temperature, Occupancy, CO2 etc
    private String status;      // ACTIVE, MAINTENANCE, OFFLINE
    private double currentValue;
    private String roomId;      // which room this sensor is in

    public Sensor() {}

    public Sensor(String id, String type, String status, double currentValue, String roomId) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.currentValue = currentValue;
        this.roomId = roomId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public synchronized String getStatus() { return status; }
    public synchronized void setStatus(String status) { this.status = status; }

    public synchronized double getCurrentValue() { return currentValue; }
    public synchronized void setCurrentValue(double currentValue) { this.currentValue = currentValue; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
}

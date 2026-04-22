package com.smartcampus.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// Room model class - represents a physical room on campus
public class Room {
    private String id;
    private String name;
    private int capacity;
    private CopyOnWriteArrayList<String> sensorIds = new CopyOnWriteArrayList<>();

    public Room() {
    }

    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    // getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<String> getSensorIds() {
        return sensorIds;
    }

    public void setSensorIds(CopyOnWriteArrayList<String> sensorIds) {
        this.sensorIds = sensorIds;
    }

    // helper method to add sensor id
    public void addSensorId(String sensorId) {
        this.sensorIds.addIfAbsent(sensorId);
    }

    public void removeSensorId(String sensorId) {
        this.sensorIds.remove(sensorId);
    }
}

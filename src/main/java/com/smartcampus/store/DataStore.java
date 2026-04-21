package com.smartcampus.store;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Singleton data store to keep all data in memory
// Using ConcurrentHashMap for thread safety
public class DataStore {

    private static DataStore instance;

    private Map<String, Room> rooms = new ConcurrentHashMap<>();
    private Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    private Map<String, List<SensorReading>> sensorReadings = new ConcurrentHashMap<>();

    private DataStore() {
        // add some sample data
        initSampleData();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void initSampleData() {
        // add rooms
        Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room r2 = new Room("ENG-101", "Engineering Lab A", 30);
        Room r3 = new Room("SCI-205", "Science Lecture Hall", 200);
        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);
        rooms.put(r3.getId(), r3);

        // add sensors
        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        Sensor s2 = new Sensor("CO2-001", "CO2", "ACTIVE", 415.0, "LIB-301");
        Sensor s3 = new Sensor("OCC-001", "Occupancy", "ACTIVE", 12.0, "ENG-101");
        Sensor s4 = new Sensor("TEMP-002", "Temperature", "MAINTENANCE", 0.0, "ENG-101");
        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);
        sensors.put(s3.getId(), s3);
        sensors.put(s4.getId(), s4);

        // link sensors to rooms
        r1.addSensorId("TEMP-001");
        r1.addSensorId("CO2-001");
        r2.addSensorId("OCC-001");
        r2.addSensorId("TEMP-002");

        // create empty reading lists
        sensorReadings.put("TEMP-001", new ArrayList<>());
        sensorReadings.put("CO2-001", new ArrayList<>());
        sensorReadings.put("OCC-001", new ArrayList<>());
        sensorReadings.put("TEMP-002", new ArrayList<>());
    }

    // Room methods
    public Map<String, Room> getRooms() { return rooms; }
    public Room getRoom(String id) { return rooms.get(id); }
    public void addRoom(Room room) { rooms.put(room.getId(), room); }
    public Room removeRoom(String id) { return rooms.remove(id); }
    public boolean roomExists(String id) { return rooms.containsKey(id); }

    // Sensor methods
    public Map<String, Sensor> getSensors() { return sensors; }
    public Sensor getSensor(String id) { return sensors.get(id); }

    public void addSensor(Sensor sensor) {
        sensors.put(sensor.getId(), sensor);
        sensorReadings.putIfAbsent(sensor.getId(), new ArrayList<>());
    }

    public Sensor removeSensor(String id) {
        sensorReadings.remove(id);
        return sensors.remove(id);
    }

    public boolean sensorExists(String id) { return sensors.containsKey(id); }

    // Reading methods
    public List<SensorReading> getReadings(String sensorId) {
        return sensorReadings.getOrDefault(sensorId, new ArrayList<>());
    }

    public void addReading(String sensorId, SensorReading reading) {
        sensorReadings.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);
    }
}

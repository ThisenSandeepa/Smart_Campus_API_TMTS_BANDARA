package com.smartcampus.resource;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// Sub resource for sensor readings
@Produces(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;
    private DataStore dataStore = DataStore.getInstance();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // GET all readings for this sensor
    @GET
    public Response getReadings() {
        List<SensorReading> readings = dataStore.getReadings(sensorId);

        Map<String, Object> result = new HashMap<>();
        result.put("sensorId", sensorId);
        result.put("count", readings.size());
        result.put("readings", readings);
        return Response.ok(result).build();
    }

    // POST new reading - also updates parent sensor currentValue
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading, @Context UriInfo uriInfo) {
        Sensor sensor = dataStore.getSensor(sensorId);

        // block if sensor is in maintenance
        if (sensor != null && "MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                "Sensor '" + sensorId + "' is in MAINTENANCE mode and cannot accept readings"
            );
        }

        // auto generate id and timestamp if not provided
        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }
        if (reading.getTimestamp() <= 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }

        dataStore.addReading(sensorId, reading);

        // update parent sensors current value
        if (sensor != null) {
            sensor.setCurrentValue(reading.getValue());
        }

        URI uri = uriInfo.getAbsolutePathBuilder().path(reading.getId()).build();

        Map<String, Object> result = new HashMap<>();
        result.put("message", "Reading added for sensor '" + sensorId + "'");
        result.put("reading", reading);
        result.put("sensorCurrentValue", sensor != null ? sensor.getCurrentValue() : reading.getValue());
        return Response.created(uri).entity(result).build();
    }
}

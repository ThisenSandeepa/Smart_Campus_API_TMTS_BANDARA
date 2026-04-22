package com.smartcampus.exception;

// thrown when trying to post a reading to a sensor thats in maintenance
public class SensorUnavailableException extends RuntimeException {
    public SensorUnavailableException(String message) {
        super(message);
    }
}

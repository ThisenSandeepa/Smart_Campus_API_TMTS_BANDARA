package com.smartcampus.exception;

// thrown when sensor references a room that doesnt exist
public class LinkedResourceNotFoundException extends RuntimeException {
    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}

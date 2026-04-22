package com.smartcampus.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

// Global catch-all exception mapper - catches any unexpected errors
// Returns 500 without exposing stack trace to the client
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        // If it's a built-in JAX-RS exception (like 404 NotFoundException), preserve its status
        if (exception instanceof WebApplicationException) {
            WebApplicationException wae = (WebApplicationException) exception;
            Response originalResponse = wae.getResponse();
            
            Map<String, Object> err = new HashMap<>();
            err.put("status", originalResponse.getStatus());
            err.put("error", originalResponse.getStatusInfo().getReasonPhrase());
            err.put("message", exception.getMessage());
            
            return Response.status(originalResponse.getStatus())
                    .entity(err)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // log the actual error on the server
        LOGGER.log(Level.SEVERE, "Unexpected error: " + exception.getMessage(), exception);

        Map<String, Object> error = new HashMap<>();
        error.put("status", 500);
        error.put("error", "Internal Server Error");
        error.put("message", "Something went wrong on the server. Please try again later.");

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

package com.smartcampus.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Discovery endpoint - root of the API
@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getApiInfo(@Context UriInfo uriInfo) {
        Map<String, Object> info = new HashMap<>();
        info.put("apiName", "Smart Campus Sensor & Room Management API");
        info.put("version", "1.0");
        info.put("description", "RESTful API for managing rooms and sensors on campus");

        // contact info
        Map<String, String> contact = new HashMap<>();
        contact.put("name", "Smart Campus Admin");
        contact.put("email", "admin@smartcampus.university.edu");
        contact.put("department", "Facilities Management");
        info.put("contact", contact);

        // HATEOAS resource links
        String baseUri = uriInfo.getBaseUri().toString();
        List<Map<String, String>> links = new ArrayList<>();
        
        Map<String, String> selfLink = new HashMap<>();
        selfLink.put("rel", "self");
        selfLink.put("href", baseUri);
        links.add(selfLink);
        
        Map<String, String> roomsLink = new HashMap<>();
        roomsLink.put("rel", "rooms");
        roomsLink.put("href", baseUri + "rooms");
        links.add(roomsLink);
        
        Map<String, String> sensorsLink = new HashMap<>();
        sensorsLink.put("rel", "sensors");
        sensorsLink.put("href", baseUri + "sensors");
        links.add(sensorsLink);
        
        info.put("_links", links);

        info.put("status", "running");
        return info;
    }
}

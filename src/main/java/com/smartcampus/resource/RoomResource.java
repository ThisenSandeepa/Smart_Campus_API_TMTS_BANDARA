package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Resource class for managing rooms
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
public class RoomResource {

    private DataStore dataStore = DataStore.getInstance();

    // GET all rooms
    @GET
    public Response getAllRooms() {
        List<Room> rooms = new ArrayList<>(dataStore.getRooms().values());
        return Response.ok(rooms).build();
    }

    // POST - create new room
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            Map<String, Object> err = new HashMap<>();
            err.put("status", 400);
            err.put("error", "Bad Request");
            err.put("message", "Room ID is required");
            return Response.status(Response.Status.BAD_REQUEST).entity(err).build();
        }

        if (dataStore.roomExists(room.getId())) {
            Map<String, Object> err = new HashMap<>();
            err.put("status", 409);
            err.put("error", "Conflict");
            err.put("message", "Room with ID '" + room.getId() + "' already exists");
            return Response.status(Response.Status.CONFLICT).entity(err).build();
        }

        dataStore.addRoom(room);
        URI uri = uriInfo.getAbsolutePathBuilder().path(room.getId()).build();
        return Response.created(uri).entity(room).build();
    }

    // GET room by id
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRoom(roomId);
        if (room == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("status", 404);
            err.put("error", "Not Found");
            err.put("message", "Room '" + roomId + "' not found");
            return Response.status(Response.Status.NOT_FOUND).entity(err).build();
        }
        return Response.ok(room).build();
    }

    // DELETE room - cant delete if sensors are still assigned
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRoom(roomId);

        if (room == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("status", 404);
            err.put("error", "Not Found");
            err.put("message", "Room '" + roomId + "' not found");
            return Response.status(Response.Status.NOT_FOUND).entity(err).build();
        }

        // check if room has sensors - if yes, block deletion
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                "Cannot delete room '" + roomId + "' - it still has " +
                room.getSensorIds().size() + " sensor(s) assigned to it"
            );
        }

        dataStore.removeRoom(roomId);

        Map<String, Object> result = new HashMap<>();
        result.put("status", 200);
        result.put("message", "Room '" + roomId + "' deleted successfully");
        return Response.ok(result).build();
    }
}

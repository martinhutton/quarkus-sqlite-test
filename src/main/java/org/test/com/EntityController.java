package org.test.com;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.time.Instant;
import java.util.List;

@Path("/entities")
public class EntityController {

    @Inject
    EntityService entityService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entity> retrieveAllEntities() {
        return entityService.retrieveAllEntities();
    }

    @GET
    @Path("/entity/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Entity retrieveEntity(@PathParam("id") String id) {

        return entityService.retrieveEntity(id);
    }

    @POST
    @Path("/test")
    public void insert120Entities() {

        for(int i = 0; i <= 120; i++){

            String s = String.valueOf(i);

            entityService.insert(new Entity(s, Instant.now()));
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Entity post(Entity entity) {

        return entityService.insert(new Entity(entity.getId(), entity.getTime()));
    }

    @DELETE
    public void clearAllEntities() {
        entityService.clearAllEntities();
    }

}
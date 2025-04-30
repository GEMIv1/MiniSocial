package user.controller;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import user.entity.userEntity;
import user.interfaces.services.IService;

@Path("/user")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class userController {

	@EJB
	IService service;               

   @POST
   @Path("/register")
   public Response register(userEntity user) {
       return service.createUser(user);
    }

    @GET
    @Path("/test")
    public String test() {
        return "Test";
    }
}

package user.controller;

import javax.ejb.EJB;
//import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import user.entity.userEntity;
import user.interfaces.services.IAuthService;

@Path("/user")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class userController {

	@EJB
	IAuthService<userEntity> service;               

   @POST
   @Path("/register")
   public Response register(userEntity user) {
       return service.register(user);
    }
   
   
   @PUT
   @Path("/login")
   public Response login(userEntity user) {
	   return service.login(user);
   }
   
   

   
}

package user.controller;

import java.util.List;

import javax.ejb.EJB;
//import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import user.entity.friendRequestEntity;
import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;
import user.interfaces.services.IAuthService;
import user.interfaces.services.IFriendRequsetService;
import user.interfaces.services.IUserService;

@Path("/user")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class userController {

	@Context
    HttpServletRequest servletRequest;
	@EJB
	IAuthService<userEntity> Authservice;
	@EJB
	IUserService UserInfoService;



   @POST
   @Path("/register")
   public Response register(userEntity user) {
       return Authservice.register(user);
    }
   
   
   @POST
   @Path("/login")
   public Response login(userEntity user) {
	   return Authservice.login(user, servletRequest);
   }
   
   @PUT
   @Path("/updateProfile/{id}")
   public Response updateInfo(@PathParam("id") int targetId, userEntity user) {
	   return UserInfoService.updateInfo(targetId, user, servletRequest);   
   }
   
   @DELETE
   @Path("/logout")
   public Response logout() {
	   return Authservice.logout(servletRequest);
   }
   
   	@GET
	@Path("/viewFriends")
	public Response getAllFriends() {
		return UserInfoService.viewConnections(servletRequest);
	}
  
   
   
   
   

   
}

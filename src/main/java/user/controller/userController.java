package user.controller;

import java.util.List;
import java.util.Map;

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
import user.interfaces.services.ISearchService;
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
	@EJB
	IuserRepository userRepository;
	@EJB
	ISearchService searchService;



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
   	
   	@GET
   	@Path("/getAllGroups/{id}")
   	public Response getAllGroups(@PathParam("id") int targetId) {
   		userEntity usr = userRepository.findById(targetId);
   		return Response.ok(usr.getFriends()).build();
   	}
   	
   	@GET
   	@Path("/getAllNotifications")
   	public Response getAllNotifications() {
   		return UserInfoService.getAllNotifications(servletRequest);
   	}
   	
   	@GET
   	@Path("/getFriendSuggestions/{limit}")
   	public Response getFriendSuggestions(@PathParam("limit") int limit) {
   		return searchService.getFriendSuggestions(servletRequest, limit);
   	}
   	
   	@GET
   	@Path("/search")
   	public Response getSearchResult(@QueryParam("searchTerm") String searchTerm,@QueryParam("limit") int limit) {
   		return searchService.searchUsers(searchTerm, servletRequest, limit);
   	}
   	
   	
  
   
   
   
   

   
}

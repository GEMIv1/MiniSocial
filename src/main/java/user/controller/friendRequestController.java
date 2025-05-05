package user.controller;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import user.interfaces.services.IFriendRequsetService;

@Path("/friendRequest")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class friendRequestController {
	
	@Context
    HttpServletRequest servletRequest;
	
	
	@EJB 
	IFriendRequsetService friendRequsetService;
	
	
	@GET
	@Path("/send/{toId}")
	public Response send(@PathParam("toId") int toId) {
		return friendRequsetService.send(servletRequest, toId);
	}
	
	@PUT
	@Path("/accept/{fromId}")
	public Response acceptFriend(@PathParam("fromId") int fromId) {
		return friendRequsetService.accept(servletRequest, fromId);
	}
	
	@PUT
	@Path("/reject/{fromId}")
	public Response rejectFriend(@PathParam("fromId") int fromId) {
		return friendRequsetService.reject(servletRequest, fromId);
	}
	

}

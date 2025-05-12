package group.controller;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import group.entity.groupEntity;
import group.interfaces.services.IGroupEngagmentService;
import post.entity.postEntity;
import post.interfaces.services.IPostEngagementService;
import post.interfaces.services.IPostManagmentService;
import group.interfaces.services.IGroupAdminService;

@Path("/group")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class groupController {
	@Context
    HttpServletRequest servletRequest;
	
	@EJB
	IGroupAdminService groupManagmentService;
	
	@EJB
	IGroupEngagmentService groupEngagmentService;
	
	@EJB
	IPostEngagementService postEngagmentService;
	
	@EJB
	IPostManagmentService postManagmentService;
	
	@POST
	@Path("/create")
	public Response createGroup(groupEntity grp) {
		return groupManagmentService.createGrp(grp, servletRequest);
	}
	
	@DELETE
	@Path("/delete/{id}")
	public Response deleteGroup(@PathParam("id") int id) {
		return groupManagmentService.deleteGrp(id, servletRequest);
	}
	
	@PUT
	@Path("/changeStatus/{id}")
	public Response changeStatus(@PathParam("id") int id, boolean status) {
		return groupManagmentService.editGrpStatus(id, status, servletRequest);
	}
	
	@PUT
	@Path("/changeDescription/{id}")
	public Response changeDescription(@PathParam("id") int id, String description) {
		return groupManagmentService.editDescription(id, description, servletRequest);
	}
	
	@GET
	@Path("/viewStatus/{id}")
	public Response getStatus(@PathParam("id") int id) {
		return groupManagmentService.getGrpStatus(id);
	}
	
	@PUT
	@Path("/join/{id}")
	public Response join(@PathParam("id") int id) {
		return groupEngagmentService.join(servletRequest,id);
	}
	
	@DELETE
	@Path("/leave/{id}")
	public Response leave(@PathParam("id") int id) {
		return groupEngagmentService.leave(servletRequest,id);
	}
	
	@POST
	@Path("/createPost/{id}")
	public Response create(@PathParam("id") int id, postEntity post) {
		return postManagmentService.createPostInGrp(post, id, servletRequest);
	}
	
	
	@PUT
	@Path("/acceptUser/{id}/{grpId}")
	public Response accept(@PathParam("id") int id, @PathParam("grpId") int grpId) {
		return groupManagmentService.acceptMemeberInGrp(grpId, id, servletRequest);
	}
	
	@PUT
	@Path("/rejectUser/{id}/{grpId}")
	public Response reject(@PathParam("id") int id, @PathParam("grpId") int grpId) {
		return groupManagmentService.rejectMemeberInGrp(grpId, id, servletRequest);
	}
	
	@DELETE
	@Path("/deleteUser/{id}/{grpId}")
	public Response deleteUsr(@PathParam("id") int id, @PathParam("grpId") int grpId) {
		return groupManagmentService.deleteMemeberInGrp(grpId, id, servletRequest);
	}
	
	
	

}

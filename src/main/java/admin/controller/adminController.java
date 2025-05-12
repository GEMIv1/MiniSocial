package admin.controller;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import group.interfaces.services.IGroupAdminService;
import post.interfaces.services.IPostManagmentService;
import user.interfaces.services.IUserService;

@Path("/admin")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class adminController {
	
	@Context
    private HttpServletRequest request;
	
	@EJB
	private IPostManagmentService postManagmentService;
	
	@EJB
	private IGroupAdminService groupAdminService;
	
	@EJB
	private IUserService userManagmentService;
	
	@DELETE
	@Path("deletePost/{postId}")
	public Response deletePost(@PathParam("postId") int postId) {
		return postManagmentService.delete(postId, request);
	}
	
	@DELETE
	@Path("/deleteGroup/{groupId}")
	public Response deleteGroup(@PathParam("groupId") int id) {
		return groupAdminService.deleteGrp(id, request);
	}
	
	@DELETE
	@Path("/deleteUser/{userId}")
	public Response deleteUser(@PathParam("userId") int id) {
		return userManagmentService.deleteUsr(request, id);
	}
	
	

}

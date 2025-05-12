package post.controller;

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

import post.entity.postEntity;
import post.interfaces.services.IPostEngagementService;
import post.interfaces.services.IPostManagmentService;

@Path("/post")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class postController {
	
	@Context
    private HttpServletRequest request;
	
	@EJB
	private IPostManagmentService postManagmentService;
	
	@EJB
	private IPostEngagementService postEngagement;
	
	
	
	@POST
	@Path("/createPost")
	public Response createPost(postEntity post) {
		return postManagmentService.create(post, request);
	}
	
	@PUT
	@Path("/updatePost/{postId}")
	public Response updatePost(@PathParam("postId") int postId, String content) {
		return postManagmentService.update(postId, content, request);
	}
	
	@GET
	@Path("/TimeLine")
	public Response getTimeLine() {
		return postEngagement.viewTimeLine(request);
	}
	
	@PUT
	@Path("/Like/{postId}")
	public Response like(@PathParam("postId") int postId) {
		return postEngagement.like(request, postId);
	}
	
	@PUT
	@Path("/Comment/{postId}")
	public Response comment(@PathParam("postId") int postId, String content) {
		return postEngagement.comment(content, request, postId);
	}
	
	@DELETE
	@Path("/Delete/{postId}")
	public Response delete(@PathParam("postId") int postId) {
		return postManagmentService.delete(postId, request);
	}
	

}

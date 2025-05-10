package post.interfaces.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import post.entity.postEntity;

public interface IPostManagmentService {
	public Response create(postEntity newPost, HttpServletRequest servlet);
	public Response update(int postId, String newContent, HttpServletRequest servlet);
	public Response delete(int postId, HttpServletRequest servlet);
	public Response createPostInGrp(postEntity newPost, int grpId,HttpServletRequest servlet);

}

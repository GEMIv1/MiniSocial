package group.interfaces.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import post.entity.postEntity;
import post.interfaces.services.IPostEngagementService;
import post.interfaces.services.IPostManagmentService;
import user.entity.userEntity;

public interface IGroupEngagmentService{
	public Response join(HttpServletRequest servletReq, int grpId);
	public Response leave(HttpServletRequest servletReq, int grpId);
}

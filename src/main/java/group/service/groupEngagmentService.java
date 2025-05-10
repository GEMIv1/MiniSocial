package group.service;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import group.entity.groupEntity;
import group.entity.requestEntity;
import group.interfaces.repositories.IGroupRepository;
import group.interfaces.repositories.IRequestRepository;
import group.interfaces.services.IGroupEngagmentService;
import post.entity.postEntity;
import post.interfaces.repositories.IPostRepository;
import post.interfaces.services.IPostEngagementService;
import post.interfaces.services.IPostManagmentService;
import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;

@Stateless
@Local(IGroupEngagmentService.class)
public class groupEngagmentService implements IGroupEngagmentService{
	
	@Inject
	IGroupRepository groupDatabaseManager;
	@Inject
	IPostRepository postDatabaseManager;
	@Inject
	IuserRepository userDatabaseManager;
	@Inject
	IRequestRepository requestDatabaseManager;
	@Inject
	IPostManagmentService postManagmentService;
	@Inject
	IPostEngagementService postEngagmentService;

	@Override
	public Response join(HttpServletRequest servletReq, int grpId) {
	    HttpSession session = servletReq.getSession(false);
	    if (session == null || session.getAttribute("userId") == null) {
	        return Response.status(Response.Status.UNAUTHORIZED)
	                       .entity("User not authenticated.")
	                       .build();
	    }

	    int userId = (Integer) session.getAttribute("userId");
	    userEntity user = userDatabaseManager.findById(userId);
	    if (user == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                       .entity("User not found.")
	                       .build();
	    }

	    // 1) Already a member?
	    boolean isMember = user.getGroups()
	                           .stream()
	                           .anyMatch(g -> g.getGrpId() == grpId);
	    if (isMember) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                       .entity("You are already a member of this group.")
	                       .build();
	    }

	    groupEntity grp = groupDatabaseManager.getGroupById(grpId);
	    if (grp == null) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                       .entity("Group does not exist.")
	                       .build();
	    }

	    // 2) Private group → must send (and dedupe) a request
	    if (!grp.getGrpStatus()) {
	        if (requestDatabaseManager.existsPendingRequest(userId, grpId)) {
	            return Response.status(Response.Status.BAD_REQUEST)
	                           .entity("You have already requested to join this group.")
	                           .build();
	        }
	        requestDatabaseManager.createReq(userId, grpId);
	        return Response.ok()
	                       .entity("Request sent successfully!")
	                       .build();
	    }

	    // 3) Public group → auto‑join
	    groupDatabaseManager.addUserInGrp(userId, grpId);

	    // keep the in‑memory model in sync
	    grp.getAllUsrInGrp().add(user);
	    user.getGroups().add(grp);

	    return Response.ok()
	                   .entity("Welcome to " + grp.getName() + "!")
	                   .build();
	}

	@Override
	public Response leave(HttpServletRequest servletReq, int grpId) {
		try {
			HttpSession session = servletReq.getSession(false);
		    if (session == null || session.getAttribute("userId") == null) {
		        return Response.status(Response.Status.UNAUTHORIZED)
		                      .entity("User not authenticated.").build();
		    }
		    int userId = (Integer) session.getAttribute("userId");
		    userEntity usr = userDatabaseManager.findById(userId);
		    
		    groupDatabaseManager.removeUserInGrp(userId, grpId);

		    
		    userDatabaseManager.removeGroupFromUser(userId, grpId);
		    groupEntity grp = groupDatabaseManager.getGroupById(grpId);
		    usr.getGroups().remove(grp);
	        grp.getAllUsrInGrp().remove(usr);
		    return Response.ok().entity("Leaving " + grp.getName() + "...").build(); 
		    
			
		}catch(Exception e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Error while leaving the group").build();
		}
	}
	
//	@Override
//	public Response createPost(postEntity newPost, int groupId, HttpServletRequest servlet) {
//		try {
//		
//			HttpSession session = servlet.getSession(false);
//			if (session == null || session.getAttribute("userId") == null) {
//			    return Response.status(Response.Status.UNAUTHORIZED)
//			                  .entity("User not authenticated.").build();
//			}
//			
//			int userId = (Integer) session.getAttribute("userId");
//			userEntity user = userDatabaseManager.findById(userId);
//			
//			if (user == null ) {
//			    return Response.status(Response.Status.NOT_FOUND)
//			                  .entity("User not found.").build();
//			}
//			
//			groupEntity grpEntity = groupDatabaseManager.getGroupById(groupId);
//			newPost.setGroup(grpEntity);
//			postDatabaseManager.createPost(newPost);
//			groupDatabaseManager.createPostInGrp(groupId, userId, newPost);
//			return Response.ok().entity("Post at " + grpEntity.getName() + " group uploaded successfully!!").build(); 
//			
//		}catch(Exception e) {
//			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Error while creating a post in the join the group").build();
//		}   
//	}
//	
//	//delete, create and like post will be in the controller
	
}

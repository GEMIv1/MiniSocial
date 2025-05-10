package group.service;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import group.entity.groupEntity;
import group.interfaces.repositories.IGroupRepository;
import group.interfaces.repositories.IRequestRepository;
import group.interfaces.services.IGroupAdminService;
import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;

/*
 * delete group
 * leave group
 * delete member in the group
 * 
 * */


@Stateless
@Local(IGroupAdminService.class)
public class groupAdminService implements IGroupAdminService {

	
	@Inject
	IGroupRepository groupDatabaseManager;
	@Inject
	IuserRepository userDatabaseManager;
	@Inject
	IRequestRepository requestDatabaseManager;
	
	
	@Override
	public Response createGrp(groupEntity grp, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession(false);
		    if (session == null || session.getAttribute("userId") == null) {
		        return Response.status(Response.Status.UNAUTHORIZED)
		                      .entity("User not authenticated.").build();
		    }
		    
		    int userId = (Integer) session.getAttribute("userId");
		    userEntity user = userDatabaseManager.findById(userId);
		    
		    if (user == null ) {
		        return Response.status(Response.Status.NOT_FOUND)
		                      .entity("User not found.").build();
		    }
		    
		    grp.setCreator(user);
	        grp.getAllUsrInGrp().add(user); 
		    user.getGroups().add(grp);
			groupDatabaseManager.save(grp);
			userDatabaseManager.save(user);

		}
		catch(Exception e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Error while creating the group the group may be already exsist").build();
		}
		return Response.ok().entity("Group created successfully and you are now the admin for the group!, NOTE: THE GROUP STATUS IS CLOSED BY DEFAULT").build();
	}


	@Override
	public Response editDescription(int grpId, String newDescription, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession(false);
		    if (session == null || session.getAttribute("userId") == null) {
		        return Response.status(Response.Status.UNAUTHORIZED)
		                      .entity("User not authenticated.").build();
		    }
		    
		    int userId = (Integer) session.getAttribute("userId");
		    userEntity user = userDatabaseManager.findById(userId);
		    
		    if (user == null ) {
		        return Response.status(Response.Status.NOT_FOUND)
		                      .entity("User not found.").build();
		    }
		    
		    userEntity author = groupDatabaseManager.getAuthor(grpId);
		    
		    if(author.getId() != userId) {return Response.status(Response.Status.UNAUTHORIZED).entity("You are not the group admin").build();}
		    
		    groupDatabaseManager.updateDescription(grpId, newDescription);
			
		}
		catch(Exception e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Error while editing the description").build();
		}
		return Response.ok().entity("Description changed successfully to " + newDescription).build();

	}


	@Override
	public Response editGrpStatus(int grpId, boolean status, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession(false);
		    if (session == null || session.getAttribute("userId") == null) {
		        return Response.status(Response.Status.UNAUTHORIZED)
		                      .entity("User not authenticated.").build();
		    }
		    
		    int userId = (Integer) session.getAttribute("userId");
		    userEntity user = userDatabaseManager.findById(userId);
		    
		    if (user == null ) {
		        return Response.status(Response.Status.NOT_FOUND)
		                      .entity("User not found.").build();
		    }
		    
		    userEntity author = groupDatabaseManager.getAuthor(grpId);
		    
		    if(author.getId() != userId) {return Response.status(Response.Status.UNAUTHORIZED).entity("You are not the group admin").build();}
		    
		    int rows = groupDatabaseManager.updateStatus(grpId, status);
		    if (rows != 1) {
		      return Response.status(Response.Status.NOT_FOUND)
		                     .entity("Group not found").build();
		    }
			
		}
		catch(Exception e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Error while editing the status").build();
		}
		return Response.ok().entity("Group status change successfully!!. New status " + status).build();
	}


	@Override
	public Response deleteGrp(int grpId, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession(false);
		    if (session == null || session.getAttribute("userId") == null) {
		        return Response.status(Response.Status.UNAUTHORIZED)
		                      .entity("User not authenticated.").build();
		    }
		    
		    int userId = (Integer) session.getAttribute("userId");
		    userEntity user = userDatabaseManager.findById(userId);
		    
		    if (user == null ) {
		        return Response.status(Response.Status.NOT_FOUND)
		                      .entity("User not found.").build();
		    }
		    
		    userEntity author = groupDatabaseManager.getAuthor(grpId);
		    if (author == null) {
	            return Response.status(Response.Status.NOT_FOUND)
	                           .entity("Group not found or author is null.").build();
	        }
		    if (author.getId() != userId) {
	            return Response.status(Response.Status.UNAUTHORIZED)
	                           .entity("You are not the group admin.").build();
	        }
	        groupDatabaseManager.removeAllGroupMembers(grpId);
	        groupDatabaseManager.deleteAllGroupPosts(grpId);
		    int rows = groupDatabaseManager.deleteGroup(grpId);

		    if (rows == 0) {
		        return Response.status(Response.Status.NOT_FOUND).entity("").build();
		    }

			
		}
		catch(Exception e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Error while deleting the group").build();
		}
		return Response.ok().entity("Group deleted successfully!!").build();
	}


	@Override
	public Response deleteMemeberInGrp(int grpId, int usrId, HttpServletRequest req) {
		try {
			HttpSession session = req.getSession(false);
		    if (session == null || session.getAttribute("userId") == null) {
		        return Response.status(Response.Status.UNAUTHORIZED)
		                      .entity("User not authenticated.").build();
		    }
		    
		    int userId = (Integer) session.getAttribute("userId");
		    userEntity user = userDatabaseManager.findById(userId);
		    
		    if (user == null ) {
		        return Response.status(Response.Status.NOT_FOUND)
		                      .entity("User not found.").build();
		    }
		    
		    groupEntity grpEntity = groupDatabaseManager.getGroupById(grpId);
		    
		    if(userId == grpEntity.getCreator().getId()) {
		    	List<userEntity> usersInGrp =  grpEntity.getAllUsrInGrp();
			    for(userEntity usr : usersInGrp) {
			    	if(usr.getId() == usrId) {
			    		groupDatabaseManager.removeUserInGrp(usrId, grpId);
			    		usr.getGroups().remove(grpEntity);
			    		break;
			    	}
			    }
		    }
		    else {
		    	return Response.status(Response.Status.UNAUTHORIZED).entity("You are not the group admin").build();
		    }
		    
		    

		}catch(Exception e){
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Error while deleting user from the group").build();
		}
		return Response.ok().entity("User deleted successfully!!").build();
	}


	@Override
	public Response acceptMemeberInGrp(int grpId, int usrId, HttpServletRequest req) {
		HttpSession session = req.getSession(false);
	    if (session == null || session.getAttribute("userId") == null) {
	        return Response.status(Response.Status.UNAUTHORIZED)
	                       .entity("User not authenticated.")
	                       .build();
	    }

	    int adminId = (Integer) session.getAttribute("userId");
	    userEntity admin = userDatabaseManager.findById(adminId);
	    if (admin == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                       .entity("Admin user not found.")
	                       .build();
	    }

	    groupEntity group = groupDatabaseManager.getGroupById(grpId);
	    if (group == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                       .entity("Group not found.")
	                       .build();
	    }

	    // Only the group’s author/admin may accept members:
	    if (group.getCreator().getId() != adminId) {
	        return Response.status(Response.Status.FORBIDDEN)
	                       .entity("You are not the group admin.")
	                       .build();
	    }

	    userEntity toAccept = userDatabaseManager.findById(usrId);
	    if (toAccept == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                       .entity("User to accept not found.")
	                       .build();
	    }

	    // Optional: check they actually requested to join
	    if (!group.getAllRequestsInGrp().contains(toAccept)) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                       .entity("This user has not requested to join.")
	                       .build();
	    }

	    // 1) Persist the membership
	    groupDatabaseManager.addUserInGrp(usrId, grpId);
	    requestDatabaseManager.updateReqAccepted(usrId, grpId);

	    // 3) Update in‑memory collections
	    group.getAllUsrInGrp().add(toAccept);
	    toAccept.getGroups().add(group);
	    group.getAllRequestsInGrp().remove(toAccept);

	    return Response.ok()
	                   .entity("User " + toAccept.getName() + " has been accepted into " + group.getName())
	                   .build();
	}
	
	@Override
	public Response rejectMemeberInGrp(int grpId, int usrId, HttpServletRequest req) {
		HttpSession session = req.getSession(false);
	    if (session == null || session.getAttribute("userId") == null) {
	        return Response.status(Response.Status.UNAUTHORIZED)
	                       .entity("User not authenticated.")
	                       .build();
	    }

	    int adminId = (Integer) session.getAttribute("userId");
	    userEntity admin = userDatabaseManager.findById(adminId);
	    if (admin == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                       .entity("Admin user not found.")
	                       .build();
	    }

	    groupEntity group = groupDatabaseManager.getGroupById(grpId);
	    if (group == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                       .entity("Group not found.")
	                       .build();
	    }

	    // only the group’s creator/admin may reject requests
	    if (group.getCreator().getId() != adminId) {
	        return Response.status(Response.Status.FORBIDDEN)
	                       .entity("You are not the group admin.")
	                       .build();
	    }

	    userEntity toReject = userDatabaseManager.findById(usrId);
	    if (toReject == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                       .entity("User to reject not found.")
	                       .build();
	    }

	    // ensure they actually requested to join
	    if (!group.getAllRequestsInGrp().contains(toReject)) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                       .entity("This user has not requested to join.")
	                       .build();
	    }

	    // 1) mark the request REJECTED
	    int updated = requestDatabaseManager.updateReqReject(usrId, grpId);
	    if (updated == 0) {
	        return Response.status(Response.Status.BAD_GATEWAY).entity("Failed to update request status to REJECTED").build();
	    }

	    // 2) sync your in-memory collections
	    group.getAllRequestsInGrp().remove(toReject);

	    return Response.ok()
	                   .entity("User " + toReject.getName()
	                         + "’s request to join " + group.getName()
	                         + " has been rejected.")
	                   .build();
	}


	@Override
	public Response getGrpStatus(int grpId) {
		return Response.ok().entity("Status is " + groupDatabaseManager.getStatus(grpId)).build();
	}





}

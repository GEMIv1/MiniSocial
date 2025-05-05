package user.services;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import user.entity.RequestStatus;
import user.entity.friendRequestEntity;
import user.entity.userEntity;
import user.interfaces.repositories.IfriendRequestRepository;
import user.interfaces.repositories.IuserRepository;
import user.interfaces.services.IFriendRequsetService;

@Stateless
@Local(IFriendRequsetService.class)
public class friendRequestService implements IFriendRequsetService{
	
	@Inject
    IuserRepository userDatabaseManager;
	
	@Inject
	IfriendRequestRepository friendRequestDatabaseManager;

	@Override
	public Response send(HttpServletRequest servlet, int toId) {
		
		HttpSession session = servlet.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("Not logged in.")
                           .build();
        }
		
		int fromId = (Integer)session.getAttribute("userId");
		userEntity currUser = userDatabaseManager.findById(fromId);
		userEntity toUser = userDatabaseManager.findById(toId);
		
		for(userEntity friend : currUser.getFriends()) {
			if(friend.getId() == toId) {
				return Response.status(Response.Status.FORBIDDEN).entity("User already in your friends.").build();
			}
		}
		
		for(friendRequestEntity friend : currUser.getFriendRequestRecieved()) {
			if(friend.getReceiver().equals(toUser)) {
				return Response.status(Response.Status.FORBIDDEN).entity("User in your pending requests.").build();
			}
		}
		
		for(friendRequestEntity friend : currUser.getfriendRequestSent()) {
			if(friend.getReceiver().equals(toUser)) {
				return Response.status(Response.Status.FORBIDDEN).entity("You already sent the friend request.").build();
			}
		}
		
		
		friendRequestDatabaseManager.save(new friendRequestEntity(currUser, toUser));
		
	
		return Response.status(Response.Status.ACCEPTED).entity("Friend request sent successfully.").build();
	}

	@Override
	public Response accept(HttpServletRequest servlet, int fromId) {
		HttpSession session = servlet.getSession(false);
	    if (session == null || session.getAttribute("userId") == null) {
	        return Response.status(Response.Status.UNAUTHORIZED)
	                       .entity("Not logged in.").build();
	    }
	    int currUserId = (Integer) session.getAttribute("userId");
	    
	    if (currUserId == fromId) {
	        return Response.status(Response.Status.FORBIDDEN)
	                       .entity("Cannot accept your own request.").build();
	    }
	    
	    friendRequestEntity req = friendRequestDatabaseManager.findPendingRequest(fromId, currUserId);
	    if (req == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                       .entity("No pending request found.").build();
	    }
	    
	    req.setStatus(RequestStatus.ACCEPTED);
	    friendRequestDatabaseManager.save(req);

	    userDatabaseManager.addNewFriend(currUserId, fromId);
	    userDatabaseManager.addNewFriend(fromId, currUserId);

	    
	    friendRequestDatabaseManager.deleteAcceptedOrRejectedRequsets(currUserId, fromId);
		
		
		return Response.status(Response.Status.ACCEPTED).entity(req.getSender().getName() + " becomes now your friend.").build();
	}

	
	@Override
	public Response reject(HttpServletRequest servlet, int fromId) {
		HttpSession session = servlet.getSession(false);
	    if (session == null || session.getAttribute("userId") == null) {
	        return Response.status(Response.Status.UNAUTHORIZED)
	                       .entity("Not logged in.").build();
	    }
	    int currUserId = (Integer) session.getAttribute("userId");
	    
	    if (currUserId == fromId) {
	        return Response.status(Response.Status.FORBIDDEN)
	                       .entity("Cannot accept your own request.").build();
	    }
	    
	    friendRequestEntity req = friendRequestDatabaseManager.findPendingRequest(fromId, currUserId);
	    if (req == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                       .entity("No pending request found.").build();
	    }
	    
	    req.setStatus(RequestStatus.REJECTED);
	    friendRequestDatabaseManager.save(req);

	    friendRequestDatabaseManager.deleteAcceptedOrRejectedRequsets(currUserId, fromId);
		
		
		return Response.status(Response.Status.ACCEPTED).entity(req.getSender().getName() + " rejected.").build();
	}

	

}

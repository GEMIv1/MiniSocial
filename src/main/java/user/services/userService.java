package user.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import notifications.entity.notificationEntity;
import notifications.interfaces.repository.INotificationRepository;
import user.entity.Role;
import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;
import user.interfaces.services.IUserService;


@Stateless
@Local(IUserService.class)
public class userService implements IUserService{
	

	@Inject
	IuserRepository userDatabaseManager;
	
	@Inject
	INotificationRepository notificationDatabaseManager;

	
	//If works split it
	@Override
	public Response updateInfo(int targetId, userEntity user, HttpServletRequest request) {
		
        HttpSession session = request.getSession(false);
        
        if(session == null || session.getAttribute("userId") == null) {
        	 return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid.").build();
        }
        
        int loggedInId = (Integer) session.getAttribute("userId");
        
        // add role userEntity me = userDatabaseManager.findById(loggedInId);
        if(loggedInId == targetId || userDatabaseManager.findById(loggedInId).getRole() == Role.ADMIN) {
        	
        	userEntity toUpdate = userDatabaseManager.findById(targetId);
            if (toUpdate == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
            }
            
            toUpdate.setName(user.getName());
            toUpdate.setBio(user.getBio());
            toUpdate.setPassword(user.getPassword());
            toUpdate.setEmail(user.getEmail());

            
            userDatabaseManager.save(toUpdate);
            
            Map<String,Object> result = new HashMap<>();
            result.put("id",    toUpdate.getId());
            result.put("name",  toUpdate.getName());
            result.put("email", toUpdate.getEmail());
            result.put("bio",   Optional.ofNullable(toUpdate.getBio()).orElse(""));
            
            return Response.ok(result).build();
        	
        }
        
    	return Response.status(Response.Status.FORBIDDEN).entity("You’re not allowed to update this user.").build();
        

	}

	//If works split it
	@Override
	public Response viewConnections(HttpServletRequest servlet) {
		
		HttpSession session = servlet.getSession(false);
        
        if(session == null || session.getAttribute("userId") == null) {
        	 throw new WebApplicationException("Not logged in.", Response.Status.UNAUTHORIZED);
        }
        
        int loggedInId = (Integer) session.getAttribute("userId");
        
        List<userEntity> friends = userDatabaseManager.getAllFriend(loggedInId);

        
        JsonArrayBuilder arr = Json.createArrayBuilder();
        for (userEntity f : friends) {
            arr.add(Json.createObjectBuilder()
                        .add("id",   f.getId())
                        .add("name", f.getName()));
        }
        String json = arr.build().toString();

        return Response.ok(json).build();

	}

	@Override
	public Response getAllNotifications(HttpServletRequest servlet) {
		HttpSession session = servlet.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("User not authenticated.")
                           .build();
        }

        int userId = (Integer) session.getAttribute("userId");
        
        
        List<notificationEntity> notifications = userDatabaseManager.getNotificationsForUser(userId);

        List<Map<String,Object>> list = new ArrayList<>();
        for (notificationEntity n : notifications) {
             Map<String,Object> m = new HashMap<>();
              m.put("id",        n.getNotificationId());
              m.put("user", n.getActorUser());
              m.put("message Type",   n.getType().name());
              m.put("createdAt", n.getCreatedAt());
              m.put("read",      n.isRead());
              list.add(m);
         }

         Map<String,Object> res = new HashMap<>();
         res.put("count",         list.size());
         res.put("notifications", list);
         
         for(notificationEntity n: notifications) {
        	 n.setRead(true);
         }
         
         return Response.ok(res).build();

	}
	
	@Override
	public Response deleteUsr(HttpServletRequest servlet, int targetId) {
        HttpSession session = servlet.getSession(false);
        
        if(session == null || session.getAttribute("userId") == null) {
        	 return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid.").build();
        }
        
        int loggedInId = (Integer) session.getAttribute("userId");

        
        if(loggedInId == targetId || userDatabaseManager.findById(loggedInId).getRole() == Role.ADMIN) {
        	
        	userEntity toDelete = userDatabaseManager.findById(targetId);;
            if (toDelete == null) {
                return Response.status(Response.Status.FORBIDDEN).entity("You’re not allowed to update this user.").build();
            }
            
            userDatabaseManager.delete(targetId);
        	
        }
        
    	return Response.status(Response.Status.ACCEPTED).entity("User deleted successfully.").build();

	}
	


	
	


	

}

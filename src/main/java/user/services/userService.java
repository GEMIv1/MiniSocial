package user.services;

import java.awt.PageAttributes.MediaType;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;
import user.interfaces.services.IAuthService;
import user.interfaces.services.IUserService;

@Stateless
@Local(IUserService.class)
public class userService implements IUserService{
	

	@Inject
	IuserRepository userDatabaseManager;

	@Override
	public Response updateInfo(int targetId, userEntity user, HttpServletRequest request) {
		
        HttpSession session = request.getSession(false);
        
        if(session == null || session.getAttribute("userId") == null) {
        	 return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid.").build();
        }
        
        int loggedInId = (Integer) session.getAttribute("userId");
        
        // add role userEntity me = userDatabaseManager.findById(loggedInId);
        if(loggedInId != targetId) {
        	return Response.status(Response.Status.FORBIDDEN).entity("You’re not allowed to update this user.").build();
        }
        
        userEntity toUpdate = userDatabaseManager.findById(targetId);
        if (toUpdate == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
        }
        
        toUpdate.setName(user.getName());
        toUpdate.setBio(user.getBio());
        toUpdate.setPassword(user.getPassword());
        toUpdate.setEmail(user.getEmail());

        
        userDatabaseManager.save(toUpdate);
        
        
        return Response.ok(toUpdate).build();
	}

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


	
	


	

}

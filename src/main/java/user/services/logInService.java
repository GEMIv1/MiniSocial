package user.services;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import databaseManager.userDatabaseManager;
import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class logInService {
	
	@Inject
    private IuserRepository userDatabaseManager;
	
	public Response login(@QueryParam("email") String email, @QueryParam("password") String password ) {
        
		userEntity user = userDatabaseManager.findByEmail(email);
		if (user != null && user.getPassword().equals(password)) {
            return Response.ok("Login successful!").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
		

       
	}
}

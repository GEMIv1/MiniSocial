package user.services;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;
import user.interfaces.services.IAuthService;


@Stateless
@Local(IAuthService.class)
public class authService implements IAuthService<userEntity> {

	@Inject
    private IuserRepository userDatabaseManager;
	
	@Override
	public Response register(userEntity input) {
		
		userDatabaseManager.save(input);

        return Response.status(Response.Status.CREATED)
                .entity("User registered successfully!")
                .build();
	}
	
	//Add Token
	@Override
	public Response login(userEntity input) {
		userEntity user = userDatabaseManager.findByEmail(input.getEmail());
		if (user != null && user.getPassword().equals(input.getPassword())) {
            return Response.ok("Login successful!").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
	}
	
	


}

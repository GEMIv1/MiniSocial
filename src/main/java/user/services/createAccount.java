package user.services;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import javax.ws.rs.core.Response;

import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;
import user.interfaces.services.IService;

@Stateless
@Local(IService.class)
public class createAccount {
	
	@Inject
    private IuserRepository userDatabaseManager;
	
    public Response createUser(userEntity user) {
      
        userDatabaseManager.save( user);

        return Response.status(Response.Status.CREATED)
                .entity("User registered successfully!")
                .build();
    }
}

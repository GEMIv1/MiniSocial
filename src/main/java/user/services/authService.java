package user.services;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
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
	
	@Override
	public Response login(userEntity input, HttpServletRequest servletRequest) {
		userEntity user = userDatabaseManager.findByEmail(input.getEmail());
		if (user != null && user.getPassword().equals(input.getPassword())) {
				HttpSession oldSession = servletRequest.getSession(false);
		        if (oldSession != null) {
		            oldSession.invalidate();
		        }
           
			HttpSession session = servletRequest.getSession(true);
            session.setAttribute("userId", user.getId());


            return Response.ok("Login successful!").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
	}

	@Override
	public Response logout(HttpServletRequest servletRequest) {
		HttpSession session = servletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return Response.noContent().build(); 
	}
}

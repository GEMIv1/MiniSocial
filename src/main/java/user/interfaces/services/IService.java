package user.interfaces.services;

import javax.ws.rs.core.Response;

import user.entity.userEntity;

public interface IService {
	
	public Response createUser(userEntity user);
}

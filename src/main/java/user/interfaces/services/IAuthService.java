package user.interfaces.services;

import javax.ws.rs.core.Response;

import user.entity.userEntity;

public interface IAuthService <T> {
	public Response login(T input);
	public Response register(T input);

}

package user.interfaces.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import user.entity.userEntity;

public interface IAuthService <T> {
	public Response login(T input, HttpServletRequest servlet);
	public Response register(T input);
	public Response logout(HttpServletRequest servletRequest);
}

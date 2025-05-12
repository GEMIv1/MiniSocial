package user.interfaces.services;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import user.entity.userEntity;

public interface IUserService{
	public Response updateInfo(int userId, userEntity user, HttpServletRequest servlet);
	public Response viewConnections(HttpServletRequest servlet);
	public Response getAllNotifications(HttpServletRequest servlet);
	public Response deleteUsr(HttpServletRequest servlet, int targetId);
	
}

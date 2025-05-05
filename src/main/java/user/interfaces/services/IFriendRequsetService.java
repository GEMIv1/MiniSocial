package user.interfaces.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

public interface IFriendRequsetService {
	public Response send(HttpServletRequest servlet, int toId);
	public Response accept(HttpServletRequest servlet, int fromId);
	public Response reject(HttpServletRequest servlet, int fromId);
}

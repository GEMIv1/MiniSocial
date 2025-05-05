package post.interfaces.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

public interface IPostEngagementService {
	public Response like(HttpServletRequest req, int postId);
	public Response comment(String content, HttpServletRequest req, int postId);
	public Response viewTimeLine(HttpServletRequest req);
}

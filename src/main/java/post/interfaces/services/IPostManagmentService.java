package post.interfaces.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import post.entity.postEntity;

public interface IPostManagmentService {
	Response create(postEntity newPost, HttpServletRequest servlet);
	Response update(int postId, String newContent, HttpServletRequest servlet);
    void delete(int postId);
}

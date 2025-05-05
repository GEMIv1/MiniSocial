package post.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import post.entity.postEntity;
import post.interfaces.repositories.IPostRepository;
import post.interfaces.services.IPostManagmentService;
import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;
import user.interfaces.services.IUserService;


@Stateless
@Local(IPostManagmentService.class)
public class postManagmentService implements IPostManagmentService {
	
	@Inject
	IPostRepository postDatabaseManager;
	@Inject
	IuserRepository userDatabaseManager;


	@Override
	public Response create(postEntity newPost, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("User is not authenticated.")
                           .build();
        }

        int userId = (Integer) session.getAttribute("userId");
        userEntity author = userDatabaseManager.findById(userId);
        if (author == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Invalid user.")
                           .build();
        }

        newPost.setAuthor(author);
        postDatabaseManager.createPost(newPost);
        author.getPosts().add(newPost);
        
        return Response.status(Response.Status.CREATED)
                       .entity("Post created successfully!")
                       .build();
    }


	@Override
	public Response update(int postId, String newContent, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("User not authenticated.")
                           .build();
        }

        int userId = (Integer) session.getAttribute("userId");
        
        postEntity post = postDatabaseManager.findById(postId);
        if (post == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("Post not found.")
                           .build();
        }

        if (post.getAuthor().getId() != userId) {
            return Response.status(Response.Status.FORBIDDEN)
                           .entity("Not authorized to update this post.")
                           .build();
        }

        postEntity updated = postDatabaseManager.updateContent(postId, newContent, userId);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("id", updated.getPostId());
        responseMap.put("content", updated.getContent());

        if (updated.getAuthor() != null) {
            Map<String, Object> authorMap = new HashMap<>();
            authorMap.put("id", updated.getAuthor().getId());
            authorMap.put("username", updated.getAuthor().getName());
            responseMap.put("author", authorMap);
        }

        return Response.ok(responseMap).build();
    }

	@Override
	public void delete(int postId) {
		postDatabaseManager.delete(postId);
		
	}
	
	
}

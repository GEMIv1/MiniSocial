package post.services;

import java.util.List;      
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import group.entity.groupEntity;
import post.entity.commentEntity;
import post.entity.likeEntity;
import post.entity.postEntity;
import post.interfaces.repositories.IPostRepository;
import post.interfaces.services.IPostEngagementService;
import user.entity.userEntity;
import user.interfaces.repositories.ICommentRepository;
import user.interfaces.repositories.ILikeRepository;
import user.interfaces.repositories.IuserRepository;

@Stateless
@Local(IPostEngagementService.class)
public class postEngagementService implements IPostEngagementService{
	
	
	@Inject
	IuserRepository userDatabaseManager;
	@Inject
	IPostRepository postDatabaseManager;
	@Inject
	ILikeRepository likeDatabaseManager;
	@Inject
	ICommentRepository commentDatabaseManager;
	
	

	@Override
	public Response like(HttpServletRequest req, int postId) {
		HttpSession session = req.getSession(false);
	    if (session == null || session.getAttribute("userId") == null) {
	        return Response.status(Response.Status.UNAUTHORIZED)
	                      .entity("User not authenticated.").build();
	    }
	    
	    int userId = (Integer) session.getAttribute("userId");
	    userEntity user = userDatabaseManager.findById(userId);
	    postEntity post = postDatabaseManager.findById(postId);
	    
	    if (user == null || post == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                      .entity("User or post not found.").build();
	    }
	    
	    likeEntity existingLike = likeDatabaseManager.findByUserAndPost(user, post);
	    long totalLikes = 0;
	    
	    if (existingLike != null) {
	        likeDatabaseManager.delete(existingLike);
	        totalLikes = likeDatabaseManager.countLikesByPost(post);
	        return Response.ok()
	            .entity(Map.of(
	                "action", "unliked",
	                "totalLikes", totalLikes
	            )).build();
	    } else {
	    	
	        likeEntity newLike = new likeEntity();
	        newLike.setUser(user);
	        newLike.setPost(post);
	        likeDatabaseManager.save(newLike);
	        totalLikes = likeDatabaseManager.countLikesByPost(post);
	        return Response.ok()
	            .entity(Map.of(
	                "action", "liked",
	                "totalLikes", totalLikes
	            )).build();
	    }
	}

	@Override
	public Response comment(String content, HttpServletRequest req, int postId) {
		
		HttpSession session = req.getSession(false);
	    if (session == null || session.getAttribute("userId") == null) {
	        return Response.status(Response.Status.UNAUTHORIZED)
	                      .entity("User not authenticated.").build();
	    }
	    
	    int userId = (Integer) session.getAttribute("userId");
	    userEntity user = userDatabaseManager.findById(userId);
	    postEntity post = postDatabaseManager.findById(postId);
	    
	    if (user == null || post == null) {
	        return Response.status(Response.Status.NOT_FOUND)
	                      .entity("User or post not found.").build();
	    }
	    
	    commentEntity comment = new commentEntity();
	    comment.setContent(content);
	    comment.setAuthor(user);
	    comment.setPost(post);
	    commentDatabaseManager.save(comment);
	    
	    return Response.status(Response.Status.ACCEPTED).entity("Comment done successfully!").build();
	}

	@Override
	public Response viewTimeLine(HttpServletRequest request) {
	    HttpSession session = request.getSession(false);
	    if (session == null || session.getAttribute("userId") == null) {
	        return Response.status(Response.Status.UNAUTHORIZED)
	                       .entity("User is not authenticated.")
	                       .build();
	    }

	    int userId = (Integer) session.getAttribute("userId");
	    userEntity currUser = userDatabaseManager.findById(userId);
	    if (currUser == null) {
	        return Response.status(Response.Status.BAD_REQUEST)
	                       .entity("Invalid user.")
	                       .build();
	    }

	    Set<Integer> seenPostIds = new HashSet<>();
	    List<Map<String, Object>> timeLine = new ArrayList<>();

	    for (groupEntity grp : currUser.getGroups()) {
	        List<Map<String, Object>> postSummaries = new ArrayList<>();

	        for (postEntity post : grp.getAllPostsInGrp()) {
	            if (!seenPostIds.add(post.getPostId())) continue;

	            Map<String, Object> postData = new HashMap<>();
	            postData.put("writer", post.getAuthor().getName());
	            postData.put("content", post.getContent());
	            postData.put("groupName", grp.getName());

	            long likeCount = likeDatabaseManager.countLikesByPost(post);
	            postData.put("likes", likeCount);

	            List<Map<String, Object>> commentSummaries = new ArrayList<>();
	            for (commentEntity comment : post.getComments()) {
	                Map<String, Object> commentData = new HashMap<>();
	                commentData.put("author", comment.getAuthor().getName());
	                commentData.put("content", comment.getContent());
	                commentSummaries.add(commentData);
	            }
	            postData.put("comments", commentSummaries);

	            postSummaries.add(postData);
	        }

	        if (!postSummaries.isEmpty()) {
	            Map<String, Object> entry = new HashMap<>();
	            entry.put("groupName", grp.getName());
	            entry.put("posts", postSummaries);
	            timeLine.add(entry);
	        }
	    }

	    for (userEntity friend : currUser.getFriends()) {
	        List<Map<String, Object>> postSummaries = new ArrayList<>();

	        for (postEntity post : friend.getPosts()) {
	            if (!seenPostIds.add(post.getPostId())) continue;

	            Map<String, Object> postData = new HashMap<>();
	            postData.put("writer", post.getAuthor().getName());
	            postData.put("content", post.getContent());
	            postData.put("friendName", friend.getName());

	            long likeCount = likeDatabaseManager.countLikesByPost(post);
	            postData.put("likes", likeCount);

	            List<Map<String, Object>> commentSummaries = new ArrayList<>();
	            for (commentEntity comment : post.getComments()) {
	                Map<String, Object> commentData = new HashMap<>();
	                commentData.put("author", comment.getAuthor().getName());
	                commentData.put("content", comment.getContent());
	                commentSummaries.add(commentData);
	            }
	            postData.put("comments", commentSummaries);

	            postSummaries.add(postData);
	        }

	        if (!postSummaries.isEmpty()) {
	            Map<String, Object> entry = new HashMap<>();
	            entry.put("friendName", friend.getName());
	            entry.put("posts", postSummaries);
	            timeLine.add(entry);
	        }
	    }
	    


	    return Response.ok(timeLine).build();
	}

}

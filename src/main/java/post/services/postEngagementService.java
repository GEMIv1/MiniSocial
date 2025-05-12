package post.services;

import java.util.List;      
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import group.entity.groupEntity;
import notifications.interfaces.services.notificationProducer;
import notifications.services.notificationCommentPostProducer;
import notifications.services.notificationLikePostProducer;
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
    @EJB(beanName="likePostProducer")
	notificationProducer LikeProducer;
    @EJB(beanName="commentPostProducer")
	notificationProducer commentProducer;
	
	

    @Override
    public Response like(HttpServletRequest req, int postId) {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                           .entity("User not authenticated.")
                           .build();
        }

        int userId = (Integer) session.getAttribute("userId");
        userEntity user = userDatabaseManager.findById(userId);
        postEntity post = postDatabaseManager.findById(postId);

        if (user == null || post == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User or post not found.")
                           .build();
        }

        groupEntity grp = post.getGroup();
        if (grp != null) {
            if (!user.getGroups().contains(grp)) {
                return Response.status(Response.Status.FORBIDDEN)
                               .entity("Cannot like a post in a group you’re not in.")
                               .build();
            }
        }
        else {
            userEntity author = post.getAuthor();
            if (!user.getFriends().contains(author)) {
                return Response.status(Response.Status.FORBIDDEN)
                               .entity("Cannot like posts of non-friends.")
                               .build();
            }
        }

        likeEntity existing = likeDatabaseManager.findByUserAndPost(user, post);
        long totalLikes;
        if (existing != null) {
            likeDatabaseManager.delete(existing);
            totalLikes = likeDatabaseManager.countLikesByPost(post);

            return Response.ok(Map.of(
                    "action", "unliked",
                    "totalLikes", totalLikes
            )).build();
        } else {
            likeEntity newLike = new likeEntity();
            newLike.setUser(user);
            newLike.setPost(post);
            likeDatabaseManager.save(newLike);

            totalLikes = likeDatabaseManager.countLikesByPost(post);

            LikeProducer.notify(
                post.getAuthor().getId(),
                userId,
                post.getPostId(),
                grp != null ? grp.getGrpId() : null
            );

            return Response.ok(Map.of(
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
                           .entity("User not authenticated.")
                           .build();
        }

        int userId = (Integer) session.getAttribute("userId");
        userEntity user = userDatabaseManager.findById(userId);
        postEntity post = postDatabaseManager.findById(postId);

        if (user == null || post == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("User or post not found.")
                           .build();
        }

        groupEntity grp = post.getGroup();
        if (grp != null) {
            if (!user.getGroups().contains(grp)) {
                return Response.status(Response.Status.FORBIDDEN)
                               .entity("Cannot comment on a post in a group you’re not in.")
                               .build();
            }
        } else {
            userEntity author = post.getAuthor();
            if (!user.getFriends().contains(author)) {
                return Response.status(Response.Status.FORBIDDEN)
                               .entity("Cannot comment on posts of non-friends.")
                               .build();
            }
        }

        commentEntity comment = new commentEntity();
        comment.setContent(content);
        comment.setAuthor(user);
        comment.setPost(post);
        commentDatabaseManager.save(comment);

        commentProducer.notify(
            post.getAuthor().getId(),
            userId,
            post.getPostId(),
            grp != null ? grp.getGrpId() : null
        );

        return Response.status(Response.Status.ACCEPTED)
                       .entity("Comment added successfully.")
                       .build();
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

        Set<groupEntity> myGroups = new HashSet<>(currUser.getGroups());
        Set<Integer> seenPostIds = new HashSet<>();
        List<Map<String,Object>> timeLine = new ArrayList<>();

        for (groupEntity grp : myGroups) {
            List<Map<String,Object>> posts = grp.getAllPostsInGrp().stream()
                .filter(p -> seenPostIds.add(p.getPostId()))
                .map(p -> buildPostData(p, grp.getName(), "groupName"))
                .collect(Collectors.toList());

            if (!posts.isEmpty()) {
                Map<String,Object> entry = new HashMap<>();
                entry.put("groupName", grp.getName());
                entry.put("posts", posts);
                timeLine.add(entry);
            }
        }

        for (userEntity friend : currUser.getFriends()) {
            List<Map<String,Object>> posts = friend.getPosts().stream()
                .filter(p -> seenPostIds.add(p.getPostId()))
                .filter(p -> {
                    groupEntity g = p.getGroup();
                    return g == null || myGroups.contains(g);
                })
                .map(p -> {
                    groupEntity g = p.getGroup();
                    String labelKey = (g != null) ? "groupName" : "friendName";
                    String labelValue = (g != null) ? g.getName() : friend.getName();
                    return buildPostData(p, labelValue, labelKey);
                })
                .collect(Collectors.toList());

            if (!posts.isEmpty()) {
                Map<String,Object> entry = new HashMap<>();
                entry.put("friendName", friend.getName());
                entry.put("posts", posts);
                timeLine.add(entry);
            }
        }

        return Response.ok(timeLine).build();
    }

    private Map<String,Object> buildPostData(postEntity post, String label, String labelKey) {
        Map<String,Object> data = new HashMap<>();
        data.put("writer",  post.getAuthor().getName());
        data.put("content", post.getContent());
        data.put(labelKey,   label);
        data.put("likes",    likeDatabaseManager.countLikesByPost(post));

        List<Map<String,String>> comments = post.getComments().stream()
            .map(c -> {
                Map<String,String> cm = new HashMap<>();
                cm.put("author",  c.getAuthor().getName());
                cm.put("content", c.getContent());
                return cm;
            }).collect(Collectors.toList());

        data.put("comments", comments);
        return data;
    }

}

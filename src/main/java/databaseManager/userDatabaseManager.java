package databaseManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import group.entity.groupEntity;
import group.entity.requestEntity;
import notifications.entity.notificationEntity;
import post.entity.commentEntity;
import post.entity.likeEntity;
import post.entity.postEntity;
import user.entity.friendRequestEntity;
import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;

// use named parameter query

@Stateless
public class userDatabaseManager implements IuserRepository {
	
	
	
	@PersistenceContext(unitName = "hello")
	EntityManager em;

	@Override
	public void save(userEntity user) {
		em.merge(user);	
	}

	@Override
	public userEntity findById(int id) {
		
		try {
			return em.createQuery("select u from userEntity u where u.id = :id", userEntity.class).setParameter("id", id).getSingleResult();
			
		}catch(NoResultException e) {
			System.out.print(e.getMessage());
			return null;
		}
	}

	@Override
	public userEntity findByEmail(String email) {
		  try {
		        return em.createQuery("SELECT u FROM userEntity u WHERE u.email = :email", userEntity.class).setParameter("email", email).getSingleResult();
		    } catch (NoResultException e) {
				System.out.print(e.getMessage());
		        return null;
		    }	
	}

	@Override
	public List<userEntity> findAll() {
		return em.createQuery("SELECT u FROM UserEntity u", userEntity.class).getResultList();
	}

	@Override
	public void delete(int userId) {
	    userEntity user = em.find(userEntity.class, userId);
	    if (user == null) {
	        throw new EntityNotFoundException("User with ID " + userId + " not found");
	    }
	    
	    for (userEntity friend : user.getFriendOf()) {
	        friend.getFriends().remove(user);
	        em.merge(friend);
	    }
	    
	    user.getFriends().clear();
	    em.merge(user);
	    
	    List<friendRequestEntity> sentRequestsToDelete = new ArrayList<>(user.getfriendRequestSent());
	    for (friendRequestEntity req : sentRequestsToDelete) {
	        em.remove(req);
	    }
	    
	    List<friendRequestEntity> receivedRequestsToDelete = new ArrayList<>(user.getFriendRequestRecieved());
	    for (friendRequestEntity req : receivedRequestsToDelete) {
	        em.remove(req);
	    }
	    
	    List<postEntity> postsToDelete = new ArrayList<>(user.getPosts());
	    for (postEntity post : postsToDelete) {
	        em.remove(post);
	    }
	    
	    List<commentEntity> commentsToDelete = new ArrayList<>(user.getComments());
	    for (commentEntity comment : commentsToDelete) {
	        em.remove(comment);
	    }
	    
	    List<likeEntity> likesToDelete = new ArrayList<>(user.getLikes());
	    for (likeEntity like : likesToDelete) {
	        em.remove(like);
	    }
	    
	    List<notificationEntity> receivedNotifications = new ArrayList<>(user.getNotificationReceived());
	    for (notificationEntity notification : receivedNotifications) {
	        em.remove(notification);
	    }
	    
	    List<notificationEntity> sentNotifications = new ArrayList<>(user.getNotificationSent());
	    for (notificationEntity notification : sentNotifications) {
	        em.remove(notification);
	    }
	    
	    List<groupEntity> groupsToDelete = new ArrayList<>(user.getGroupsCreated());
	    for (groupEntity group : groupsToDelete) {
	        group.getAllUsrInGrp().clear();
	        em.merge(group);
	        
	        em.remove(group);
	    }
	    
	    List<requestEntity> requestsToDelete = new ArrayList<>(user.getRequests());
	    for (requestEntity request : requestsToDelete) {
	        em.remove(request);
	    }
	    
	    em.remove(user);
	}
	
	@Override
    public void addNewFriend(int userId, int newFriendId) {
        em.createNativeQuery(
            "INSERT INTO user_friends (user_id, friend_id) VALUES (:uid, :fid)")
          .setParameter("uid", userId)
          .setParameter("fid", newFriendId)
          .executeUpdate();
    }
	
	@Override
	public List<userEntity> getAllFriend(int id) {
	    return em.createQuery(
	        "SELECT f FROM userEntity u JOIN u.friends f WHERE u.id = :id",
	        userEntity.class)
	      .setParameter("id", id)
	      .getResultList();
	}
	
	@Override
	public void removeGroupFromUser(int userId, int groupId) {
	      em.createNativeQuery(
	          "DELETE FROM user_groups WHERE user_id = :u AND group_id = :g"
	      )
	      .setParameter("u", userId)
	      .setParameter("g", groupId)
	      .executeUpdate();
	  }
	
	@Override
	public List<groupEntity> getAllUserGrps(int userId) {
		return em.createQuery(
	               "SELECT g FROM groupEntity g JOIN g.users u WHERE u.userId  = :uid",
	               groupEntity.class
	           )
	           .setParameter("uid", userId)
	           .getResultList();
	}
	
	@Override
	public void addUserToGroupNative(int userId, int groupId) {
	    em.createNativeQuery(
	        "INSERT INTO user_groups (user_id, group_id) VALUES (?, ?)"
	    ).setParameter(1, userId).setParameter(2, groupId).executeUpdate();
	}
	
	@Override
	public List<notificationEntity> getNotificationsForUser(int userId) {
        return em.createQuery(
                "SELECT n FROM notificationEntity n\n" +
                " WHERE n.recipient.userId = :uid\n" +
                " ORDER BY n.createdAt DESC",
                notificationEntity.class
            )
            .setParameter("uid", userId)
            .getResultList();
    }
	
	@Override
	public List<userEntity> searchUsers(String searchTerm, int currentUserId) {
		String like = "%" + searchTerm + "%";

	    return em.createQuery(
	            "SELECT DISTINCT u                               \n" +
	             "  FROM userEntity u                             \n" +
	             "  LEFT JOIN FETCH u.friends                     \n" +
	             " WHERE u.userId != :currentUserId               \n" +
	             "   AND (LOWER(u.name)  LIKE :like               \n" +
	             "     OR LOWER(u.email) LIKE :like)              \n" +
	             " ORDER BY u.name ASC", 
	                    userEntity.class)
	                  .setParameter("currentUserId", currentUserId)
	                  .setParameter("like", like)
	                  .getResultList();
    }
	
	@Override
	public List<userEntity> findNonFriendUsers(int userId, List<Integer> friendIds) {
	    String queryStr = "SELECT u FROM userEntity u WHERE u.userId != :userId " +
	                      "AND u.userId NOT IN :friendIds";

	    if (friendIds == null || friendIds.isEmpty()) {
	        friendIds = Collections.singletonList(-1);
	    }

	    return em.createQuery(queryStr, userEntity.class)
	             .setParameter("userId", userId)
	             .setParameter("friendIds", friendIds)
	             .getResultList();
	}
	

}

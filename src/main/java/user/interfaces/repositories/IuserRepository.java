package user.interfaces.repositories;

import java.util.List;

import group.entity.groupEntity;
import notifications.entity.notificationEntity;
import user.entity.friendRequestEntity;
import user.entity.userEntity;

public interface IuserRepository {

    public void save(userEntity user);

    public userEntity findById(int id);

    public userEntity findByEmail(String email);

    public List<userEntity> findAll();

    public void delete(int user);
    	
	public void addNewFriend(int userId, int newFriendId);
	
	public List<userEntity> getAllFriend(int id);

	public void removeGroupFromUser(int userId, int groupId);

	public List<groupEntity> getAllUserGrps(int userId);

	public void addUserToGroupNative(int userId, int groupId);

	public List<notificationEntity> getNotificationsForUser(int usrId);

	public List<userEntity> searchUsers(String searchTerm, int currentUserId);

	public List<userEntity> findNonFriendUsers(int userId, List<Integer> friendIds);

}
package user.interfaces.repositories;

import java.util.List;

import group.entity.groupEntity;
import user.entity.friendRequestEntity;
import user.entity.userEntity;

public interface IuserRepository {

    public void save(userEntity user);

    public userEntity findById(int id);

    public userEntity findByEmail(String email);

    public List<userEntity> findAll();

    public void update(userEntity user);

    public void delete(userEntity user);
    	
	public void addNewFriend(int userId, int newFriendId);
	
	public List<userEntity> getAllFriend(int id);

	public void removeGroupFromUser(int userId, int groupId);

	public List<groupEntity> getAllUserGrps(int userId);

	void addUserToGroupNative(int userId, int groupId);

}
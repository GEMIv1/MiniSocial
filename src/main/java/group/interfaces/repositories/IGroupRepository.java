package group.interfaces.repositories;

import java.util.List;

import group.entity.groupEntity;
import post.entity.postEntity;
import user.entity.userEntity;

public interface IGroupRepository {
	public void save(groupEntity grp);
	public int addUserInGrp(int usrId, int grpId);
	public int removeUserInGrp(int usrId, int grpId);
	public List<userEntity> getAllUsersInGrp(int grpId);
	public int createPostInGrp(int grpId, int author, postEntity post);
	public List<postEntity> getAllPostsInGrp(int grpId);
	public userEntity getAuthor(int usrId);
	public int updateDescription(int grpId, String newDescription);
	public int updateStatus(int grpId, boolean status);
	public int deleteGroup(int grpId);
	public boolean getStatus(int grpId);
	public groupEntity getGroupById(int grpId);
}

package user.interfaces.repositories;

import java.util.List;

import user.entity.friendRequestEntity;
import user.entity.userEntity;

public interface IfriendRequestRepository {
	public void save(friendRequestEntity friendR);
	public void remove(int currUserId, int fromId);
	public friendRequestEntity findPendingRequest(int senderId, int receiverId);
	public void deleteAcceptedOrRejectedRequsets(int senderId, int receiverId);
}

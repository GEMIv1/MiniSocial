package group.interfaces.repositories;

import group.entity.requestEntity;

public interface IRequestRepository {

	public int createReq(int usr, int grp);
	public int deleteReq(int usr, int grp);
	public int updateReqReject(int userId, int grpId);
	public int updateReqAccepted(int userId, int grpId);
	boolean existsPendingRequest(int userId, int grpId);
	
}

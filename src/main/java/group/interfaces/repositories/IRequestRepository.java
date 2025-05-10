package group.interfaces.repositories;


public interface IRequestRepository {

	public int createReq(int usr, int grp);
	public int deleteReq(int usr, int grp);
	public int updateReqReject(int userId, int grpId);
	public int updateReqAccepted(int userId, int grpId);
	public boolean existsPendingRequest(int userId, int grpId);
	
}

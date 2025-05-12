package databaseManager;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import group.entity.RequestStatus;
import group.interfaces.repositories.IRequestRepository;

@Stateless
public class requestDatabaseManager implements IRequestRepository{

	@PersistenceContext(unitName = "hello")
	EntityManager em;
	
	@Override
	public int createReq(int usrId, int grp) {
		return em.createNativeQuery("INSERT INTO requests (user_id, group_id, status) VALUES (:u, :g, :s)")
        .setParameter("u", usrId)
        .setParameter("g", grp)
        .setParameter("s", RequestStatus.PENDING.name())
        .executeUpdate();
	}

	@Override
	public int deleteReq(int usrId, int grpId) {
		return em.createNativeQuery(
	            "DELETE FROM requests WHERE user_id = :u AND group_id = :g")
		        .setParameter("u", usrId)
		        .setParameter("g", grpId)
		        .executeUpdate();
	}

	@Override
	public int updateReqReject(int userId, int grpId) {
		return em.createQuery(
		        "UPDATE requestEntity r " +
		        "SET r.status = :status " +
		        "WHERE r.user.id = :uid AND r.group.id = :gid"
		    )
		    .setParameter("status", RequestStatus.REJECTED)   
		    .setParameter("uid",    userId)                   
		    .setParameter("gid",    grpId)         
		    .executeUpdate();             
	}
	
	@Override
	public int updateReqAccepted(int userId, int grpId) {
        return em.createQuery(
                "UPDATE requestEntity r " +
                "  SET r.status = :status " +
                "WHERE r.user.userId  = :uid " +
                "  AND r.group.groupId = :gid"
            )
            .setParameter("status", RequestStatus.APPROVED)
            .setParameter("uid",    userId)
            .setParameter("gid",    grpId)
            .executeUpdate();
    }
	@Override
	public boolean existsPendingRequest(int userId, int grpId) {
	    Long count = em.createQuery(
	        "SELECT COUNT(r) FROM requestEntity r " +
	        " WHERE r.user.userId   = :uid" +
	        "   AND r.group.groupId  = :gid" +
	        "   AND r.status         = :pending",
	        Long.class
	    )
	    .setParameter("uid",     userId)
	    .setParameter("gid",     grpId)
	    .setParameter("pending", RequestStatus.PENDING)
	    .getSingleResult();

	    return count > 0;
	}

}

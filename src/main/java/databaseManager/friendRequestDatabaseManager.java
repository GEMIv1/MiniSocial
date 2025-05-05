package databaseManager;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import user.entity.RequestStatus;
import user.entity.friendRequestEntity;
import user.entity.userEntity;
import user.interfaces.repositories.IfriendRequestRepository;


@Stateless

public class friendRequestDatabaseManager implements IfriendRequestRepository {
	
	@PersistenceContext(unitName = "hello")
	EntityManager em;
	
	@Override
	public void save(friendRequestEntity friendR) {
		em.persist(friendR);
	}

	@Override
	public void remove(int currUserId, int fromId) {
		em.createQuery(
		        "DELETE FROM friendRequestEntity fr " +
		        "WHERE fr.sender.id   = :currUserId " +
		        "  AND fr.receiver.id = :fromId"
		    )
		    .setParameter("currUserId", currUserId)
		    .setParameter("fromId",       fromId)
		    .executeUpdate();  	
	}
	
	@Override
	public friendRequestEntity findPendingRequest(int senderId, int receiverId) {
	    try {
	        return em.createQuery(
	                "SELECT fr FROM friendRequestEntity fr "
	              + " WHERE fr.sender.id   = :senderId "
	              + "   AND fr.receiver.id = :receiverId "
	              + "   AND fr.status      = :pending",
	                friendRequestEntity.class)
	            .setParameter("senderId",   senderId)
	            .setParameter("receiverId", receiverId)
	            .setParameter("pending",    RequestStatus.PENDING)
	            .getSingleResult(); 
	    } catch (NoResultException e) {
	    	System.out.print(e.getMessage());
	        return null;
	    }
	}
	
	@Override 
	public void deleteAcceptedOrRejectedRequsets(int senderId, int receiverId) {
		em.createQuery(
		        "DELETE FROM friendRequestEntity fr " +
		        " WHERE fr.sender.id   = :senderId " +
		        "   AND fr.receiver.id = :receiverId " +
		        "   AND fr.status     <> :pending"
		    )
		    .setParameter("senderId",   senderId)
		    .setParameter("receiverId", receiverId)
		    .setParameter("pending",    RequestStatus.PENDING)
		    .executeUpdate();
	}
	
}

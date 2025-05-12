package databaseManager;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import notifications.entity.NotificationType;
import notifications.entity.notificationEntity;
import notifications.interfaces.repository.INotificationRepository;

@Stateless
public class notificationDatabaseManager implements INotificationRepository {

	@PersistenceContext(unitName = "hello")
	EntityManager em;
	
	@Override
	public void save(notificationEntity notification) {
		em.persist(notification);
	}
	
	@Override
	public notificationEntity findByRecipientActorAndType(int recipientId, int actorId, NotificationType type) {
        try {
            return em.createQuery(
                "SELECT n FROM notificationEntity n " +
                "WHERE n.recipient.id = :recipientId " +
                "AND n.actor.id = :actorId " +
                "AND n.type = :type", 
                notificationEntity.class).setParameter("recipientId", recipientId).setParameter("actorId", actorId).setParameter("type", type).getSingleResult();
            
        } catch (NoResultException e) {
            return null;
        }
    }
}

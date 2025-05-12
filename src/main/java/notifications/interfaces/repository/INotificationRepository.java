package notifications.interfaces.repository;

import notifications.entity.NotificationType;
import notifications.entity.notificationEntity;

public interface INotificationRepository {
	public void save(notificationEntity notification);
	public notificationEntity findByRecipientActorAndType(int recipientId, int actorId, NotificationType type);
}

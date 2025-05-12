package notifications.services;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.jboss.logging.Logger;

import notifications.entity.NotificationType;
import notifications.entity.notificationEntity;
import notifications.interfaces.repository.INotificationRepository;
import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;

@MessageDriven(
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/NotificationsQueue"),
        @ActivationConfigProperty(propertyName = "destinationType",       propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode",       propertyValue = "Auto-acknowledge")
    }
)
public class mdbNotificationConsumer implements MessageListener {

    private static final Logger logger = Logger.getLogger(mdbNotificationConsumer.class);

    @Inject
    private INotificationRepository notificationRepository;

    @Inject
    private IuserRepository userDatabaseManager;

    @Override
    public void onMessage(Message message) {
        if (!(message instanceof MapMessage)) {
            logger.warn("Received non-MapMessage: " + message);
            return;
        }

        MapMessage m = (MapMessage) message;
        Integer recipientId = null, actorId = null, grpId = null, postId = null;
        NotificationType type = null;

        try {
            if (m.itemExists("recipientUserId") && m.getObject("recipientUserId") != null) {
                recipientId = m.getInt("recipientUserId");
            } else {
                logger.warn("Key 'recipientUserId' missing or null; cannot process this field.");
            }

            if (m.itemExists("actorUserId") && m.getObject("actorUserId") != null) {
                actorId = m.getInt("actorUserId");
            } else {
                logger.warn("Key 'actorUserId' missing or null; cannot process this field.");
            }

            if (m.itemExists("type") && m.getString("type") != null) {
                try {
                    type = NotificationType.valueOf(m.getString("type"));
                } catch (IllegalArgumentException iae) {
                    logger.errorf(iae, "Invalid NotificationType value '%s'; skipping type.", m.getString("type"));
                }
            } else {
                logger.warn("Key 'type' missing or null; skipping type.");
            }

            if (m.itemExists("grpId") && m.getObject("grpId") != null) {
                grpId = m.getInt("grpId");
            } else {
                logger.debug("Key 'grpId' missing or null; defaulting to null.");
            }

            if (m.itemExists("postId") && m.getObject("postId") != null) {
                postId = m.getInt("postId");
            } else {
                logger.debug("Key 'postId' missing or null; defaulting to null.");
            }

            if (recipientId == null || actorId == null || type == null) {
                logger.error("Mandatory fields missing; cannot create notification.");
                return;
            }

            userEntity recipient = userDatabaseManager.findById(recipientId);
            userEntity actor     = userDatabaseManager.findById(actorId);

            if (!notificationExists(recipientId, actorId, type)) {
                notificationEntity notif = new notificationEntity();
                notif.setRecipientUser(recipient);
                notif.setActorUser(actor);
                notif.setType(type);
                if (grpId != null) notif.setGroupId(grpId);
                if (postId != null) notif.setPostId(postId);
                notif.setRead(false);
                notificationRepository.save(notif);
                logger.infof("Saved new notification: recipient=%d, actor=%d, type=%s, grpId=%s, postId=%s",
                    recipientId, actorId, type, grpId, postId);
            } else {
                logger.debugf("Notification already exists: recipient=%d, actor=%d, type=%s", recipientId, actorId, type);
            }
        } catch (JMSException jmse) {
            logger.error("JMSException when processing MapMessage", jmse);
        } catch (Exception e) {
            logger.error("Unexpected error processing JMS message, skipping.", e);
        }
    }

    private boolean notificationExists(int recipientId, int actorId, NotificationType type) {
        try {
            return notificationRepository.findByRecipientActorAndType(recipientId, actorId, type) != null;
        } catch (Exception e) {
            logger.errorf(e, "Error checking existing notification for recipientId=%d, actorId=%d, type=%s", recipientId, actorId, type);
            return true;
        }
    }
}

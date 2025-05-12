package notifications.services;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.json.Json;

import notifications.entity.NotificationType;
import notifications.entity.notificationEntity;
import notifications.interfaces.services.notificationProducer;
import user.entity.userEntity;
import user.interfaces.repositories.IuserRepository;

@Stateless(name="friendReqProducer")
@Local(notificationProducer.class)
public class notificationFriendReqProducer extends notificationProducer {
	
	@Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    private ConnectionFactory cf;

    @Resource(lookup = "java:/jms/queue/NotificationsQueue")
    private Queue queue;
    

    
    private JMSContext context() {
        return cf.createContext(Session.AUTO_ACKNOWLEDGE);
    }

	
	@Override
	public void notify(Integer recipientUserId, Integer actorId, Integer postId, Integer grpId) {
		try (JMSContext ctx = context()) {
        	MapMessage msg = ctx.createMapMessage();

            msg.setInt   ("recipientUserId", recipientUserId);
            msg.setInt   ("actorUserId", actorId);
            msg.setString("type", NotificationType.FRIEND_REQUEST.name());


            ctx.createProducer().send(queue, msg);
            
        } catch (JMSException  e) {
            throw new RuntimeException("Failed to send JMS message", e);
        }
		
	}

}

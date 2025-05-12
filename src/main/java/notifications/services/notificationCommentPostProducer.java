package notifications.services;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Queue;
import javax.jms.Session;

import notifications.entity.NotificationType;
import notifications.interfaces.services.notificationProducer;

@Stateless(name="commentPostProducer")
@Local(notificationProducer.class)
public class notificationCommentPostProducer extends notificationProducer{

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
		        	
		        	msg.setInt("recipientUserId", recipientUserId);
		            msg.setInt   ("actorUserId",      actorId);
		            msg.setInt("grpId", grpId);
		            msg.setInt   ("postId",           postId);
		            msg.setString("type",   NotificationType.POST_COMMENT.name());
		
		
		            ctx.createProducer().send(queue, msg);
		            
		        } catch (JMSException  e) {
		            throw new RuntimeException("Failed to send JMS message", e);
		        }
		
	}

}

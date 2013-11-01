package basic.jms.app.utils;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

@Named
public class JMSClient {
	@Inject
	private Logger log;
	
	@Resource(lookup="java:jboss/activemq/QueueConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	public void sendText(String queueName, String text) throws JMSException {
		log.fine("JMSClient#send " + text);
		final long start = System.currentTimeMillis();
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			session = connection.createSession(true, -1);
			Queue queue = session.createQueue(queueName);
			TextMessage message = session.createTextMessage();
			message.setText(text);
			message.setLongProperty("timestamp", System.currentTimeMillis());
			MessageProducer producer = session.createProducer(queue);
			producer.send(message);
			session.commit();
		} catch (Exception e) {
			if(session != null){
				try {
					session.rollback();
				} catch (JMSException ex) {
					ex.printStackTrace();
				}
			}
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			if(connection != null){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}	
		log.fine("JMSClient#send[" + (System.currentTimeMillis() - start) + "ms]");
	}
}

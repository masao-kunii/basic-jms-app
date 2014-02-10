package basic.jms.app.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

@Named
public class JMSClient {
	@Inject
	private Logger log;
	
//	@Resource(lookup="java:jboss/activemq/QueueConnectionFactory")
	@Resource(lookup="java:/activemq/QueueConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	public void sendText(String queueName, String text, Map<String, Object> prop) throws JMSException {
		log.fine("JMSClient#send " + text);
		final long start = System.currentTimeMillis();
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(true, -1);
			Queue queue = session.createQueue(queueName);
			TextMessage message = session.createTextMessage();
			message.setText(text);
			message.setLongProperty("timestamp", System.currentTimeMillis());
			Set<Entry<String, Object>> entryset = prop.entrySet();
			for (Entry<String, Object> entry : entryset) {
				message.setObjectProperty(entry.getKey(), entry.getValue());
			}
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
					connection.stop();
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}	
		log.fine("JMSClient#send[" + (System.currentTimeMillis() - start) + "ms]");
	}
	public void sendText(String queueName, String text) throws JMSException {
		log.fine("JMSClient#send " + text);
		final long start = System.currentTimeMillis();
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
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
					connection.stop();
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}	
		log.fine("JMSClient#send[" + (System.currentTimeMillis() - start) + "ms]");
	}
	public Message receive(String queueName, long timeout) throws JMSException {
		log.info("JMSClient#receive from " + queueName);
		final long start = System.currentTimeMillis();
		Connection connection = null;
		Session session = null;
		Message message = null;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(true, -1);
			Queue queue = session.createQueue(queueName);
			MessageConsumer consumer = session.createConsumer(queue);
			message = consumer.receive(timeout);
			
			Boolean forceRollback = null;
			try{
				forceRollback = message.getBooleanProperty("forceRollback");
			}catch(Exception e){
			}
			
			if(forceRollback != null && forceRollback){
				log.info("force rollback");
				session.rollback();
			}else{
				log.info("commit");
				session.commit();
			}
			connection.stop();
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
		log.info("JMSClient#received[" + (System.currentTimeMillis() - start) + "ms]");
		return message;
	}
}

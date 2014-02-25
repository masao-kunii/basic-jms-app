package basic.jms.app.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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
import javax.naming.InitialContext;

@Named
public class JMSClient {
	@Inject
	private Logger log;
	
//	@Resource(lookup="java:jboss/activemq/QueueConnectionFactory")
//	@Resource(lookup="java:/activemq/QueueConnectionFactory")
	@Resource(lookup="java:/jms/cp/QueueConnectionFactory")
	private ConnectionFactory connectionFactory;
	
	public void sendText(String queueName, String text, Map<String, Object> prop) throws JMSException {
		log.info("JMSClient#send " + text);
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
		log.info("JMSClient#send[" + (System.currentTimeMillis() - start) + "ms]");
	}
	public void sendText(String queueName, String text) throws JMSException {
		log.info("JMSClient#send " + text);
		final long start = System.currentTimeMillis();
		Connection connection = null;
		Session session = null;
		try {
			connection = connectionFactory.createConnection();
			log.fine("connection " + connection);
			connection.start();
			session = connection.createSession(true, -1);
			log.fine("session " + session);
			Queue queue = session.createQueue(queueName);

			try{
			InitialContext jndi = new InitialContext();
			Queue jndiqueue = (Queue)jndi.lookup("java:/jms/queues/" + queueName);
			if(jndiqueue != null){
				queue = jndiqueue;
			}
			}catch(Exception e){
			}
			
			log.fine("queue " + queue);
			TextMessage message = session.createTextMessage();
			message.setText(text);
			message.setLongProperty("timestamp", System.currentTimeMillis());
			log.fine("message " + message);
			MessageProducer producer = session.createProducer(queue);
			log.fine("producer " + producer);
			producer.send(message);
			log.fine("sent");
			session.commit();
			log.fine("committed");

			try{
				if(text.matches("b[0-9]*")){
					TimeUnit.SECONDS.sleep(Long.parseLong(text.substring(1)));
				}
			}catch(Exception e){
			}

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
			log.fine("JMSClient#closing the connection");
			if(connection != null){
				try {
					connection.stop();
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			log.fine("JMSClient#closed");
		}	
		try{
			if(text.matches("a[0-9]*")){
				TimeUnit.SECONDS.sleep(Long.parseLong(text.substring(1)));
			}
		}catch(Exception e){
		}
		log.info("JMSClient#sent in [" + (System.currentTimeMillis() - start) + "ms]");
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
		log.info("JMSClient#received in [" + (System.currentTimeMillis() - start) + "ms]");
		return message;
	}
	
}

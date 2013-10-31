package basic.jms.app.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSException;

import basic.jms.app.utils.JMSClient;


/**
 * 
 */
@Stateless
public class MessageSenderBean {
    @Inject
    private Logger log;
    
	@Inject
	private JMSClient client;

	public boolean send(String queueName, String message) {
		log.fine(String.format("MessageSenderBean - message:%s queue:%s", message, queueName));
		try {
			client.sendText(queueName, message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		log.fine("MessageSenderBean - success");
		return true;
	}
}
package basic.jms.app.service.integration;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import basic.jms.app.model.Log;
import basic.jms.app.service.LogBean;
import basic.jms.app.service.MessageReceiverBean;
import basic.jms.app.service.MessageSenderBean;
import basic.jms.app.utils.JMSClient;
import basic.jms.app.utils.Resources;

@RunWith(Arquillian.class)
public class MessageReceiverBeanTest {
	@Inject
	private MessageSenderBean sender;
	@Inject
	private MessageReceiverBean receiver;

	@Deployment(name = "test.jar", managed = true)
	public static JavaArchive createDeployment() {
		return ShrinkWrap
				.create(JavaArchive.class, "test.jar")
				.addPackage("basic.jms.app.service")
				.addPackage("basic.jms.app.model")
				.addPackage("basic.jms.app.utils")
				.addAsResource("basic-jms-app-ds.xml",
						"META-INF/basic-jms-app-ds.xml")
				.addAsResource("META-INF/persistence.xml",
						"META-INF/persistence.xml")
				.addAsResource("META-INF/beans.xml", "META-INF/beans.xml");
	}

	@Test
	public void should_receive_message() throws IOException {
		String queueName = "testQueue";
		sender.send(queueName, "aaa");
		String actual = receiver.receive(queueName, 10000, 7);
		System.out.println(actual);
		assertNotNull(actual);
	}

//	@Test
	public void should_retry() throws IOException {
		String queueName = "testQueue";
		Map<String, Object> prop = new HashMap<String, Object>();
		prop.put("forceRollback", new Boolean(true));
		sender.send(queueName, "aaa", prop);
		String actual = receiver.receive(queueName, 10000, 7);
		System.out.println(actual);
		assertNotNull(actual);
	}
}

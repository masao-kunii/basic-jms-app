package basic.jms.app.mdb;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;

import org.jboss.ejb3.annotation.ResourceAdapter;

import basic.jms.app.service.LogBean;
import javax.inject.Named;

public class TestMDB2 extends AbstractMDB {
	@Inject
	@Named("produceLog")
	private Logger log;

	@Inject
	private LogBean logBean;

	public void onMessage(final Message message) {
		log.info("onMessage " + message);
		logBean.insert("onMessage " + message);
	}
}
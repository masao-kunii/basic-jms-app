package basic.jms.app.mdb;

import javax.jms.Message;
import javax.jms.MessageListener;

public abstract class AbstractMDB implements MessageListener {

	protected void handleException(final Throwable e, final Message receivedMessage) {
		e.printStackTrace();
		throw new RuntimeException(e);
	}
}

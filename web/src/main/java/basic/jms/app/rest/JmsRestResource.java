package basic.jms.app.rest;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import basic.jms.app.service.MessageReceiverBean;
import basic.jms.app.service.MessageSenderBean;

public class JmsRestResource implements IJmsRestResource{
	@Inject
	private Logger log;
	
	@Inject
	MessageSenderBean senderBean;

	@Inject
	MessageReceiverBean receiverBean;

	@Override
	public Response send(@PathParam("queueName") String queueName,
			@PathParam("message") String message) {
		log.fine(String.format("send %s to %s", queueName, message));
		senderBean.send(queueName, message);
		return Response.ok().build();
	}

	@Override
	public Response sendlargemessage(@PathParam("queueName") String queueName,
			@FormParam("message") String message){
		log.fine(String.format("sendlargemessage %s to %s", queueName, message));
		senderBean.send(queueName, message);
		return Response.ok().build();
	}

	public Response send(@PathParam("queueName") String queueName,
			@PathParam("timeout") long timeout, @PathParam("retry") int retry){
		log.fine(String.format("receive message from %s", queueName));
		String string = receiverBean.receive(queueName, timeout, retry);
		return Response.ok().build();
	}

}
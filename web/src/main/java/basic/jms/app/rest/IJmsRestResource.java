package basic.jms.app.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/jms")
public interface IJmsRestResource {

	@GET
	@Path("/send/{queueName}/{message}")
	@Produces("application/json")
	public Response send(@PathParam("queueName") String queueName,
			@PathParam("message") String message);

	@POST
	@Path("/sendlargemessage/{queueName}")
//	@Produces("application/json")
	public Response sendlargemessage(@PathParam("queueName") String queueName,
			@FormParam("message") String message);

	@GET
	@Path("/send/{queueName}/{timeout}/{retry}")
	@Produces("application/json")
	public Response send(@PathParam("queueName") String queueName,
			@PathParam("timeout") long timeout, @PathParam("retry") int retry);
}
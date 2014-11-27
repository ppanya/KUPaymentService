package ku.payment.resource;

import java.net.URI;
import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ku.payment.entity.User;
import ku.payment.handler.UserHandler;

@Path("/user")
@Singleton
public class UserResource {

	@Context
	UriInfo uriInfo;
	private UserHandler handler;

	private final Response NOT_FOUND = Response.status(Status.NOT_FOUND)
			.build();
	private final Response OK = Response.status(Status.OK).build();
	private final Response CONFLICT = Response.status(Status.CONFLICT).build();
	private final Response BAD_REQUEST = Response.status(Status.BAD_REQUEST)
			.build();
	private final Response NOT_MODIFIED = Response.status(Status.NOT_MODIFIED)
			.build();
	private final Response PRECONDITION = Response.status(
			Status.PRECONDITION_FAILED).build();

	private final Response NO_CONTENT = Response.status(Status.NO_CONTENT)
			.build();

	public UserResource() {
		this.handler = new UserHandler();
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllUser(@HeaderParam("Accept") String accept)
			throws JSONException {

		List<User> u_list = handler.getAllUser();

		if (accept.equals("application/json")) {
			JSONArray jsonArray = new JSONArray();

			for (int i = 0; i < u_list.size(); i++) {
				JSONObject json = new JSONObject();
				JSONObject pay = new JSONObject(u_list.get(i));
				json.put("user", pay);
				jsonArray.put(json);
			}

			return Response.ok().entity(jsonArray.toString()).build();
		}

		GenericEntity<List<User>> list = convertToXML(u_list);

		return Response.ok(list).build();
	}

	@GET
	@Path("/{id: [1-9]\\d*}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getUserById(@HeaderParam("Accept") String accept,
			@PathParam("id") long id) throws JSONException {

		User payment = handler.getUserByID(id);

		if (payment != null) {

			if (accept.equals("application/json")) {
				JSONObject json = new JSONObject(payment);
				return Response.ok(json.toString()).build();
			}

			return Response.ok(payment).build();
		}

		return NOT_FOUND;
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createUser(@HeaderParam("Content-Type") String ctype,
			JAXBElement<User> element, @Context UriInfo uriInfo) {

		User user = null;

		if (ctype.equals("application/xml")) {
			user = element.getValue();
		}

		if (handler.getUserByID(user.getId()) != null) {
			return CONFLICT;
		}

		if (handler.createUser(user)) {
			URI uri = uriInfo.getAbsolutePathBuilder()
					.path(user.getId() + "").build();
			return Response.created(uri).build();
		}
		return BAD_REQUEST;
	}

	@PUT
	@Path("/{id: [1-9]\\d*}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateUser(JAXBElement<User> element,
			@PathParam("id") long id, @Context UriInfo uriInfo,
			@Context Request req) {

		User user = handler.getUserByID(id);

		if (user != null) {

			User update = element.getValue();
			update.setId(id);

			handler.updateUser(update);

			URI uri = uriInfo.getAbsolutePath();
			return Response.ok(uri + "").build();
		}

		return NOT_FOUND;
	}


	/**
	 * Convert List of Contact to XML
	 * 
	 * @param contact
	 *            List of Contact
	 * @return GenericEntity List of Contact
	 */
	private GenericEntity<List<User>> convertToXML(List<User> payment) {
		return new GenericEntity<List<User>>(payment) {
		};
	}

}

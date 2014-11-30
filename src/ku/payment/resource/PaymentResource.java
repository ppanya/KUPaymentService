package ku.payment.resource;

import java.net.URI;

import java.util.List;
import java.util.Set;

import org.json.*;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import ku.payment.entity.*;
import ku.payment.handler.PaymentHandler;
import ku.payment.handler.UserHandler;

@Path("/payment")
@Singleton
public class PaymentResource {

	@Context
	UriInfo uriInfo;

	private PaymentHandler handler;
	private UserHandler user_handler;
	private final String RESOURCE_NAME = "payment/";
	private final String PENDING_STATUS = "pending";
	private final String SUCCESS_STATUS = "success";
	private final String RECIPIENT = "recipient";
	private final String SENDER = "sender";
	private final String BOTH = "both";
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

	private final Response FORBIDDEN = Response.status(Status.FORBIDDEN)
			.build();

	public PaymentResource() {
		this.handler = new PaymentHandler();
		this.user_handler = new UserHandler();
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@RolesAllowed("admin")
	public Response getAllPayment(@HeaderParam("Accept") String accept)
			throws JSONException {

		List<PaymentTransaction> p_list = handler.getAllPayment();

		if (accept.equals("application/json")) {
			JSONArray jsonArray = new JSONArray();

			for (int i = 0; i < p_list.size(); i++) {
				JSONObject json = new JSONObject();
				JSONObject pay = new JSONObject(p_list.get(i));
				json.put("payment", pay);
				jsonArray.put(json);
			}

			return Response.ok().entity(jsonArray.toString()).header("Access-Control-Allow-Origin", "*").build();
		}

		GenericEntity<List<PaymentTransaction>> list = convertToXML(p_list);

		return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
	}

	@GET
	@Path("{id: [1-9]\\d*}")
	@RolesAllowed({ "admin", "user" })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPaymentById(@HeaderParam("Accept") String accept,
			@PathParam("id") long id) throws JSONException {

		PaymentTransaction payment = handler.getPaymentByID(id);

		if (payment != null) {

			if (accept.equals("application/json")) {
				JSONObject json = new JSONObject(payment);
				return Response.ok(json.toString()).build();
			}

			return Response.ok(payment).header("Access-Control-Allow-Origin", "*").build();
		}

		return NOT_FOUND;
	}

	@GET
	@Path("user/{username}")
	@PermitAll
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPaymentByUser(@HeaderParam("Accept") String accept,
<<<<<<< HEAD
			@PathParam("username") String email,
			@Context HttpHeaders httpHeaders) throws JSONException {

		String header_email = extractUsernameFromHeaders(httpHeaders)
				.toLowerCase();
		if (header_email.equals(email.toLowerCase())) {
			long userID = user_handler.getUserIDFromEmail(email);
=======
			@PathParam("username") String username,
			@Context HttpHeaders httpHeaders) throws JSONException {

		String header_username = extractUsernameFromHeaders(httpHeaders)
				.toLowerCase();
		if (header_username.equals(username.toLowerCase())) {
			long userID = user_handler.getUserIDFromUsername(username);
>>>>>>> 9ab378beb5128b497e9bdc3707d9f561e592ef0d
			List<PaymentTransaction> p_list = handler.getPaymentByUser(userID);

			if (p_list.size() == 0)
				return NOT_FOUND;

			if (accept.equals("application/json")) {
				JSONArray jsonArray = new JSONArray();

				for (int i = 0; i < p_list.size(); i++) {
					JSONObject json = new JSONObject();
					JSONObject pay = new JSONObject(p_list.get(i));
					json.put("payment", pay);
					jsonArray.put(json);
				}

				return Response.ok().entity(jsonArray.toString()).header("Access-Control-Allow-Origin", "*").build();
			}
			GenericEntity<List<PaymentTransaction>> list = convertToXML(p_list);

			return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
		}
		return FORBIDDEN;
	}

	@POST
	@PermitAll
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createPayment(@HeaderParam("Content-Type") String ctype,
			JAXBElement<PaymentTransaction> element, @Context UriInfo uriInfo) {

		PaymentTransaction payment = element.getValue();

		if (payment != null) {
			if (handler.getPaymentByID(payment.getId()) != null) {
				return CONFLICT;
			}

			if (handler.sendPayment(payment)) {
				URI uri = uriInfo.getAbsolutePathBuilder()
						.path(payment.getId() + "").build();
<<<<<<< HEAD
				return Response.created(uri).header("Access-Control-Allow-Origin", "*").build();
=======
				return Response.created(uri).build();
>>>>>>> 9ab378beb5128b497e9bdc3707d9f561e592ef0d
			}
		}
		return BAD_REQUEST;
	}

	@GET
	@Path("login")
	@PermitAll
	public Response login(@Context UriInfo uriInfo, @Context HttpHeaders headers) {
<<<<<<< HEAD

		String email = extractUsernameFromHeaders(headers);

		User user = user_handler.getUserByEmail(email);

		if (user != null) {
			long id = user.getId();
			URI uri = uriInfo.getBaseUri();
			return Response.ok(uri + RESOURCE_NAME + id).header("Access-Control-Allow-Origin", "*").build();
=======

		String username = extractUsernameFromHeaders(headers);

		User user = user_handler.getUserByUsername(username);

		if (user != null) {
			long id = user.getId();
			URI uri = uriInfo.getBaseUri();
			return Response.ok(uri + RESOURCE_NAME + id).build();
>>>>>>> 9ab378beb5128b497e9bdc3707d9f561e592ef0d
		}
		
		return NOT_FOUND;
	}

	@PUT
	@Path("accept/{id: [1-9]\\d*}")
	@PermitAll
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response acceptPayment(@PathParam("id") long id,
			@Context UriInfo uriInfo, @Context Request req,
			@Context HttpHeaders httpHeaders) {

		if (isUserRelatetoPayment(httpHeaders, id, RECIPIENT)) {

			PaymentTransaction update = handler.getPaymentByID(id);
			if (!update.getStatus().toLowerCase().equals(PENDING_STATUS))
				return CONFLICT;

			handler.acceptPayment(update);

			URI uri = uriInfo.getBaseUri();
<<<<<<< HEAD
			return Response.ok(uri + RESOURCE_NAME + id).header("Access-Control-Allow-Origin", "*").build();
=======
			return Response.ok(uri + RESOURCE_NAME + id).build();
>>>>>>> 9ab378beb5128b497e9bdc3707d9f561e592ef0d
		}

		return NOT_FOUND;
	}

	@PUT
	@Path("reverse/{id: [1-9]\\d*}")
	@PermitAll
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response reversePayment(@PathParam("id") long id,
			@Context UriInfo uriInfo, @Context Request req,
			@Context HttpHeaders httpHeaders) {

		if (isUserRelatetoPayment(httpHeaders, id, BOTH)) {

			PaymentTransaction update = handler.getPaymentByID(id);
			if (!update.getStatus().toLowerCase().equals(SUCCESS_STATUS))
				return CONFLICT;
			handler.reversePayment(update);

			URI uri = uriInfo.getBaseUri();
<<<<<<< HEAD
			return Response.ok(uri + RESOURCE_NAME + id).header("Access-Control-Allow-Origin", "*").build();
=======
			return Response.ok(uri + RESOURCE_NAME + id).build();
>>>>>>> 9ab378beb5128b497e9bdc3707d9f561e592ef0d
		}

		return NOT_FOUND;
	}

	public String extractUsernameFromHeaders(HttpHeaders headers) {
		String username = null;
		String[] temp = headers.getHeaderString("Authorization").split(",");
		for (String s : temp) {
			if (s.contains("username")) {
				username = s.split("=")[1];
				break;
			}
		}
		return username.substring(1, username.length() - 1);
	}

	public boolean isSameUser(HttpHeaders headers, long userID) {
		String email = extractUsernameFromHeaders(headers);
		long auth_userID = user_handler.getUserIDFromEmail(email);
		if (auth_userID == -1)
			return false;
		return userID == auth_userID;
	}

	public boolean isUserRelatetoPayment(HttpHeaders headers, long paymentID,
			String condition) {
		String email = extractUsernameFromHeaders(headers);
		PaymentTransaction payment = handler.getPaymentByID(paymentID);
		if (payment == null)
			return false;
		long userID = user_handler.getUserIDFromEmail(email);
		if (userID == -1)
			return false;
		condition = condition.toLowerCase();
		long recID = payment.getRecipientID();
		long senderID = payment.getSenderID();

		if (condition.equals(RECIPIENT))
			return recID == userID;
		else if (condition.equals(SENDER))
			return senderID == userID;
		else
			return recID == userID || senderID == userID;
	}

	/**
	 * Convert List of Contact to XML
	 * 
	 * @param contact
	 *            List of Contact
	 * @return GenericEntity List of Contact
	 */
	private GenericEntity<List<PaymentTransaction>> convertToXML(
			List<PaymentTransaction> payment) {
		return new GenericEntity<List<PaymentTransaction>>(payment) {
		};
	}

}
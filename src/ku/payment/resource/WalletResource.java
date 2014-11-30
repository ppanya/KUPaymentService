package ku.payment.resource;

import java.net.URI;
import java.util.List;

import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import ku.payment.entity.Wallet;
import ku.payment.entity.WalletTransaction;
import ku.payment.handler.UserHandler;
import ku.payment.handler.WalletHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/wallet")
@Singleton
public class WalletResource {

	@Context
	UriInfo uriInfo;

	private WalletHandler handler;
	private UserHandler user_handler;
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

	public WalletResource() {
		this.handler = new WalletHandler();
		this.user_handler = new UserHandler();
	}

	@GET
	@RolesAllowed({ "admin" })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllWallet(@HeaderParam("Accept") String accept)
			throws JSONException {

		List<Wallet> p_list = handler.getAllWallet();

		if (accept.equals("application/json")) {
			JSONArray jsonArray = new JSONArray();

			for (int i = 0; i < p_list.size(); i++) {
				JSONObject json = new JSONObject();
				JSONObject pay = new JSONObject(p_list.get(i));
				json.put("wallet", pay);
				jsonArray.put(json);
			}

			return Response.ok().entity(jsonArray.toString()).header("Access-Control-Allow-Origin", "*").build();
		}

		GenericEntity<List<Wallet>> list = convertToXML(p_list);

		return Response.ok(list).header("Access-Control-Allow-Origin", "*").build();
	}

	@GET
	@Path("/{id: [1-9]\\d*}")
	@RolesAllowed({ "user" })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getWalletById(@HeaderParam("Accept") String accept,
			@PathParam("id") long id, @Context HttpHeaders headers)
			throws JSONException {

		if (isSameUser(headers, id)) {
			Wallet wallet = handler.getWalletByID(id);
			if (accept.equals("application/json")) {
				JSONObject json = new JSONObject(wallet);
				return Response.ok(json.toString()).build();
			}

			return Response.ok(wallet).header("Access-Control-Allow-Origin", "*").build();
		}

		return NOT_FOUND;
	}

	@GET
	@Path("/user/{id: [1-9]\\d*}")
	@RolesAllowed({ "user" })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getWalletByUser(@HeaderParam("Accept") String accept,
			@PathParam("id") long userID, @Context HttpHeaders headers)
			throws JSONException {

		if (isSameUser(headers, userID)) {
			Wallet wallet = handler.getWalletByuserID(userID);
			if (accept.equals("application/json")) {
				JSONObject json = new JSONObject(wallet);
				return Response.ok(json.toString()).build();
			}

			return Response.ok(wallet).header("Access-Control-Allow-Origin", "*").build();
		}

		return NOT_FOUND;
	}

	@PUT
	@RolesAllowed({ "user" })
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response moneyTransfer(@HeaderParam("Content-Type") String ctype,
			JAXBElement<WalletTransaction> element, @Context UriInfo uriInfo,
			@Context HttpHeaders headers) {

		WalletTransaction transaction = element.getValue();
		long walletID = transaction.getWalletID();
		long userID = handler.getUserIdByWalletID(walletID);
		if (userID != -1) {
			if (isSameUser(headers, userID)) {
				double amount = transaction.getAmount();
				String transType = transaction.getTransactionType()
						.toLowerCase();
				if (transType.equals("deposit")) {
					handler.addMoney(walletID, amount);
				} else if (transType.equals("withdraw"))
					if (!handler.deductMoney(walletID, amount))
						return BAD_REQUEST; // not enough money
				return Response.ok().build();
			}
		}
		return BAD_REQUEST;
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
		long request_user_id = user_handler.getUserIDFromEmail(email);
		if (request_user_id == -1)
			return false;
		return request_user_id == userID;
	}

	/**
	 * Convert List of Contact to XML
	 * 
	 * @param contact
	 *            List of Contact
	 * @return GenericEntity List of Contact
	 */
	private GenericEntity<List<Wallet>> convertToXML(List<Wallet> payment) {
		return new GenericEntity<List<Wallet>>(payment) {
		};
	}
}

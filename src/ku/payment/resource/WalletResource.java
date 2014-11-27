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

import ku.payment.entity.Wallet;
import ku.payment.entity.WalletTransaction;
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
		
	public WalletResource(WalletHandler handler){
		this.handler = handler;
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllWallet(@HeaderParam("Accept") String accept)
			throws JSONException {

		List<Wallet> p_list = handler.getAllWallet();

		if (accept.equals("application/json")) {
			JSONArray jsonArray = new JSONArray();

			for (int i = 0; i < p_list.size(); i++) {
				JSONObject json = new JSONObject();
				JSONObject pay = new JSONObject(p_list.get(i));
				json.put("payment", pay);
				jsonArray.put(json);
			}

			return Response.ok().entity(jsonArray.toString()).build();
		}

		GenericEntity<List<Wallet>> list = convertToXML(p_list);

		return Response.ok(list).build();
	}

	@GET
	@Path("/{id: [1-9]\\d*}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getWalletById(@HeaderParam("Accept") String accept,
			@PathParam("id") long id) throws JSONException {

		Wallet wallet = handler.getWalletByID(id);

		if (wallet != null) {

			if (accept.equals("application/json")) {
				JSONObject json = new JSONObject(wallet);
				return Response.ok(json.toString()).build();
			}

			return Response.ok(wallet).build();
		}
	

		return NOT_FOUND;
	}

	@GET
	@Path("/user/{id: [1-9]\\d*}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getWalletByUser(@HeaderParam("Accept") String accept,
			@PathParam("id") long userID) throws JSONException {

		Wallet wallet = handler.getWalletByuserID(userID);
		

		if (wallet != null) {

			if (accept.equals("application/json")) {
				JSONObject json = new JSONObject(wallet);
				return Response.ok(json.toString()).build();
			}

			return Response.ok(wallet).build();
		}
		
		return NOT_FOUND;
	}

	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response moneyTransfer(@HeaderParam("Content-Type") String ctype,
			JAXBElement<WalletTransaction> element ,@Context UriInfo uriInfo) {
		
		WalletTransaction transaction = null;
		
		if(ctype.equals("application/xml")) {
			transaction = element.getValue();
		}
	
		if (handler.getWalletByID(transaction.getWalletID()) != null) {
			return CONFLICT;
		}
		
		long walletID = transaction.getWalletID();
		if(handler.getWalletByID(walletID) != null) {
			double amount = transaction.getAmount();
			String transType = transaction.getTransactionType().toLowerCase();
			if(transType=="deposit")
				handler.depositMoney(walletID, amount);
			else if(transType=="withdraw")
				handler.withdrawMoney(walletID, amount);
			URI uri = uriInfo.getAbsolutePathBuilder()
					.path(walletID + "").build();
			return Response.created(uri).build();
		}
		return BAD_REQUEST;
	}

	@PUT
	@Path("accept/{id: [1-9]\\d*}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response acceptPayment(
			JAXBElement<Wallet> element, @PathParam("id") long id,
			@Context UriInfo uriInfo, @Context Request req) {

		Wallet payment = handler.getPaymentByID(id);

		if (payment != null) {

			Wallet update = element.getValue();
			update.setId(id);

			handler.acceptPayment(update);

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
	private GenericEntity<List<Wallet>> convertToXML(
			List<Wallet> payment) {
		return new GenericEntity<List<Wallet>>(payment) {
		};
	}
}

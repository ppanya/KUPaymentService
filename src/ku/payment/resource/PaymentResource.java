package ku.payment.resource;

import java.net.URI;

import java.util.List;

import org.json.*;

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

import ku.payment.entity.*;
import ku.payment.service.DaoFactory;
import ku.payment.service.PaymentDao;
import ku.payment.service.UserDao;
import ku.payment.service.WalletDao;
import ku.payment.service.WalletTransactionDao;

@Path("/kupayment")
@Singleton
public class PaymentResource {

	@Context
	UriInfo uriInfo;

	private PaymentDao paymentdao;
	private UserDao userdao;
	private WalletDao walletdao;
	private WalletTransactionDao walletTransacdao;
	private DaoFactory factory;

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

	public PaymentResource() {
		this.factory = DaoFactory.getInstance();
		paymentdao = factory.getPaymentDao();
		userdao = factory.getUserDao();
		walletdao = factory.getWalletDao();
		walletTransacdao = factory.getWalletTransactionDao();
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllPayment(@HeaderParam("Accept") String accept)
			throws JSONException {

		List<PaymentTransaction> p_list = paymentdao.findAll();

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

		GenericEntity<List<PaymentTransaction>> list = convertToXML(p_list);

		return Response.ok(list).build();
	}

	@GET
	@Path("{id: [1-9]\\d*}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPaymentById(@HeaderParam("Accept") String accept,
			@PathParam("id") long id) throws JSONException {

		PaymentTransaction payment = paymentdao.find(id);

		if (payment != null) {

			if (accept.equals("application/json")) {
				JSONObject json = new JSONObject(payment);
				return Response.ok(json.toString()).build();
			}

			return Response.ok(payment).build();
		}

		return NOT_FOUND;
	}

	@GET
	@Path("user/{id: [1-9]\\d*}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPaymentByUser(@HeaderParam("Accept") String accept,
			@PathParam("id") long id) throws JSONException {

		List<PaymentTransaction> p_list = paymentdao.findByUser(id);
		

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
		GenericEntity<List<PaymentTransaction>> list = convertToXML(paymentdao
				.findByUser(id));
		
		return Response.ok(list).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createPayment(@HeaderParam("Content-Type") String ctype,
			JAXBElement<PaymentTransaction> element ,@Context UriInfo uriInfo) {
		
		PaymentTransaction payment = null;
		
		if(ctype.equals("application/xml")) {
			payment = element.getValue();
		}
		
		if(ctype.equals("application/json")) {
			
		}

		if (paymentdao.find(payment.getId()) != null) {
			return CONFLICT;
		}

		if (paymentdao.save(payment)) {
			URI uri = uriInfo.getAbsolutePathBuilder()
					.path(payment.getId() + "").build();
			return Response.created(uri).build();
		}
		return BAD_REQUEST;
	}

	@PUT
	@Path("{id: [1-9]\\d*}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateStatusPayment(
			JAXBElement<PaymentTransaction> element, @PathParam("id") long id,
			@Context UriInfo uriInfo, @Context Request req) {

		PaymentTransaction payment = paymentdao.find(id);

		if (payment != null) {

			PaymentTransaction update = element.getValue();
			update.setId(id);

			paymentdao.update(update);

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
	private GenericEntity<List<PaymentTransaction>> convertToXML(
			List<PaymentTransaction> payment) {
		return new GenericEntity<List<PaymentTransaction>>(payment) {
		};
	}

}

package ku.payment.resource;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class PaymentApplication extends ResourceConfig {
<<<<<<< HEAD
	
=======

>>>>>>> 9ab378beb5128b497e9bdc3707d9f561e592ef0d
	public PaymentApplication() {
		super(PaymentResource.class, UserResource.class, WalletResource.class);
		register(RolesAllowedDynamicFeature.class);
	}
}

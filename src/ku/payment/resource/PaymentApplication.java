package ku.payment.resource;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class PaymentApplication extends ResourceConfig {

	public PaymentApplication() {
		super(PaymentResource.class, UserResource.class, WalletResource.class);
		register(RolesAllowedDynamicFeature.class);
	}
}

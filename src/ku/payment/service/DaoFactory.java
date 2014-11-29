package ku.payment.service;

import ku.payment.service.jpa.JpaDaoFactory;


/**
 * DaoFactory defines methods for obtaining instance of data access objects.
 * To create DAO you first get an instance of a concrete factory by invoking
 * <p>
 * <tt>DaoFactory factory = DaoFactory.getInstance(); </tt>
 * <p>
 * Then use the <tt>factory</tt> object to get instances of actual DAO.
 * This factory is an abstract class.  There are concrete subclasses for
 * each persistence mechanism.  You can add your own factory by subclassing
 * this factory.
 * 
 */
public abstract class DaoFactory {
	// singleton instance of this factory
	private static DaoFactory factory;
	
	/** this class shouldn't be instantiated, but constructor must be visible to subclasses. */
	protected DaoFactory() {
		// nothing to do
	}
	
	/**
	 * Get a singleton instance of the DaoFactory.
	 * @return instance of a concrete DaoFactory
	 */
	public static DaoFactory getInstance() {
		if(factory==null) setFactory(new JpaDaoFactory());
		return factory;
	}
	
	public abstract PaymentDao getPaymentDao();

	public abstract UserDao getUserDao();

	public abstract WalletDao getWalletDao();

	public abstract WalletTransactionDao getWalletTransactionDao();

	
	/**
	 * Shutdown all persistence services.
	 * This method gives the persistence framework a chance to
	 * gracefully save data and close databases before the
	 * application terminates.
	 */
	public abstract void shutdown();
	
	/**
	 * Load File XML to the DAOFactory
	 * By Unmarshal ( XML To Java )
	 */
	public abstract void loadFile();
	
	/**
	 * Set Factory of DaoFactory
	 * @param factory factory which to use
	 */
	public static void setFactory(DaoFactory factory){
		DaoFactory.factory = factory;
	}
}

package ku.payment.service.jpa;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ku.payment.service.DaoFactory;
import ku.payment.service.PaymentDao;
import ku.payment.service.UserDao;
import ku.payment.service.WalletDao;
import ku.payment.service.WalletTransactionDao;

public class JpaDaoFactory extends DaoFactory {
	private static final String PERSISTENCE_UNIT = "paymentservice";
	/** instance of the entity DAO */

	private PaymentDao paymentDao;
	private UserDao userDao;
	private WalletDao walletDao;
	private WalletTransactionDao walletTransactionDao;

	private final EntityManagerFactory emf;
	private EntityManager em;
//	private EntityManager paymentEm;
//	private EntityManager userEm;
//	private EntityManager walletEm;
//	private EntityManager walletTransEm;
	private static Logger logger;

	static {
		logger = Logger.getLogger(JpaDaoFactory.class.getName());
	}

	public JpaDaoFactory() {
		emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		em = emf.createEntityManager();
		paymentDao = new JpaPaymentDao(em);
		userDao = new JpaUserDao(em);
		walletDao = new JpaWalletDao(em);
		walletTransactionDao = new JpaWalletTransactionDao(em);
//		paymentEm = emf.createEntityManager();
//		userEm = emf.createEntityManager();
//		walletEm = emf.createEntityManager();
//		walletTransEm = emf.createEntityManager();
//		paymentDao = new JpaPaymentDao(paymentEm);
//		userDao = new JpaUserDao(userEm);
//		walletDao = new JpaWalletDao(walletEm);
//		walletTransactionDao = new JpaWalletTransactionDao(walletTransEm);
	}

	public PaymentDao getPaymentDao() {
		return paymentDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public WalletDao getWalletDao() {
		return walletDao;
	}

	public WalletTransactionDao getWalletTransactionDao() {
		return walletTransactionDao;
	}

	/**
	 * @see contact.service.DaoFactory#shutdown()
	 */
	@Override
	public void shutdown() {
		try {
			if( em != null && em.isOpen()) 
				em.close();
//			if (paymentEm != null && paymentEm.isOpen())
//				paymentEm.close();
//			if (userEm != null && userEm.isOpen())
//				userEm.close();
//			if (walletEm != null && walletEm.isOpen())
//				walletEm.close();
//			if (walletTransEm != null && walletTransEm.isOpen())
//				walletTransEm.close();
			if (emf != null && emf.isOpen())
				emf.close();
		} catch (IllegalStateException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
		}
	}

	/**
	 * @see contact.service.DaoFactory#loadFile()
	 */
	@Override
	public void loadFile() {
		// TODO Auto-generated method stub

	}
}
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
			if (em != null && em.isOpen())
				em.close();
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
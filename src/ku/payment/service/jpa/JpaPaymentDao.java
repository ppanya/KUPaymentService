package ku.payment.service.jpa;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import ku.payment.entity.PaymentTransaction;
import ku.payment.service.PaymentDao;

public class JpaPaymentDao implements PaymentDao {
	
	private final EntityManager em;
	
	public JpaPaymentDao (EntityManager em) {
		this.em = em;
	}
	
	@Override
	public PaymentTransaction find(long id) {
		return em.find(PaymentTransaction.class, id);
	}

	@Override
	public List<PaymentTransaction> findAll() {
		Query query = em.createQuery("SELECT c FROM PaymentTransaction c");
		return query.getResultList();
	}
	
	@Override
	public List<PaymentTransaction> findByUser(long userID){
		Query query = em.createQuery("SELECT c FROM PaymentTransaction c WHERE c.recipientID = :userID OR c.senderID = :userID");
		query.setParameter("userID", userID);
		return query.getResultList();
	}

	@Override
	public boolean delete(long id) {
		PaymentTransaction payment = find(id);
		if (payment == null)
			throw new IllegalArgumentException("Can't find payment ID : " + id);
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(payment);
			tx.commit();
			return true;
		} catch (EntityExistsException ex) {
			Logger.getLogger(this.getClass().getName())
					.warning(ex.getMessage());
			if (tx.isActive())
				try {
					tx.rollback();
				} catch (Exception e) {
				}
			return false;
		}
	}

	@Override
	public boolean save(PaymentTransaction payment) {
		if (payment == null)
			throw new IllegalArgumentException("Can't save a null payment");
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(payment);
			tx.commit();
			return true;
		} catch (EntityExistsException ex) {
			Logger.getLogger(this.getClass().getName())
					.warning(ex.getMessage());
			if (tx.isActive())
				try {
					tx.rollback();
				} catch (Exception e) {
				}
			return false;
		}
	}

	@Override
	public boolean update(PaymentTransaction update) {
		PaymentTransaction payment = find(update.getId());
		if (payment == null || update == null)
			throw new IllegalArgumentException("Can't find payment ID : "
					+ update.getId());
		payment.applyUpdate(update);
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			// payment.applyUpdate(update);
			em.merge(payment);
			tx.commit();
			return true;
		} catch (EntityExistsException ex) {
			Logger.getLogger(this.getClass().getName())
					.warning(ex.getMessage());
			if (tx.isActive())
				try {
					tx.rollback();
				} catch (Exception e) {
				}
			return false;
		}
	}

}

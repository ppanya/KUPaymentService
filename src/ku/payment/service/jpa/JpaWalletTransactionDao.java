package ku.payment.service.jpa;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import ku.payment.entity.WalletTransaction;
import ku.payment.service.WalletTransactionDao;

public class JpaWalletTransactionDao implements WalletTransactionDao {
	private final EntityManager em;
	
	public JpaWalletTransactionDao(EntityManager em){
		this.em = em;
	}
	
	@Override
	public WalletTransaction find(long id) {
		return em.find(WalletTransaction.class, id);
	}

	@Override
	public List<WalletTransaction> findAll() {
		Query query = em.createQuery("SELECT c FROM WalletTransaction c");
		return query.getResultList();
	}

	@Override
	public boolean delete(long id) {
		WalletTransaction walletTransaction = find(id);
		if (walletTransaction == null)
			throw new IllegalArgumentException("Can't find walletTransaction ID : " + id);
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(walletTransaction);
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
	public boolean save(WalletTransaction walletTransaction) {
		if (walletTransaction == null)
			throw new IllegalArgumentException("Can't save a null walletTransaction");
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(walletTransaction);
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
	public boolean update(WalletTransaction update) {
		WalletTransaction walletTransaction = find(update.getId());
		if (walletTransaction == null || update == null)
			throw new IllegalArgumentException("Can't find walletTransaction ID : "
					+ update.getId());
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			// walletTransaction.applyUpdate(update);
			em.merge(walletTransaction);
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

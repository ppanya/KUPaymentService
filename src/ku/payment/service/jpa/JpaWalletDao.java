package ku.payment.service.jpa;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import ku.payment.entity.Wallet;
import ku.payment.service.WalletDao;

public class JpaWalletDao implements WalletDao {

	private final EntityManager em;
	
	public JpaWalletDao (EntityManager em) {
		this.em = em;
	}

	@Override
	public Wallet find(long id) {
		return em.find(Wallet.class, id);
	}

	@Override
	public List<Wallet> findAll() {
		Query query = em.createQuery("SELECT c FROM Wallet c");
		return query.getResultList();
	}

	@Override
	public boolean delete(long id) {
		Wallet wallet = find(id);
		if (wallet == null)
			throw new IllegalArgumentException("Can't find wallet ID : " + id);
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(wallet);
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
	public boolean save(Wallet wallet) {
		if (wallet == null)
			throw new IllegalArgumentException("Can't save a null wallet");
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(wallet);
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
	public boolean update(Wallet update) {
		Wallet wallet = find(update.getId());
		if (wallet == null || update == null)
			throw new IllegalArgumentException("Can't find wallet ID : "
					+ update.getId());
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			// wallet.applyUpdate(update);
			em.merge(wallet);
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

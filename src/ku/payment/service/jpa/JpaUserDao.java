package ku.payment.service.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import ku.payment.entity.User;
import ku.payment.service.UserDao;


public class JpaUserDao implements UserDao {
	
	private final EntityManager em;
	
	public JpaUserDao (EntityManager em) {
		this.em = em;
	}
	
	@Override
	public User find(long id) {
		return em.find(User.class, id);
	}

	@Override
	public List<User> findAll() {
		Query query = em.createQuery("SELECT c FROM User c");
		return query.getResultList();
//		return new ArrayList<User>();
	}

	@Override
	public boolean delete(long id) {
		User user = find(id);
		if (user == null)
			throw new IllegalArgumentException("Can't find user ID : " + id);
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(user);
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
	public boolean save(User user) {
		if (user == null)
			throw new IllegalArgumentException("Can't save a null user");
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(user);
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
	public boolean update(User update) {
		User user = find(update.getId());
		if (user == null || update == null)
			throw new IllegalArgumentException("Can't find user ID : "
					+ update.getId());
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			// user.applyUpdate(update);
			em.merge(user);
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

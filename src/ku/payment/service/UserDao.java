package ku.payment.service;

import java.util.List;

import ku.payment.entity.User;

public interface UserDao {

	/** Find a contact by ID in contacts.
	 * @param the id of contact to find
	 * @return the matching contact or null if the id is not found
	 */
	public abstract User find(long id);

	public abstract List<User> findAll();

	/**
	 * Delete a saved contact.
	 * @param id the id of contact to delete
	 * @return true if contact is deleted, false otherwise.
	 */
	public abstract boolean delete(long id);

	
	public abstract List<String> findAllUsername();
	
	
	public abstract long findIDFromUsername(String username);
	
	/**
	 * Save or replace a contact.
	 * If the contact.id is 0 then it is assumed to be a
	 * new (not saved) contact.  In this case a unique id
	 * is assigned to the contact.  
	 * If the contact.id is not zero and the contact already
	 * exists in saved contacts, the old contact is replaced.
	 * @param contact the contact to save or replace.
	 * @return true if saved successfully
	 */
	public abstract boolean save(User user);

	/**
	 * Update a Contact.  Only the non-null fields of the
	 * update are applied to the contact.
	 * @param update update info for the contact.
	 * @return true if the update is applied successfully.
	 */
	public abstract boolean update(User update);
	
}

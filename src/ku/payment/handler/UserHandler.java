package ku.payment.handler;

import java.util.List;

import ku.payment.entity.User;
import ku.payment.entity.Wallet;
import ku.payment.service.DaoFactory;
import ku.payment.service.UserDao;

public class UserHandler {

	private UserDao userDao;
	private WalletHandler w_handler;

	public UserHandler() {
		this.userDao = DaoFactory.getInstance().getUserDao();
		this.w_handler = new WalletHandler();
	}

	public boolean createUser(User user) {
		if (!isUserExist(user))
			return false;
		if (userDao.save(user)) {
			return w_handler.createWallet(user.getId());
		}
		return false;
	}

	public boolean deleteUser(User user) {
		if (!isUserExist(user))
			return false;
		Wallet wallet = w_handler.getWalletByuserID(user.getId());
		
		if( wallet != null) {
			w_handler.deleteWallet(wallet.getId());
			return userDao.delete(user.getId());
		}
		return false;
	}

	public boolean updateUser(User user) {
		if (!isUserExist(user))
			return false;
		return userDao.update(user);
	}
	
	public List<User> getAllUser() {
		return userDao.findAll();
	}
	
	public User getUserByID(long userID) {
		return userDao.find(userID);
	}
	
	public List<String> getAllemail(){
		return userDao.findAllEmail();
	}
	
	public long getUserIDFromEmail(String email) {
		return userDao.findIDFromEmail(email);
	}
	
	public User getUserByEmail(String email) {
		return userDao.find(email);
	}

	private boolean isUserExist(User user) {
		if (userDao.find(user.getId()) != null)
			return false;
		return true;
	}
}

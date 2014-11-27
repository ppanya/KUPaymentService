package ku.payment.handler;

import java.util.List;

import ku.payment.entity.Wallet;
import ku.payment.service.WalletDao;

public class WalletHandler {

	private WalletDao walletDao;

	public WalletHandler(WalletDao walletDao) {
		this.walletDao = walletDao;
	}

	public boolean createWallet(long userID) {
		Wallet wallet = new Wallet(userID, 0, "");
		return walletDao.save(wallet);
	}

	public boolean addMoney(long walletID, double amount) {
		if (!isWalletExist(walletID))
			return false;
		Wallet wallet = walletDao.find(walletID);
		double balance = wallet.getBalance();
		wallet.setBalance(balance + amount);
		return walletDao.update(wallet);
	}

	public boolean deductMoney(long walletID, double amount) {
		if (!isWalletExist(walletID))
			return false;
		Wallet wallet = walletDao.find(walletID);
		double balance = wallet.getBalance();
		wallet.setBalance(balance - amount);
		return walletDao.update(wallet);
	}

	public boolean deleteWallet(long walletID) {
		if (!isWalletExist(walletID))
			return false;
		Wallet wallet = walletDao.find(walletID);
		return walletDao.delete(wallet.getId());
	}
	
	public List<Wallet> getAllWallet() {
		return walletDao.findAll();
	}
	
	public Wallet getWalletByID(long id) {
		return walletDao.find(id);
	}
	
	public Wallet getWalletByuserID(long userID) {
		return walletDao.findByUser(userID);
	}
	
	private boolean isWalletExist(long walletID) {
		if (walletDao.find(walletID) == null)
			return false;
		return true;
	}
}

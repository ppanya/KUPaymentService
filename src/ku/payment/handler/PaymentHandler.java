package ku.payment.handler;

import java.util.List;

import ku.payment.entity.PaymentTransaction;
import ku.payment.entity.Wallet;
import ku.payment.service.PaymentDao;
import ku.payment.service.UserDao;
import ku.payment.service.WalletDao;
import ku.payment.service.WalletTransactionDao;

public class PaymentHandler {

	private PaymentDao payDao;
	private WalletHandler walletHandler;

	public PaymentHandler(PaymentDao payDao,WalletHandler walletHandler) {
		this.payDao = payDao;
		this.walletHandler = walletHandler;
	}

	public boolean sendPayment(PaymentTransaction payment) {
		return payDao.save(payment);
	}

	public PaymentTransaction acceptPayment(PaymentTransaction payment) {

		Wallet customer_wallet = walletHandler.getWalletByuserID(payment.getRecipientID());
		Wallet merchant_wallet = walletHandler.getWalletByuserID(payment.getSenderID());

		double customer_cash = customer_wallet.getBalance();
		double amount = payment.getAmount();

		if (customer_cash < amount) {
			payment.setStatus("Insufficient funds");
		} else {
			payment.setStatus("Success");
			walletHandler.deductMoney(customer_wallet.getId(), amount);
			walletHandler.addMoney(merchant_wallet.getId(), amount);
		}
		
		payDao.update(payment);
		return payment;
	}

	public PaymentTransaction reversePayment(PaymentTransaction payment) {
		
		Wallet customer_wallet = walletHandler.getWalletByuserID(payment.getRecipientID());
		Wallet merchant_wallet = walletHandler.getWalletByuserID(payment.getSenderID());
		
		double customer_cash = customer_wallet.getBalance();
		double merchant_cash = merchant_wallet.getBalance();
		double amount = payment.getAmount();
		
		payment.setStatus("Reverse Transaction");
		if(merchant_cash<amount)
			payment.setStatus("Can't reverse a Transaction due to insufficient merchant's cash.");
		else {
			walletHandler.deductMoney(merchant_wallet.getId(), amount);
			walletHandler.addMoney(customer_wallet.getId(), amount);
		}
		payDao.update(payment);
		
		return payment;
		
	}
	
	public List<PaymentTransaction> getAllPayment() {
		return payDao.findAll();
	}
	
	public PaymentTransaction getPaymentByID(long id) {
		return payDao.find(id);
	}
	
	public List<PaymentTransaction> getPaymentByUser(long userID) {
		return payDao.findByUser(userID);
	}

}

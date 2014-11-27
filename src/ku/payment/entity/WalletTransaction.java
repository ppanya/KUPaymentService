package ku.payment.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "wallet_transaction")
@XmlRootElement(name = "walletTransaction")
@XmlAccessorType(XmlAccessType.FIELD)
public class WalletTransaction {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XmlAttribute
	private long id;
	private long walletID;
	private double amount;
	private String dateTime;
	private String transactionType;
	
	public WalletTransaction(){
		
	}

	public WalletTransaction(long id) {
		this.id = id;
	}

	public WalletTransaction(long walletID, double amount, String dateTime,
			String transactionType) {
		this.walletID = walletID;
		this.amount = amount;
		this.dateTime = dateTime;
		this.transactionType = transactionType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getWalletID() {
		return walletID;
	}

	public void setWalletID(long walletID) {
		this.walletID = walletID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}


}

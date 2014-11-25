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
@Table(name = "PaymentTransaction")
@XmlRootElement(name = "payment")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentTransaction {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@XmlAttribute
	private long id;
	private long recipientID;
	private long senderID;
	private double amount;
	private String status;
	private String datetime;

	public PaymentTransaction() {

	}

	public PaymentTransaction(long recipientID, long senderID, double amount,String status,
			String datetime) {
		this.recipientID = recipientID;
		this.senderID = senderID;
		this.amount = amount;
		this.status = status;
		this.datetime = datetime;
	}

	public PaymentTransaction(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRecipientID() {
		return recipientID;
	}

	public void setRecipientID(long recipientID) {
		this.recipientID = recipientID;
	}

	public long getSenderID() {
		return senderID;
	}

	public void setSenderID(long senderID) {
		this.senderID = senderID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDateTime() {
		return datetime;
	}

	public void setDateTime(String datetime) {
		this.datetime = datetime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void applyUpdate(PaymentTransaction update) {
		if (update == null)
			return;
		if (update.getId() != 0 && update.getId() != this.getId())
			throw new IllegalArgumentException(
					"Update contact must have same id as contact to update");
		
		if (update.getRecipientID() != 0)
			this.setRecipientID(update.getRecipientID());
		
		if (update.getSenderID() != 0)
			this.setSenderID(update.getSenderID());
		
		if (update.getStatus() != null)
			this.setStatus(update.getStatus());
		
		if (update.getAmount() > 0)
			this.setAmount(update.getAmount());
		
		if (update.getDateTime() != null)
			this.setDateTime(update.getDateTime());

	}

}

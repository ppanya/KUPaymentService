<h1>KUPaymentService</h1>

<h3>Description</h3>
Payment Webservice, Service for payment from one user to other user. <br>
There is not include parts that use bank api to redeem money from this service or deposite money to this service.

<h3>Stakeholder</h3>
* Merchant
* Customer
* Payment System Admin
<br><b><i>note:</i></b> I consider that merchant and customer is a different user type. But I still consider about customer to customer transaction, should this service support it or not.

<h3>Specific Term</h3>
* Payment - a payment for goods/services that merchant send to the recipient (customer).
* Problem - a problem that broke an agreement of two stakeholders which is merchant and customer.
<br><b><i>note:</i></b> It should have a standard for Problem term but it seem hard to set it up.

<h3>Use Cases</h3>
1. Send a payment
  * Primary Actor: Merchant
  * Scope: Payment System
  * Level: Very High
  * Story: Merchant send a payment to customer.
2. Accept a payment (funds transferred)
  * Primary Actor: Customer
  * Scope: Payment System
  * Level: Very High
  * Story: Customer approve a payment then funds transferred to merchant. (payment completed)
3. Reverse/chargeback a payment
  * Primary Actor: Customer
  * Scope: Payment System
  * Level: Low
  * Story: Some problem occured. Customers want to refund their funds back. They go and try to reverse/rollback a payment.

<h3>Functional Requirement</h3>
1. Transaction history
2. Current balance for each user.
3. (Extra) Support multiple currency and multiple language

<h3>API Specification</h3>
<b><i>[API Specification at github's wiki](https://github.com/maixezer/KUPaymentService/wiki/API-Specification) </i></b> <br>
<b><i>[google's doc link](https://docs.google.com/document/d/1AndOGXRR6K3h-zw_yxhqK6GgPK1ZZImVK7IjFj_BfzM/edit#) <i><b>

<h3>Reference</h3>
This part is reference of my research through paypal system.
I found this [link](https://www.paypal.com/cy/webapps/mpp/ua/servicedescription-full) is really helpful.

<h2>Developer</h2>
* Atit Lelasuksan 5510546201
* Parinthorn Panya 5510546085
* Wat Wattanagaroon 5510546140

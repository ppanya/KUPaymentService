<h1>KUPaymentService</h1>

<h3>Description</h3>
Payment Webservice, Service for payment from one user to other user. <br>
There is not include parts that use bank api to redeem money from this service or deposite money to this service.

<h3>Stakeholder</h3>
* Store
* Customer
* Payment System Admin
<br>note: I consider that store and customer is a different user type. But I still consider about customer to customer transaction, should this service support it or not.

<h3>Specific Term</h3>
* Payment - a payment for goods/services that store send to the recipient (customer).
* Problem - a problem that broke an agreement of two stakeholders which is store and customer.
<br>note: It should have a standard for Problem term but it seem hard to create.

<h3>Use Cases</h3>
1. Store send a payment to customer.
2. Customer accept a payment. (funds transferred)
3. Customer reverse/chargeback a payment if problem occured.

<h3>Functional Requirement</h3>
1. Transaction history
2. Current balance for each user.
3. (Extra) Support multiple currency and multiple language

<h3>Reference</h3>
This part is reference of my research through paypal system.
I found this [link](https://www.paypal.com/cy/webapps/mpp/ua/servicedescription-full) is really helpful.

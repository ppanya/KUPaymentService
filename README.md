<h1>KUPaymentService</h1>

<h3>Description</h3>
Payment Webservice, Service for payment from one user to other user. There are two sides of user first is customer and merchant. User want to pay money to merchant by kupaypal. After check out payment from merchant, website will direct to kupaypal login. User have to have kupaypal id before login then kupaypal will show the order id merchant name total amount and time of purchased, user will choose accepted or decline for confirm that payment. Then kupaypal will redirect user to merchant website. <br>
There is not include parts that use bank api to redeem money from this service or deposite money to this service.

<h3>Stakeholder</h3>
* Merchant
* Customer
* Payment System Admin
<br><b><i>note:</i></b>I consider that merchant and customer is a different user type. But I still consider about customer to customer transaction, should this service support it or not.

<h3>Specific Term</h3>
* Payment - a payment for goods/services that merchant send to the recipient (customer).
* Problem - a problem that broke an agreement of two stakeholders which is merchant and customer.
<br><b><i>note:</i></b> It should have a standard for Problem term but it seem hard to set it up.

<h3>Use Cases</h3>
<h2>1. Send a payment</h2>
When client get price, order id, merchant id, and date time, it can POST payment to webservice.
  * Primary Actor: Merchant
  * Scope: Payment System
  * Level: Very High
  * Story: Merchant send a payment to customer.

<h2>Scenario Create payment</h2>
<h4>Main Success Scenario and steps</h4>
1. User choose product
2. User proceed and check out
3. Merchant website will send order id, merchant id, total amount, and date time for create payment to webservice
Create Payment

<h4>Extensions</h4>
2a. Total amount is minus value
    .1 Merchant have to send order id, merchant id, total amount, and date time again

<h4>Trigger</h4>
  User selects to pay by "Ku paypal"  

<h4>Guarantee</h4>
  User can create payment


<h2>2. Accept a payment (funds transferred)</h2>
When customer sure for transfer money, they can PUT payment to accept the payment.
  * Primary Actor: Customer
  * Scope: Payment System
  * Level: Very High
  * Story: Customer approve a payment then funds transferred to merchant. (payment completed)

<h2>Scenario Accept a payment</h2>
<h4>Main Success Scenario and steps</h4>
1. User will see all of detail of payment
2. User accept the payment

Create Account

<h4>Precondition</h4>
  User is logged in to ku paypal
  User proceed or check out from merchant

<h4>Guarantee</h4>
  User pay the payment


<h2>3. Reverse/chargeback a payment</h2>
When customer see payment description, price/merchant is not correct, Customer will decline this payment.
  * Primary Actor: Customer
  * Scope: Payment System
  * Level: Low
  * Story: Some problem occured. Customers want to refund their funds back. They go and try to reverse/rollback a payment.

<h2>Scenario Charge back</h2>
<h4>Main Success Scenario and steps</h4>
1. User full fill form for reverse paymnet from merchant
2. Merchant tell webservice to charge back
3. User get charge back

<h4>Extensions</h4>
2 Admin of website do not permit to charge back

<h4>Trigger</h4>
  User selects to pay by "Reverse"  

<h4>Precondition</h4>
  User paid payment already

<h4>Guarantee</h4>
  User can get charge back from payment
<h2>4. User registration</h2>
 When customer can not login to the system.
  * Primary Actor: Customer
  * Scope: Account management
  * Level: High
  * Story: Customer want to become a member of the system.

<h2>Scenario Register</h2>
<h4>Main Success Scenario and steps</h4>
1. User types a user name of his or her choice
2. User types a password
3. User retypes the password
4. User types his or her first name
5. User types his or her last name
6. User types his or her Address, Date of birth (Optional)
7. User types his or her credit card number.
Create Account

<h4>Extensions</h4>
1a. User name is already in use
    .1 User is requested to select another user name and password
3a. The two passwords are different
    .1 User is requested to retype (twice) his/her password

<h4>Trigger</h4>
  User selects the "Sign Up" link 

<h4>Precondition</h4>
  User is not logged in

<h4>Guarantee</h4>
  User becomes a registeredplayer

<h2>5. User Log In</h2>
 When customer want to accessto the system.
  * Primary Actor: Customer
  * Scope: Account management
  * Level: High
  * Story: Customer want to login to the system.

<h2>Scenario Login User</h2>
<h4>Main Sucess scenario</h4>
1. User types his/her user name
2. User types his/her password
Login

<h4>Extensions</h4>
2a. User provides invalid login parameter
(see Login Failed)

<h4>Trigger</h4>
User selects the "Login" link

<h4>Precondition</h4>
User does not login yet

<h4>Guarantee</h4>
The user can see his/her own information in paypal

<h2>6. User Log In Failed</h2>
 When customer want to accessto the system.
  * Primary Actor: Customer
  * Scope: Account management
  * Level: High
  * Story: Customer want to login to the system.

<h2> Scenario Login failed</h2>
<h3>Login Failed</h3>
<h4>Precondition</h4>
-The user provides invalid login parameters
Precondition should be identical with the condition of the extension point
<h4>Main success scenario</h4>
1.System redirects the User to the Login page
2.System informs the User that he/she typed a non-registered user name
<h3>Login without Registration</h3>
<h4>Precondition</h4>
-The User typed a non-registered user name
Precondition should refine the precondition of Login Failed
<h4>Main success scenario</h4>
1. System redirects the User to the Login page
2. System informs the User that he/she typed a non-registered user name

<h3>Functional Requirement</h3>
1. Transaction history
2. Current balance for each user.
3. (Extra) Support multiple currency and multiple language

<h3>API Specification</h3>
<b><i>[API Specification at github's wiki](https://github.com/maixezer/KUPaymentService/wiki/API-Specification) </i></b> <br>

<h3>Reference</h3>
This part is reference of my research through paypal system.
I found this [link](https://www.paypal.com/cy/webapps/mpp/ua/servicedescription-full) is really helpful.

<h2>Developer</h2>
* Atit Lelasuksan 5510546201
* Parinthorn Panya 5510546085
* Wat Wattanagaroon 5510546140

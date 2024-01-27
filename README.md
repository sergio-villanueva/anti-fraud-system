# Anti-Fraud System
A REST API that is primarily used to verify financial transactions for fraud. This project was built with:
- JDK 17
- Spring Boot 3
- Spring Data JPA
- Spring Security
- Spring Web
- H2 Database


## Users
There are three types of users in the system:
- <b>Merchant</b> : The main user who uses the system to verify transactions for fraud 
- <b>Support</b> : The user who mainly provides support for events such as transaction feedback
- <b>Administrator</b> : A user with special privileges and the first user created upon application startup 

<p>When created, a new user is locked by default and only the administrator can unlock new users. A new user 
is marked as Merchant by default and only an Administrator can assign users as Support.</p>

The following is a chart showing which user has access to which endpoint:

![antifraud system endpoints.png](..%2F..%2FDiagrams%2Fantifraud%20system%20endpoints.png)
![access and role.png](..%2F..%2FDiagrams%2Faccess%20and%20role.png)

## Verify Transaction Journey Process Map
![transactionValidation.png](..%2F..%2FDiagrams%2FtransactionValidation.png)

<p>Transaction amount limits are based on the transaction history of a card number. Each approval and rejection 
will raise or lower the threshold, respectively. Whenever a new card number is entered the default 
thresholds for max allowed limit is <b>$200.00</b> and max manual limit is <b>$1500.00</b></p> 

## Blacklisted
Support users can send requests to enter, fetch, or delete suspicious ids or stolen card numbers that are 
blacklisted for the fraud verification process.

## Recognized World Regions
Every transaction must be associated to one of the following world regions:
![World Regions.png](..%2F..%2FDiagrams%2FWorld%20Regions.png)
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
![antifraud system endpoints](https://github.com/sergio-villanueva/anti-fraud-system/assets/94728439/b7db9783-a227-40e1-91e3-09f13ffb73b5)
![access and role](https://github.com/sergio-villanueva/anti-fraud-system/assets/94728439/9699979d-3aed-496a-8f56-6bfbc7670ecc)

## Verify Transaction Journey Process Map
![transactionValidation](https://github.com/sergio-villanueva/anti-fraud-system/assets/94728439/ad634590-293c-4846-bdc7-b1e4af33592c)

<p>Transaction amount limits are based on the transaction history of a card number. Each approval and rejection 
will raise or lower the threshold, respectively. Whenever a new card number is entered the default 
thresholds for max allowed limit is <b>$200.00</b> and max manual limit is <b>$1500.00</b></p> 

## Blacklisted
Support users can send requests to enter, fetch, or delete suspicious ids or stolen card numbers that are 
blacklisted for the fraud verification process.

## Recognized World Regions
Every transaction must be associated to one of the following world regions:

![World Regions](https://github.com/sergio-villanueva/anti-fraud-system/assets/94728439/fb29c22b-c0f8-4259-a2cd-9f7504a75f84)

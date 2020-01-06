# HungryStomach
The HungryStomach is an Android app written in Java. Users can sell their home-made food and upload to the app. The other users can choose their favorite food and process the transactions online. The request notifications will send to the sellers after payment. The users can provide five-star ratings and comments of the food they purchase.

![device-2019-12-20-163804](https://user-images.githubusercontent.com/54279382/71795841-f52f4000-2ffc-11ea-9d49-6b8df1454a06.png)


| Feature      | Description      |
|------------|-------------|
| Firebase Cloud Messaging (FCM)  | will be send whenever the transaction are finished |
| Chat Room  | available in receipt and request activity |
| Braintree Payment | pay with visa using braintree sandbox |
| Rating System  | rating will open as the buyers click finished radio button |
| Shopping Cart  | add, delete item w/ quantity |
| Personal Info Setting  | setup the address, full name, and icon |

# FCM(firebase cloud messaging)
The notification service is provided by firebase. It requires fcm tokens for each
account whenever the orders are placed. The tokens expire after an hour, therefore
the tokens refresh at the time the users check out. After the transactions are
successful, both buyers and sellers receive notifications from the app. In the request
pages, the sellers have three radio buttons to update the progress to buyers.
As the sellers press each button, notifications will be sent to buyers separately.

# Rating System
After all the transactions are successful, the buyers will receive notifications about the finished
transactions. Then the systems will create a list of purchased food availalbe in rating sessions.
Users can give feebacks with five-star rating bars and comments. All of the results will post into
the detail activity with listview of previous comments.

# Braintree Payment
In the checkout activity, the buyers require to use braintree sandbox payment to complete the
transactions. Braintree is running on xampp web server with php and MySQL backend. When the
users start the payment button, the web servers first generate account tokens. As the buyers
input the visa information, the php creates params which contain the payment information
(grand total and nonce) and record the payments in receipt and request activities.

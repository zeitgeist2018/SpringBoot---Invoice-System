# SpringBoot-Invoice-System
Simple invoice management system built with SpringBoot 2, Spring JPA, Spring Security, Thymeleaf, jQuery, Bootstrap 4 and FontsAwesome.

# Demo
You can access a live demo of this web app in https://cristianlobo-spring-invoices.herokuapp.com
Username: admin. Password: 12345

# Screenshots
![alt text](https://raw.githubusercontent.com/zeitgeist2018/SpringBoot---Invoice-System/master/images/logged-in-as-admin.png)

![alt text](https://raw.githubusercontent.com/zeitgeist2018/SpringBoot---Invoice-System/master/images/invoice-details.png)

![alt text](https://raw.githubusercontent.com/zeitgeist2018/SpringBoot---Invoice-System/master/images/logged-in-as-user.png)

![alt text](https://raw.githubusercontent.com/zeitgeist2018/SpringBoot---Invoice-System/master/images/not-signed-in.png)

![alt text](https://raw.githubusercontent.com/zeitgeist2018/SpringBoot---Invoice-System/master/images/user-details-admin.png)

## Run it
You are free to clone this repository and open it on Eclipse JEE. Don't forget to configure the database in the file 'application.properties'.

## Features
This app has an auth system based on cookies and sessions. If you don't sign in, you can only see the customer list, but nothing else. If you log in with a ROLE_USER role, you will be able to view the customers' details, and if you log in as ROLE_ADMIN, you will have full CRUD rights, so you will be able to create, update and delete customers and their invoices.
* You can see some screenshots of the app in the /images folder.

## Language
The app's main language is Spanish, but it is partially translated into English. You can change the language via the dropdown located in the navbar. The exported documents (pdf, xlxs) are translated based on the language configuration of the app.


## Default users

-With ROLE_ADMIN:
* username: admin
* password: 12345

-With ROLE_USER:
* username: user
* password: 12345

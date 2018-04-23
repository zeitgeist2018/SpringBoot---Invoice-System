# SpringBoot-Invoice-System
Simple invoice management system built with SpringBoot 2, Spring JPA, Spring Security and Thymeleaf

## Run it
You are free to clone this repository and open it on Eclipse JEE. Don't forget to configure the database in the file 'application.properties'.

## Features
This app has an auth system based on cookies and sessions. If you don't sign in, you can only see the customer list, but nothing else. If you log in with a ROLE_USER role, you will be able to view the customers' details, and if you log in as ROLE_ADMIN, you will have full CRUD rights, so you will be able to create, update and delete customers and their invoices.

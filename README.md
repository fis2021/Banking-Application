# **Banking-Management-Application**

## Table of Contents
- [General Description](#general-description)
- [Technologies Used](#technologies-used)
- [Registration](#registration)
- [User](#user)
- [Admin](#admin)
- [Issue Tracking](#issue-tracking)

## General Description

Intellij Banking is an application 
created to make money transactions easier. A user can check his balance, deposit, 
withdraw and transfer money in three different currencies, RON, USD and EUR.

## Technologies Used
- [Java 15.0.1](https://www.oracle.com/java/technologies/javase/15-0-1-relnotes.html)
- [JavaFX 11.0.2](https://gluonhq.com/products/javafx/)
- [Maven](https://maven.apache.org/)
- [SQLite](https://www.sqlite.org/index.html)

## Registration

There are two types of users that can use the application:
- User
- Admin

A user can register into his account or create one by providing different information,
such as personal information (first name, last name, date of birth etc), account 
information (Card Number, Email Address) and credentials (Username, Password).

There is only one admin account. Admin accounts cannot be created like regular ones,
because they carry a lot more power. There is no special menu to log in as an admin,
admins log in from the same screen.

## User

Regular users can log in and see basic information such as balance and transfer 
historic. He can also withdraw, deposit and transfer or request money to/from other 
users.

## Admin

Admins can see all transactions effectuated or requested. They can also manage them.

## Issue Tracking
For the purpose of demonstrating a complete Agile Workflow, we
created a Jira instance that you can find
[here](https://intelli-banking.atlassian.net/jira/software/projects/IB/boards/1).
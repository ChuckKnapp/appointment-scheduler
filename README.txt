Title: ChuckKnappC195Project
Application Version: v1
Date: 1/2/2022

Purpose: This is a GUI based appointment scheduling application.
         Users can Create, Read, Update, and Delete appointments and customers.
         Scheduling hours are in Eastern Standard Time and converted to the users local time.
         It was designed to pull data from a pre-existing MySQL database.
         All login attempts are written to a text file indicating the date and time of the attempt and if it was successful.
         Three reports can be generated:
            - Appointments by type and month
            - Schedule for contacts (an appointment schedule by individual contacts)
            - Schedule for customers (an appointment schedule by individual customers)

Author: Chuck Knapp
Contact Info: ck145ck@gmail.com

IDE: IntelliJ IDEA Community Edition 2021.1.1
JDK: Java SE 11.0.11
JavaFX Version: JavaFX-SDK-11.0.2
MySQL Connector driver: mysql-connector-java-8.0.25

How To Run The Program:
 - Launch the application by running the main method in the Main class
 - Log in with:
    User Name: test
    Password: test


Description of Additional Report:
 - The additional report is a schedule for customers.
 - A schedule will be generated when a customer is selected showing appointments listed by date for that customer.
 - Contact information is display for the selected customer.
package sample.model;

import java.time.LocalDateTime;

/** This is a class that models customer appointments. */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private String customer;
    private int customerId;
    private String user;
    private int userId;
    private String contact;

    /** This is the constructor for the Appointment class.
     @param id an appointment id
     @param title an appointment title
     @param description an appointment description
     @param location an appointment location
     @param type an appointment type
     @param start a start date and time of the appointment
     @param end an end date and time of the appointment
     @param customerId an id of the customer associated with the appointment
     @param userId an id of the user associated with the appointment
     @param contact a name of the contact associated with the appointment */
    public Appointment(int id, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end,
                       int customerId, int userId, String contact) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contact = contact;
    }

    /** This returns an appointment id.
     @return Returns an appointment id */
    public int getId() { return id; }

    /** This metnhod returns an appointment title.
     @return Returns a title */
    public String getTitle() { return title; }

    /** This method returns a description of the appointment.
     @return Returns a description */
    public String getDescription() { return description; }

    /** This method returns a location of the appointment.
     @return Returns a location */
    public String getLocation() { return location; }

    /** This method returns the type of the appointment.
     @return Returns an appointment type */
    public String getType() { return type; }

    /** This method returns a start date and time of an appointment.
     @return Returns a start date and time */
    public LocalDateTime getStart() { return start; }

    /** This method returns en end date and time of an appointment.
     @return Returns an end date and time */
    public LocalDateTime getEnd() { return end; }

    /** This method returns a customer id associated with an appointment.
     @return Returns a customer id*/
    public int getCustomerId() { return customerId; }

    /** This method returns a user id associated with an appointment.
     @return Returns a user id */
    public int getUserId() { return userId; }

    /** This method returns a contact name associated with an appointment.
     @return Returns a contact name */
    public String getContact() { return contact; }

    /** This method sets the appointment id of an appointment.
     @param id an appointment id */
    public void setId(int id) { this.id = id; }

    /** This method sets the title of an appointment.
     @param title a title */
    public void setTitle(String title) { this.title = title; }

    /** This method sets the description of an appointment.
     @param description a description */
    public void setDescription(String description) { this.description = description; }

    /** This method sets the type of an appointment.
     @param type a type of an appointment */
    public void setType(String type) { this.type = type; }

    /** This method sets the start date and time of an appointment.
     @param start a start date and time */
    public void setStart(LocalDateTime start) { this.start = start; }

    /** This method sets the end date and time of an appointment.
     @param end and end date and time */
    public void setEnd(LocalDateTime end) { this.end = end; }

    /** This method sets the customer associated with an appointment.
     @param customer a customer */
    public void setCustomer(String customer) { this.customer = customer; }

    /** This method sets the users associated with an appointment.
     @param user a user */
    public void setUser(String user) { this.user = user; }

    /** This method sets the contact associated with an appointment.
     @param contact a contact */
    public void setContact(String contact) { this.contact = contact; }

    /** This method overrides the toString() method. It return a String of an appointment title, start date and time and end date and time.
     @return Returns a string of an appointment title, start date and time and end date and time */
    @Override
    public String toString() { return title + " START: " + start + " END: " + end; }
}

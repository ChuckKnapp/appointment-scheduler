package sample.Utility;

import javafx.collections.ObservableList;
import sample.controller.AppointmentDetailController;
import sample.model.Appointment;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/** This class holds the database helper methods for the Appointments class. */
public class DBAppointments {

    /** This method queries a database. It matches a customer to an appointment and returns that customer.
     @param appointment an appointment
     @return Returns a customer */
    public static String getCustomerComboChoice(Appointment appointment) throws SQLException {
        String customer = "";
        String sql = "SELECT Customer_Name " +
                "FROM customers, appointments WHERE customers.Customer_ID = " + appointment.getCustomerId() + ";";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            customer = rs.getString("Customer_Name");
        }
        return customer;
    }

    /** This method queries a database. It matches a user to an appointment and returns that user.
     @param appointment an appointment
     @return Returns a user */
    public static String getUserComboChoice(Appointment appointment) throws SQLException {
        String user = "";
        String sql = "SELECT User_Name " +
                "FROM users, appointments WHERE users.User_ID= " + appointment.getUserId()+ ";";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            user = rs.getString("User_Name");
        }
        return user;
    }

    /** This method queries a database. It fills an ObservableList of contact names with the returned ResultSet
     @param contactNameList an ObservableList of contact names */
    public static void fillContactNameList(ObservableList<String> contactNameList) throws SQLException {
        String sql = "SELECT * FROM contacts";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            contactNameList.add(rs.getString("Contact_Name"));
        }
    }

    /** This method queries a database. I fills and ObservableList of customer names with the returned ResultSet.
     @param customerNameList an ObservableList of customer names */
    public static void fillCustomerNameList(ObservableList<String> customerNameList) throws SQLException {
        String sql = "SELECT * FROM customers";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            customerNameList.add(rs.getString("Customer_Name"));
        }
    }

    /** This method queries a database. I fills an ObservableList of user names with the returned ResultSet.
     @param userNameList an ObservableList of user names */
    public static void fillUserNameList(ObservableList<String> userNameList) throws SQLException {
        String sql = "SELECT * FROM users";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            userNameList.add(rs.getString("User_Name"));
        }
    }

    /** This method queries a database. I fills an ObservableList of appointment types with the returned ResultSet.
     @param appointmentTypeList an ObservableList of appointment types */
    public static void fillAppointmentTypeList(ObservableList<String> appointmentTypeList) throws SQLException {
        String sql = "SELECT * FROM appointments";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            appointmentTypeList.add(rs.getString("Type"));
        }
    }

    /** This method queries a database. It fills an ObservableList of a given customer's appointments.
     @param customerSchedule an ObservableList of appointments
     @param  customerName a customer's name */
    public static void fillCustomerScheduleList(ObservableList<Appointment> customerSchedule, String customerName) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_Name " +
                "FROM appointments, contacts " +
                "WHERE appointments.Contact_ID = contacts.Contact_ID ";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            if (rs.getInt("Customer_ID") == getCustomerIdSelection(customerName)) {
                customerSchedule.add(new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                        rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                        convertUTCToLocalDateTime(LocalDateTime.parse(rs.getString("Start"), formatter)),
                        convertUTCToLocalDateTime(LocalDateTime.parse(rs.getString("End"), formatter)),
                        rs.getInt("Customer_ID"), rs.getInt("User_ID"), rs.getString("Contact_Name")));
            }
        }
    }

    /** this method queries a database. It fills an ObservableList of a given contact's appointments.
     @param contactSchedule an ObservableList of appointments
     @param contactName a contact's name */
    public static void fillContactScheduleList(ObservableList<Appointment> contactSchedule, String contactName) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_Name " +
                "FROM appointments, contacts " +
                "WHERE appointments.Contact_ID = contacts.Contact_ID ";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            if (rs.getString("Contact_Name").equals(contactName)) {
                contactSchedule.add(new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                        rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                        convertUTCToLocalDateTime(LocalDateTime.parse(rs.getString("Start"), formatter)),
                        convertUTCToLocalDateTime(LocalDateTime.parse(rs.getString("End"), formatter)),
                        rs.getInt("Customer_ID"), rs.getInt("User_ID"), rs.getString("Contact_Name")));
            }
        }
    }

    /** This method queries a database. It takes a customer's name and returns the customer id.
     @param customer a customer name
     @return Returns a customer id */
    public static int getCustomerIdSelection(String customer) throws SQLException {
        String sql = "SELECT * FROM customers";
        int customerId = -1;
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            if (rs.getString("Customer_Name").equals(customer)) {
                customerId = rs.getInt("Customer_ID");
            }
        }
        return customerId;
    }

    /** This method queries a database. It takes a contact's name and returns the contact id.
     @param contact a contact name
     @return Returns a contact id */
    public static int getContactIdSelection(String contact) throws SQLException {
        String sql = "SELECT * FROM contacts";
        int contactId = -1;
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            if (rs.getString("Contact_Name").equals(contact)) {
                contactId = rs.getInt("Contact_ID");
            }
        }
        return contactId;
    }

    /** This method queries a database. It takes a user's name and return the user id.
     @param user a user name
     @return Returns a user id */
    public static int getUserIdSelection(String user) throws SQLException {
        String sql = "SELECT * FROM users";
        int userId = -1;
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            if (rs.getString("User_Name").equals(user)) {
                userId = rs.getInt("User_ID");
            }
        }
        return userId;
    }

    /** This method queries a database. It deletes an appointment.
     @param appointment an appointment */
    public static void deleteAppointment(Appointment appointment) {
        String sql = "DELETE FROM appointments WHERE Appointment_ID=" + appointment.getId();
        DBQuery.makeQuery(sql);
    }

    /** This method queries a database. If the appointment's id is -1 (impossible, so appointment doesn't exist) it creates a new appointment, or it updates an existing appointment.
     @param appointment an appointemnt
     @param start a start date and time
     @param end a end date and time
     @param customerId a customer id
     @param user a user
     @param contactId a contact id
     @return Returns a boolean that is true if there is no problem with the appointment */
    public static boolean addOrUpdateAppointment(Appointment appointment, LocalDateTime start, LocalDateTime end, int customerId, int user, int contactId) throws SQLException {
        if (AppointmentDetailController.incorrectAppointmentTimes(start, end) || overlappingAppointment(appointment, customerId, start, end))
            return false;
        if (appointment.getId() != -1) {
            String sql = "UPDATE appointments SET Title='" + appointment.getTitle() + "', Description='" + appointment.getDescription() +
                    "', Location='" + appointment.getLocation() + "', Type='" + appointment.getType() + "', " +
                    "Start='" + AppointmentDetailController.convertToUTC(start) + "', End='" + AppointmentDetailController.convertToUTC(end) +
                    "', Customer_ID='" + customerId + "', User_ID='" + user + "', Contact_ID=" + contactId +
                    ", Last_Update='" + Timestamp.valueOf(AppointmentDetailController.convertToUTC(LocalDateTime.now())) +
                    "', Last_Updated_By=CURRENT_USER()" +
                    " WHERE Appointment_ID = " + appointment.getId();
            DBQuery.makeQuery(sql);
        } else {
            String sql = "INSERT INTO appointments" +
                    "(Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID, Create_Date, Created_By, Last_Update, Last_Updated_By) " +
                    "VALUES " + "('" + appointment.getTitle() + "', '" + appointment.getDescription() + "', '" + appointment.getLocation() + "', '" +
                    appointment.getType() + "', '" + AppointmentDetailController.convertToUTC(start) + "', '" + AppointmentDetailController.convertToUTC(end) +
                    "', '" + customerId + "', '" + user + "', '" + contactId + "', '" + AppointmentDetailController.convertToUTC(LocalDateTime.now()) +
                    "', CURRENT_USER(), '"  +  Timestamp.valueOf(AppointmentDetailController.convertToUTC(LocalDateTime.now())) + "', CURRENT_USER()" + ")";
            DBQuery.makeQuery(sql);
        }
        return true;
    }

    /** This method queries a database. It checks for overlapping appointment times.
     @param appointment an appointment
     @param  customerId a customer id
     @param  start a start date and time
     @param end an end date and time
     @return Returns a boolean that is true if there is a scheduling conflict */
    public static boolean overlappingAppointment(Appointment appointment, int customerId, LocalDateTime start, LocalDateTime end) throws SQLException {
        String sql = "SELECT * FROM Appointments WHERE Customer_ID = " + customerId;
        if (appointment.getId() != -1) {
            sql = "SELECT * FROM appointments WHERE Customer_ID = " + customerId + " AND Appointment_ID <> " + appointment.getId();
        }
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            if ((start.isAfter(rs.getTimestamp("Start").toLocalDateTime()) || start.isEqual(rs.getTimestamp("Start").toLocalDateTime()))
                    && start.isBefore(rs.getTimestamp("End").toLocalDateTime())) {
                AppointmentDetailController.overlappingAppointmentAlert(rs.getInt("Appointment_ID"), rs.getString("Title"),
                        rs.getTimestamp("Start").toLocalDateTime(), rs.getTimestamp("End").toLocalDateTime());
                return true;
            } else if (end.isAfter(rs.getTimestamp("Start").toLocalDateTime()) &&
                    (end.isBefore(rs.getTimestamp("End").toLocalDateTime()) || end.isEqual(rs.getTimestamp("End").toLocalDateTime()))) {
                AppointmentDetailController.overlappingAppointmentAlert(rs.getInt("Appointment_ID"), rs.getString("Title"),
                        rs.getTimestamp("Start").toLocalDateTime(), rs.getTimestamp("End").toLocalDateTime());
                return true;
            } else if ((start.isBefore(rs.getTimestamp("Start").toLocalDateTime()) || start.isEqual(rs.getTimestamp("Start").toLocalDateTime())) &&
                    (end.isAfter(rs.getTimestamp("End").toLocalDateTime()) || end.isEqual(rs.getTimestamp("End").toLocalDateTime()))) {
                AppointmentDetailController.overlappingAppointmentAlert(rs.getInt("Appointment_ID"), rs.getString("Title"),
                        rs.getTimestamp("Start").toLocalDateTime(), rs.getTimestamp("End").toLocalDateTime());
                return true;
            }
        }
        return false;
    }

    /** This method converts UTC time to local time.
     @param ldt a LocalDateTime object
     @return Returns a LocalDateTime object*/
    public static LocalDateTime convertUTCToLocalDateTime(LocalDateTime ldt) {
        ZonedDateTime zdtInUTC = ldt.atZone(ZoneId.of("UTC"));
        ZonedDateTime zdtInSystemDefault = zdtInUTC.withZoneSameInstant(ZoneId.of(ZoneId.systemDefault().toString()));
        LocalDateTime ldtInSystemDefault = zdtInSystemDefault.toLocalDateTime();
        return ldtInSystemDefault;
    }
}



























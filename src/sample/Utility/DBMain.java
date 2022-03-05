package sample.Utility;

import javafx.collections.ObservableList;
import sample.model.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Locale;


/** This class holds the database helper methods for the Main class. */
public class DBMain {

    /** This method queries a database for a valid user. It looks for a user name password match.
     @param userName a user name
     @param password a password
     @return Returns a boolean */
    public static boolean isValidUser(String userName, String password) throws SQLException {
        String combo = userName + password;
        ResultSet rs = DBQuery.runQueryToRead("SELECT * FROM USERS");
        while (rs.next()) {
            if (combo.equals(rs.getString("User_Name") + rs.getString("Password")))
                return true;
        }
        return false;
    }

    /** This method queries a database for upcoming appointments.
     It is checking to see if the appointment time is within 15 minutes and if so, how many minutes away it is.
     @param user a user
     @return Returns an array holding the interval between now and the appointment and the appointment id */
    public static int[] checkTimeForAppointment(String user) throws SQLException {
        int[] results = new int[2];
        LocalDateTime currentTime = LocalDateTime.now();
        long timeDiff;
        long interval = 0;
        String sql = "SELECT Title, Start, User_Name, Appointment_ID FROM appointments LEFT JOIN users ON appointments.User_ID = users.User_ID";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            LocalDateTime startTime = rs.getTimestamp("Start").toLocalDateTime();
            timeDiff = ChronoUnit.MINUTES.between(startTime, currentTime);
            interval = (timeDiff + -1) * -1;
            if (rs.getString("User_Name").equals(user) && (Math.floor(interval) >= 1 && interval <= 15)) {
                results[0] = (int) Math.floor(interval);
                results[1] = rs.getInt("Appointment_ID");
                break;
            }
        }
        return results;
    }

    /** This method checks date an time of an appointment. It returns a String array holding the date and the time.
     @param id an appointment id
     @return Returns an array with the date and time of the appointment*/
    public static String[] getAlertContentText(int id) throws SQLException {
        String sql = "SELECT Appointment_ID, Start FROM appointments";
        String result[] = new String[2];
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            if (rs.getInt("Appointment_ID") == id) {
                LocalDateTime ldt = rs.getTimestamp("Start").toLocalDateTime();
                result[0] = String.valueOf(ldt.toLocalDate());
                result[1] = String.valueOf(ldt.toLocalTime());
                break;
            }
        }
        return result;
    }

    /** This method queries a database. It uses the returned ResultSet to fill an ObservableList of Appointments.
     @param appointmentList an ObservableList of Appointment objects */
    public static void fillAppointmentList(ObservableList<Appointment> appointmentList) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Contact_Name, Customer_ID, User_ID " +
                "FROM appointments, contacts " +
                "WHERE appointments.Contact_ID = contacts.Contact_ID " ;
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            appointmentList.add(new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                    rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                    rs.getTimestamp("Start").toLocalDateTime(), rs.getTimestamp("End").toLocalDateTime(),
                    rs.getInt("Customer_ID"), rs.getInt("User_ID"), rs.getString("Contact_Name")));
        }
    }

    /** This method queries a database. It uses the returned ResultSet to fill an ObservableList of Appointments for the current month.
     @param appointmentList an ObservableList of Appointment objects */
    public static void fillAppointmentMonthlyList(ObservableList<Appointment> appointmentList) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Contact_Name, Customer_ID, User_ID " +
                "FROM appointments, contacts " +
                "WHERE appointments.Contact_ID = contacts.Contact_ID " ;
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            if (rs.getTimestamp("Start").toLocalDateTime().getMonth() == LocalDate.now().getMonth()) {
                appointmentList.add(new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                    rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(), rs.getTimestamp("End").toLocalDateTime(),
                    rs.getInt("Customer_ID"), rs.getInt("User_ID"), rs.getString("Contact_Name")));
            }
        }
    }

    /** This method queries a database. It uses the returned ResultSet to fill an ObservableList of Appointments for the current week.
     @param appointmentList an ObservableList of Appointment objects */
    public static void fillAppointmentWeeklyList(ObservableList<Appointment> appointmentList) throws SQLException {
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Contact_Name, Customer_ID, User_ID " +
                "FROM appointments, contacts " +
                "WHERE appointments.Contact_ID = contacts.Contact_ID " ;
        ResultSet rs = DBQuery.runQueryToRead(sql);

        Locale locale = Locale.getDefault();
        int weekOfYear = LocalDateTime.now().get(WeekFields.of(locale).weekOfYear());
        while (rs.next()) {
            if (rs.getTimestamp("Start").toLocalDateTime().get(WeekFields.of(locale).weekOfYear()) == weekOfYear) {
                appointmentList.add(new Appointment(rs.getInt("Appointment_ID"), rs.getString("Title"),
                    rs.getString("Description"), rs.getString("Location"), rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(), rs.getTimestamp("End").toLocalDateTime(),
                    rs.getInt("Customer_ID"), rs.getInt("User_ID"), rs.getString("Contact_Name")));
            }
        }
    }

    /** This method queries a database. It uses the returned ResultSet to fill an ObservableList of Customers.
     @param customerList an ObservableList of Customer objects */
    public static void fillCustomerList(ObservableList<Customer> customerList) throws SQLException {
        String sql = "SELECT Customer_ID, Customer_Name, Address, Division, Country, Postal_Code, Phone " +
                "FROM customers, first_level_divisions, countries " +
                "WHERE first_level_divisions.Country_ID = countries.Country_ID " +
                "AND customers.Division_ID = first_level_divisions.Division_ID";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            customerList.add(new Customer(rs.getInt("Customer_ID"), rs.getString("Customer_Name"),
                    rs.getString("Address"), rs.getString("Postal_Code"), rs.getString("Phone"),
                    rs.getString("Division"), rs.getString("Country")));
        }
    }
}

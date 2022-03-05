package sample.Utility;

import javafx.collections.ObservableList;
import sample.controller.AppointmentDetailController;
import sample.model.Customer;

import java.sql.*;
import java.time.LocalDateTime;

/** This class holds the database helper methods for the Customer class. */
public class DBCustomers {

    /** This method queries a database. It uses the returned ResultSet to fill a ObservableList of divisions.
     @param country a country
     @param divisionNameList an ObservableList of division names */
    public static void setDivisionsByCountry(String country, ObservableList<String> divisionNameList) throws SQLException {
        String sql = "SELECT Division FROM first_level_divisions " +
                     "LEFT JOIN countries " +
                     "ON first_level_divisions.COUNTRY_ID = countries.Country_ID " +
                     "WHERE Country = " + "'" + country + "'";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            divisionNameList.add(rs.getString("Division"));
        }
    }

    /** This method queries a database. It takes a division name and returns that division's id.
     @param division a division
     @return Returns a division id */
    public static int getDivisionIdSelection(String division) throws SQLException {
        String sql = "SELECT Division, Division_ID FROM first_level_divisions";
        int divisionId = -1;
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            if (rs.getString("Division"). equals(division)) {
                divisionId = rs.getInt("Division_ID");
            }
        }
        return divisionId;
    }

    /** This method queries a database. It fills an ObservableList of country names with the returned ResultSet.
     @param countryNameList an ObservableList of country names */
    public static void fillCountryNameList(ObservableList<String> countryNameList) throws SQLException {
        String sql = "SELECT * FROM countries";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            countryNameList.add(rs.getString("Country"));
        }
    }

    /** This method queries a database. It fills an ObservableList of divisions with the returned ResultSet.
     @param countryComboSelection a country name
     @param divisionNameList an ObservableList of divisions */
    public static void fillDivisionNameList(String countryComboSelection, ObservableList<String> divisionNameList) throws SQLException {
        String sql = "SELECT Division_ID, Division, Country FROM first_level_divisions, countries " +
                     "WHERE first_level_divisions.Country_ID = countries.Country_ID " +
                     "AND Country='" + countryComboSelection + "'";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            divisionNameList.add(rs.getString("Division"));
        }
    }

    /** This method queries a database. It fills an ObservableList of a customer's appointments with the returned ResultSet.
     @param customerReportList an ObservableList of a customer's appointments */
    public static void fillCustomerReportList(ObservableList<String> customerReportList) throws SQLException {
        String sql = "SELECT * FROM customers";
        ResultSet rs = DBQuery.runQueryToRead(sql);
        while (rs.next()) {
            customerReportList.add(rs.getString("Customer_Name"));
        }
    }

    /** This method queries a database. If the customer's id is -1 (impossible, so customer doesn't exist) it creates a new customer, or it updates an existing customer.
     @param customer a customer
     @param divisionId a division id*/
    public static void addOrUpdateCustomer(Customer customer, int divisionId) {
        if (customer.getId() != -1) {
            String sql = "UPDATE customers SET Customer_Name='" + customer.getName() + "', Address='" + customer.getAddress()
                    + "', Postal_Code='" + customer.getPostalCode() + "', Phone='" + customer.getPhoneNumber()
                    + "', Division_ID=" + divisionId + ", Last_Update='" + Timestamp.valueOf(AppointmentDetailController.convertToUTC(LocalDateTime.now())) +
                    "', Last_Updated_By=CURRENT_USER()" + " WHERE Customer_ID = " + customer.getId();
            DBQuery.makeQuery(sql);
        } else {
            String sql = "INSERT INTO customers" +
                    "(Customer_Name, Address, Postal_Code, Phone, Division_ID, Create_Date, Created_By, Last_Update, Last_Updated_By) " + "VALUES " +
                    "('" + customer.getName() + "', '" + customer.getAddress() + "', '" + customer.getPostalCode()
                    + "', '" + customer.getPhoneNumber() + "', " + divisionId + ", '" + AppointmentDetailController.convertToUTC(LocalDateTime.now()) +
                    "', CURRENT_USER(), '"  +  Timestamp.valueOf(AppointmentDetailController.convertToUTC(LocalDateTime.now())) + "', CURRENT_USER()"
                    +  ")";
            DBQuery.makeQuery(sql);
        }
    }

    /** This method queries a database. It deletes a customer.
     @param customer a customer */
    public static void deleteCustomer(Customer customer) {
        String sql = "DELETE FROM customers WHERE Customer_ID=" + customer.getId();
        DBQuery.makeQuery(sql);
    }

    /** This method queries a database. It deletes a customer's appointments.
     @param customer a customer */
    public static void deleteCustomerAppointments(Customer customer) {
        String sql = "DELETE FROM appointments WHERE Customer_ID = " + customer.getId();
        DBQuery.makeQuery(sql);
    }
}

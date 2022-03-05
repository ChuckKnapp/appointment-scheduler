package sample;

import javafx.application.Application;
import javafx.collections.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import sample.Utility.*;
import sample.controller.*;
import sample.model.*;
import java.io.IOException;
import java.sql.*;

/** This class creates a customer appointment scheduling application. */
public class Main extends Application {
    private Stage primaryStage;
    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    /** This is the start method. It is the main entry point of all JavaFX applications. */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        mainWindow();
    }

    public void closeWindow() {
        primaryStage.close();
    }

    /** This method opens the first window of the program. It displays a log in form. */
    public void mainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/main.fxml"));
            AnchorPane pane = loader.load();
            MainController mainController = loader.getController();
            mainController.setMain(this);
            Scene scene = new Scene(pane);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** This method opens a window. It displays customer appointment information. */
    public void appointmentWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/sample/view/appointment.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            AppointmentController appointmentController = loader.getController();
            appointmentController.setMain(this);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** This method opens a window. it displays customer information. */
    public void customerWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/sample/view/customer.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            CustomerController customerController = loader.getController();
            customerController.setMain(this);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** This method opens a window.
     It displays a form to add or display appointment details.
     @param appointment An Appointment object
     */
    public void appointmentDetailWindow(Appointment appointment) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/sample/view/appointmentDetail.fxml"));
            AnchorPane pane = loader.load();
            AppointmentDetailController appointmentDetailController = loader.getController();
            Stage newStage = new Stage();
            appointmentDetailController.setMain(this, newStage, appointment);
            Scene scene = new Scene(pane);
            newStage.initOwner(primaryStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(scene);
            newStage.showAndWait();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /** This method opens a window.
     The window is a form to enter or display customer information.
     @param customer a Customer object
     */
    public void customerDetailWindow(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/sample/view/customerDetail.fxml"));
            AnchorPane pane = loader.load();
            CustomerDetailController customerDetailController = loader.getController();
            Stage newStage = new Stage();
            customerDetailController.setMain(this, newStage, customer);
            Scene scene = new Scene(pane);
            newStage.initOwner(primaryStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(scene);
            newStage.showAndWait();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /** This method opens a window. It displays an appointment schedule report by contact. */
    public void contactReportWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/sample/view/contactReport.fxml"));
            AnchorPane pane = loader.load();
            ContactReportController contactReportController = loader.getController();
            Stage newStage = new Stage();
            contactReportController.setMain(this, newStage);
            Scene scene = new Scene(pane);
            newStage.initOwner(primaryStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(scene);
            newStage.setResizable(false);
            newStage.showAndWait();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /** This method opens a window. It displays an appointment schedule report by customer */
    public void customerReportWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/sample/view/customerReport.fxml"));
            AnchorPane pane = loader.load();
            CustomerReportController customerReportController = loader.getController();
            Stage newStage = new Stage();
            customerReportController.setMain(this, newStage);
            Scene scene = new Scene(pane);
            newStage.initOwner(primaryStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(scene);
            newStage.setResizable(false);
            newStage.showAndWait();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /** This method opens a window. It generates a customer appointment report by type and month. */
    public void appointmentReportWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/sample/view/appointmentReport.fxml"));
            AnchorPane pane = loader.load();
            AppointmentReportController appointmentReportController = loader.getController();
            Stage newStage = new Stage();
            appointmentReportController.setMain(this, newStage);
            Scene scene = new Scene(pane);
            newStage.initOwner(primaryStage);
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.setScene(scene);
            newStage.setResizable(false);
            newStage.showAndWait();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /** This is a constructor for the main class. It fills ObservableLists of Appointment and Customer objects. */
    public Main() throws SQLException {
        fillAppointmentList();
        fillCustomerList();
    }

    /** This method queries a database.
     It clears a list of Appointment objects, and then fills it again with the results of the database query for all customer appointments. */
    public void fillAppointmentList() throws SQLException {
        appointmentList.clear();
        DBMain.fillAppointmentList(appointmentList);
    }

    /** This method queries a database.
     It clears a list of Appointment objects, and then fills it again with the results of the database query for customer appointments of the current week. */
    public void fillAppointmentWeeklyList() throws SQLException {
        appointmentList.clear();
        DBMain.fillAppointmentWeeklyList(appointmentList);
    }

    /** This method queries a database.
     It clears a list of Appointment objects, and then fills it again with the results of the database query for customer appointments of the current month. */
    public void fillAppointmentMonthlyList() throws SQLException {
        appointmentList.clear();
        DBMain.fillAppointmentMonthlyList(appointmentList);
    }

    /** This method queries a database.
     It clears a list of Customer objects, and then fills it again with the results of the database query for all customers. */
    public void fillCustomerList() throws SQLException {
        customerList.clear();
        DBMain.fillCustomerList(customerList);
    }

    /** This method returns an ObservableList.
     The ObservableList is of Appointment objects.
     @return appointmentList a list of Appointment objects */
    public ObservableList<Appointment> getAppointmentData() { return appointmentList; }

    /** This method returns an ObservableList.
     The ObservableList is of Customer objects
     @return customerList a list of Customer objects */
    public ObservableList<Customer> getCustomerData() { return customerList; }

    /** This is the main method. It is the entry point of all Java applications. */
    public static void main(String[] args) {
        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}

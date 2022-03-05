package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.Utility.DBMain;
import sample.Main;
import java.io.*;
import java.sql.*;
import java.time.*;
import java.util.*;

/** This class creates a log in form. */
public class MainController {

    @FXML private Button the_btn;
    @FXML private TextField userNameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel, zoneLabel;

    private String alertTitle = "Upcoming Appointments";
    private String alertString1, alertString2, alertString3, alertAppointmentId, alertTime;

    private Main main;

    /** This method connects the Main class to the controller class.
     @param main a Main object */
    public void setMain(Main main) {
        this.main = main;
    }

    /** This method handles the user sign in. It logs the activity to a file, validates the user, and allows or denies access to the application. */
    public void onSignInBtnClick() throws IOException, SQLException {
        String fileName = "login_activity.txt", item;
        FileWriter fw = new FileWriter(fileName, true);
        PrintWriter outputFile = new PrintWriter(fw);
        if (DBMain.isValidUser(userNameField.getText(), passwordField.getText())) {
            outputFile.println("User " + "[ " + userNameField.getText() + " ]" + " logged in on: " + Instant.now() + " UTC");
            checkForAppointments(userNameField.getText());
            main.appointmentWindow();
        } else {
            outputFile.println("User " + "[ " + userNameField.getText() + " ]" + " gave invalid log-in on: " + Instant.now() + " UTC");
            errorLabel.setVisible(true);
        }
        outputFile.close();
    }

    /** This method checks for upcoming appointments. It sends an alert letting the user know if there is an appointment within 15 minutes.
     @param user a user */
    public void checkForAppointments(String user) throws SQLException {
        int[] queryResults = DBMain.checkTimeForAppointment(user);
        String[] dateAndTime = DBMain.getAlertContentText(queryResults[1]);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(alertTitle);
        alert.setContentText(alertAppointmentId + ": " + queryResults[1] + "\nDate: " + dateAndTime[0] + "\n" + alertTime + ": " + dateAndTime[1]);
        if (queryResults[0] == 1) {
            alert.setHeaderText(alertString1);
        } else if (queryResults[0] > 1 && queryResults[0] <= 15) {
            alert.setHeaderText(alertString2 + " " + queryResults[0] + " minutes");
        } else {
            alert.setHeaderText(alertString3);
            alert.setContentText("");
        }
        alert.showAndWait();
    }

    /** This method initializes a JavaFX controller after its root element has been processed.
     Here it works to set up a ResourceBundle to translate between French and English based on the user's settings. */
    public void initialize() {
        ResourceBundle rb = ResourceBundle.getBundle("sample/Bundle_fr", Locale.getDefault());
        ZoneId zoneId = ZoneId.systemDefault();

        if (Locale.getDefault().getLanguage().equals("fr")) {
            userNameField.setPromptText(rb.getString("User Name"));
            passwordField.setPromptText(rb.getString("Password"));
            the_btn.setText(rb.getString("Sign In"));
            errorLabel.setText(rb.getString("Incorrect Username or Password"));
            zoneLabel.setText(rb.getString("ZoneID") + ": " + zoneId);
            alertTitle = rb.getString("Upcoming Appointments");
            alertString1 = rb.getString("You have an appointment in 1 minute or less");
            alertString2 = rb.getString("You have an appointment in");
            alertString3 = rb.getString("No appointments scheduled within the next 15 minutes");
            alertAppointmentId = rb.getString("Appointment");
            alertTime = rb.getString("Time");
        } else {
            errorLabel.setText("Incorrect Username or Password");
            zoneLabel.setText("ZoneID: " + zoneId);
            alertString1 = "You have an appointment in 1 minute or less";
            alertString2 = "You have an appointment in";
            alertString3 = "No appointments scheduled within the next 15 minutes";
            alertAppointmentId = "Appointment";
            alertTime = "Time";
        }
    }
}

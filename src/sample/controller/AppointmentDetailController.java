package sample.controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Utility.DBAppointments;
import sample.Main;
import sample.model.Appointment;
import java.sql.*;
import java.time.*;

/** This class creates an appointment form to add or update appointment information. */
public class AppointmentDetailController {
    @FXML Button saveButton, cancelButton;
    @FXML private TextField appointmentIdField, titleField, descriptionField, locationField, typeField;
    @FXML private ComboBox<String> customerComboBox, contactComboBox, userComboBox;
    @FXML private ComboBox<LocalTime> startTimeComboBox, endTimeComboBox;
    @FXML private DatePicker startDatePicker, endDatePicker;

    private Main main;
    private Stage newStage;
    private Appointment appointment;
    private ObservableList<String> contactNameList = FXCollections.observableArrayList();
    private ObservableList<String> customerNameList = FXCollections.observableArrayList();
    private ObservableList<String> userNameList = FXCollections.observableArrayList();

    /** This method initializes a JavaFX controller after its root element has been processed.
     Here it sets items in ComboBoxes.
     Business hours for this application are from 8am - 10pm EST.
     Appointments are scheduled in increments of 15 minutes, so the last appointment time available is 9:45pm EST.
     The start time ComboBox is set in 15 minute increments from 8am - 9:45pm EST.
     The end time ComboBox is set in 15 minute increments from 8:15am - 10pm EST. */
    public void initialize() throws SQLException {
        DBAppointments.fillContactNameList(contactNameList);
        DBAppointments.fillCustomerNameList(customerNameList);
        DBAppointments.fillUserNameList(userNameList);
        contactComboBox.setItems(contactNameList);
        contactComboBox.setVisibleRowCount(8);
        contactComboBox.setPromptText("Select a Contact");
        customerComboBox.setItems(customerNameList);
        customerComboBox.setVisibleRowCount(8);
        customerComboBox.setPromptText("Select a Customer");
        userComboBox.setItems(userNameList);
        userComboBox.setVisibleRowCount(8);
        userComboBox.setPromptText("Select a User");

        Instant nowUtc = Instant.now();
        ZoneId zEast = ZoneId.of("US/Eastern");
        ZoneId zSysDef = ZoneId.systemDefault();
        LocalTime localEast = LocalTime.now(zEast.getRules().getOffset(nowUtc));
        LocalTime localSysDef = LocalTime.now(zSysDef.getRules().getOffset(nowUtc));

        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(21, 45);
        LocalTime origionalStart = start;
        LocalTime originalEnd = end;

        int hourOffset = localSysDef.getHour() - localEast.getHour();
        start = start.plusHours(hourOffset);

        while (origionalStart.isBefore(originalEnd.plusSeconds(1))) {
            startTimeComboBox.getItems().add(start);
            endTimeComboBox.getItems().add(start.plusMinutes(15));
            start = start.plusMinutes(15);
            origionalStart = origionalStart.plusMinutes(15);
        }
    }

    /** This method connects the Main class to the controller class.
     @param main a Main object
     @param newStage a Stage object
     @param appointment an Appointment object */
    public void setMain(Main main, Stage newStage, Appointment appointment) throws SQLException {
        this.main = main;
        this.newStage = newStage;
        this.appointment = appointment;
        if (appointment != null) {
            fillAppointmentDetails();
        }
    }

    /** This method fills the appointment form with appointment details. */
    public void fillAppointmentDetails() throws SQLException {
        appointmentIdField.setText(String.valueOf(appointment.getId()));
        titleField.setText(appointment.getTitle());
        descriptionField.setText(appointment.getDescription());
        locationField.setText(appointment.getLocation());
        typeField.setText(appointment.getType());
        contactComboBox.getSelectionModel().select(appointment.getContact());
        startDatePicker.setValue(appointment.getStart().toLocalDate());
        startTimeComboBox.getSelectionModel().select(appointment.getStart().toLocalTime());
        endDatePicker.setValue(appointment.getEnd().toLocalDate());
        endTimeComboBox.getSelectionModel().select(appointment.getEnd().toLocalTime());
        customerComboBox.getSelectionModel().select(DBAppointments.getCustomerComboChoice(appointment));
        userComboBox.getSelectionModel().select(DBAppointments.getUserComboChoice(appointment));
    }

    /** This method handles a button click. It add or updates an appointment. */
    public void onSaveButtonClicked() throws SQLException {
        if (!validateInput()) { return; }

        LocalDateTime start = LocalDateTime.parse(startDatePicker.getValue() + "T" + startTimeComboBox.getSelectionModel().getSelectedItem());
        LocalDateTime end = LocalDateTime.parse(endDatePicker.getValue() + "T" + endTimeComboBox.getSelectionModel().getSelectedItem());
        int customerId = DBAppointments.getCustomerIdSelection(customerComboBox.getSelectionModel().getSelectedItem());
        int userId = DBAppointments.getUserIdSelection(userComboBox.getSelectionModel().getSelectedItem());
        int contactId = DBAppointments.getContactIdSelection(contactComboBox.getSelectionModel().getSelectedItem());
        String contact = contactComboBox.getSelectionModel().getSelectedItem();

        Appointment dummy;
        if (appointment != null) {
            dummy = new Appointment(appointment.getId(), titleField.getText(), descriptionField.getText(), locationField.getText(),
                    typeField.getText(), start, end, customerId, userId, contact);
        } else {
            dummy = new Appointment(-1, titleField.getText(), descriptionField.getText(), locationField.getText(),
                    typeField.getText(), start, end, customerId, userId, contact);
        }
        boolean success = DBAppointments.addOrUpdateAppointment(dummy, start, end, customerId, userId, contactId);
        if (success) {
            main.fillAppointmentList();
            newStage.close();
        }
    }

    /** This method provides an alert. It alerts if there are overlapping appointment times.
     @param id an id
     @param title a title
     @param start a start date and time
     @param end an end date and time */
    public static void overlappingAppointmentAlert(int id, String title, LocalDateTime start, LocalDateTime end) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Overlapping Appointment");
        alert.setHeaderText("You Already Have an Appointment at this time");
        alert.setContentText("Appointment: " + id + "\n" +
                "Title: " + title + "\n" +
                "Start: " + start + "\n" +
                "End: " + end);
        alert.showAndWait();
    }

    /** This method checks for incorrect appointment times.
     @param start a start date and time
     @param end an end date and time
     @return Returns a boolean, true if there is an incorrect appointment time */
    public static boolean incorrectAppointmentTimes(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || start.isEqual(end) || start.isBefore(LocalDateTime.now())) {
            incorrectAppointmentTimesAlert(start, end);
            return true;
        }
        return false;
    }

    /** This method provides an alert. It alerts if there are incorrect appointment times.
     @param start a start date and time
     @param end an end date and time */
    public static void incorrectAppointmentTimesAlert(LocalDateTime start, LocalDateTime end) {
        String msg = "";
        if (start.isBefore(LocalDateTime.now())) {
            msg = "Appointment time is in the past";
        } else if (start.isAfter(end)) {
            msg = "Start time is after End time";
        } else if (start.isEqual(end)) {
            msg = "Start and End times are the same";
        }
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Incorrect Appointment Times");
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    /** This method converts UTC time to local time.
     @param ldt a LocalDateTime object
     @return Returns a LocalDateTime object*/
    public static LocalDateTime convertToUTC(LocalDateTime ldt) {
        ZonedDateTime zdt = ldt.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
        ZonedDateTime zdtInUTC = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime ldtInUTC = zdtInUTC.toLocalDateTime();
        return ldtInUTC;
    }

    /** This method closes the appointment detail window. */
    public void onCancelButtonClicked() {
        newStage.close();
    }

    /** This method checks to see if the data in the form is valid. It returns true if so, and if not, it generates an alert and returns false.
     @return Returns a boolean */
    public boolean validateInput() {
        String s = "";
        if (titleField.getText().trim().isEmpty()) { s += "\tTitle\n"; }
        if (descriptionField.getText().trim().isEmpty()) { s += "\tDescription\n"; }
        if (locationField.getText().trim().isEmpty()) { s += "\tLocation\n"; }
        if (contactComboBox.getSelectionModel().isEmpty()) { s += "\tContact\n"; }
        if (typeField.getText().trim().isEmpty()) { s += "\tType\n"; }
        if (startDatePicker.getValue() == null) { s += "\tStart Date\n"; }
        if (startTimeComboBox.getSelectionModel().isEmpty()) { s += "\tStart Time\n"; }
        if (endDatePicker.getValue() == null) { s += "\tEnd Date\n"; }
        if (endTimeComboBox.getSelectionModel().isEmpty()) { s += "\tEnd Time\n"; }
        if (customerComboBox.getSelectionModel().isEmpty()) { s += "\tCustomer\n"; }
        if (userComboBox.getSelectionModel().isEmpty()) { s += "\tUser\n"; }

        if (!s.isEmpty()) {
            String warningMessage = "The following required fields are missing:\n\n";
            warningMessage += s;
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Warning: Required Fields Missing");
            alert.setContentText(warningMessage);
            alert.showAndWait();
            return false;
        }
        return true;
    }
}

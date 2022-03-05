package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Utility.DBAppointments;
import sample.Main;
import sample.model.Appointment;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/** This class displays appointment data. */
public class AppointmentController {
    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn, customerIdColumn, userIdColumn;
    @FXML private TableColumn<Appointment, String> titleColumn, descriptionColumn, locationColumn, contactColumn, typeColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> startColumn, endColumn;
    @FXML private RadioButton weekRadio, monthRadio;

    private Main main;

    /** This method initializes a JavaFX controller after its root element has been processed.
     Here it sets up the columns in a TableView. */
    public void initialize() {
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contact"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("end"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("customerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("userId"));
    }

    /** This method connects the Main class to the controller class. It also fills the TableView with appointment information.
     @param main a Main object  */
    public void setMain(Main main) {
        this.main = main;
        appointmentTableView.setItems(main.getAppointmentData());
        sortTableView();
    }

    /** This method sorts the a TableView by id. */
    public void sortTableView() {
        appointmentIdColumn.setSortType(TableColumn.SortType.ASCENDING);
        appointmentTableView.getSortOrder().add(appointmentIdColumn);
        appointmentTableView.sort();
    }

    /** This method handles a button click. It opens an empty appointment detail window to add data to. */
    public void onAddAppointmentClicked() throws SQLException {
        main.appointmentDetailWindow(null);
        if (weekRadio.isSelected()) {
            main.fillAppointmentWeeklyList();
        } else if (monthRadio.isSelected()) {
            main.fillAppointmentMonthlyList();
        } else {
            main.fillAppointmentList();
        }
        sortTableView();
    }

    /** This method handles a button click. It alerts if no appointment is selected, or opens the appointment detail window. */
    public void onEditAppointmentClicked() throws SQLException {
        int index = appointmentTableView.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No appointment selected");
            alert.showAndWait();
        } else {
            Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();
            main.appointmentDetailWindow(appointment);
            if (weekRadio.isSelected()) {
                main.fillAppointmentWeeklyList();
            } else if (monthRadio.isSelected()) {
                main.fillAppointmentMonthlyList();
            } else {
                main.fillAppointmentList();
            }
            sortTableView();
        }
    }

    /** This method handles a button click.
     It alerts if an appointment isn't selected, or it alerts asking "Are you sure".
     Finally it alerts confirming that the appointment has been deleted. */
    public void onDeleteAppointmentClicked() {
        int index = appointmentTableView.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No appointment selected");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();
                try {
                    DBAppointments.deleteAppointment(appointment);
                    if (weekRadio.isSelected()) {
                        main.fillAppointmentWeeklyList();
                    } else if (monthRadio.isSelected()) {
                        main.fillAppointmentMonthlyList();
                    } else {
                        main.fillAppointmentList();
                    }
                    sortTableView();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Appointment ID: " + appointment.getId() + ", " + appointment.getType() + " Cancelled");
                    alert.showAndWait();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    /** This method handles a radio button selection. It adds all appointments to the TableView. */
    public void onAllAppointmentsRadioSelected() throws SQLException {
        main.fillAppointmentList();
        sortTableView();
    }

    /** This method handles a radio button selection. It adds all appointments for the current month to the TableView. */
    public void onMonthlyRadioSelected() throws SQLException {
        main.fillAppointmentMonthlyList();
        sortTableView();
    }

    /** This method handles a radio button selection. It adds all appointments for the current week to the TableView. */
    public void onWeeklyRadioSelected() throws SQLException {
        main.fillAppointmentWeeklyList();
        sortTableView();
    }

    /** This method handles a radio button selection. I opens the customer window. */
    public void onCustomerRadioSelected() { main.customerWindow(); }

    /** This method handles a selection from a menu. It opens an appointment by type and date report. */
    public void onAppointmentReportClicked() { main.appointmentReportWindow();  }

    /** This method handles a selection from a menu. It opens an appointment by contact report. */
    public void onContactReportClicked() { main.contactReportWindow(); }

    /** This method handles a selection from a menu. It opens an appointment by customer report. */
    public void onCustomerReportClicked() { main.customerReportWindow(); }

    /** This method closes the program */
    public void onCloseClicked() { main.closeWindow(); }
}

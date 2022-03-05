package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Main;
import sample.Utility.DBAppointments;
import sample.model.*;

import java.sql.SQLException;

/** This class creates an appointment by contact report. */
public class ContactReportController {
    @FXML private TableView<Appointment> contactScheduleTableView;
    @FXML private TableColumn<Appointment, String> appointmentIdColumn, titleColumn, typeColumn, descriptionColumn, startColumn, endColumn, customerIdColumn;
    @FXML private ComboBox<String> contactComboBox;

    private Main main;
    private Stage newStage;
    private ObservableList<String> contactReportList = FXCollections.observableArrayList();
    private ObservableList<Appointment> contactScheduleList = FXCollections.observableArrayList();

    /** This method initializes a JavaFX controller after its root element has been processed.
     Here it sets the items in a ComboBox and sets up the columns of a TableView. */
    public void initialize() throws SQLException {
        DBAppointments.fillContactNameList(contactReportList);
        contactComboBox.setItems(contactReportList);
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        startColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("customerId"));
    }

    /** This method connects the Main class to the controller class.
     @param main a Main object
     @param newStage a Stage object  */
    public void setMain(Main main, Stage newStage) throws SQLException {
        this.main = main;
        this.newStage = newStage;
    }

    /** This method handles a button click. It alerts the user if no contact is selected, or it generates an appointment report for the selected contact. */
    public void onButtonClicked() throws SQLException {
        if (contactComboBox.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Your report is missing information");
            alert.setContentText("Contact is required, please select a contact");
            alert.showAndWait();
        } else {
            String contactName = contactComboBox.getSelectionModel().getSelectedItem();
            contactScheduleTableView.getItems().clear();
            DBAppointments.fillContactScheduleList(contactScheduleList, contactName);
            contactScheduleTableView.setItems(contactScheduleList);
        }
    }
}

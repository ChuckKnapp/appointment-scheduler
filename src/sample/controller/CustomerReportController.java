package sample.controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Main;
import sample.Utility.*;
import sample.model.Appointment;
import java.sql.SQLException;

/** This class creates a report of appointments by customer. */
public class CustomerReportController {
    @FXML private TableView<Appointment> customerScheduleTableView;
    @FXML private TableColumn<Appointment, String> appointmentIdColumn, titleColumn, typeColumn, descriptionColumn, startColumn, endColumn, customerIdColumn;
    @FXML private ComboBox<String> customerComboBox;

    private Main main;
    private Stage newStage;
    private ObservableList<String> customerReportList = FXCollections.observableArrayList();
    private ObservableList<Appointment> customerScheduleList = FXCollections.observableArrayList();

    /** This method initializes a JavaFX controller after its root element has been processed.
     Here it works to set the items in a ComboBox and a TableView.*/
    public void initialize() throws SQLException {
        DBCustomers.fillCustomerReportList(customerReportList);
        customerComboBox.setItems(customerReportList);
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

    /** This method handles a button click. If no customer is selected, it alerts the user. */
    public void onButtonClicked() throws SQLException {
        if (customerComboBox.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Your report is missing information");
            alert.setContentText("Customer is required, please select a customer");
            alert.showAndWait();
        } else {
            String customerName = customerComboBox.getSelectionModel().getSelectedItem();
            customerScheduleTableView.getItems().clear();
            DBAppointments.fillCustomerScheduleList(customerScheduleList, customerName);
            customerScheduleTableView.setItems(customerScheduleList);
        }
    }

}

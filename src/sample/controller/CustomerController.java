package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Utility.DBCustomers;
import sample.Main;
import sample.model.Customer;
import java.sql.*;
import java.util.Optional;

/** This class displays customer data. */
public class CustomerController {
    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, String> idColumn, customerColumn, addressColumn, postalColumn, divisionColumn, countryColumn, phoneColumn;

    private Main main;

    /** This method initializes a JavaFX controller after its root element has been processed.
     Here it sets up the columns in a TableView. */
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("id"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        postalColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("division"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("country"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNumber"));
    }

    /** This method connects the Main class to the controller class. It also fills the TableView with customer information.
     @param main a Main object  */
    public void setMain(Main main) {
        this.main = main;
        customerTableView.setItems(main.getCustomerData());
        sortTableView();
    }

    /** This method sorts a TableView by id. */
    public void sortTableView() {
        idColumn.setSortType(TableColumn.SortType.ASCENDING);
        customerTableView.getSortOrder().add(idColumn);
        customerTableView.sort();
    }

    /** This method handles a button click. It changes the current customer window to the appointment window. */
    public void onBackToAppointmentsClicked() { main.appointmentWindow(); }

    /** This method handles a button click. It opens the customer detail window */
    public void onAddButtonClicked() {
        main.customerDetailWindow(null);
        sortTableView();
    }

    /** This method handles a button click. It opens the customer detail window with the selected customer's information displayed.
     If no customer is selected, it alerts the user. */
    public void onEditCustomerClicked() {
        int index = customerTableView.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No customer selected");
            alert.showAndWait();
        } else {
            Customer customer = customerTableView.getSelectionModel().getSelectedItem();
            main.customerDetailWindow(customer);
        }
        sortTableView();
    }

    /** This method handles a button click. It deletes the selected customer. If no customer is selected, it alerts the user */
    public void onDeleteButtonClicked() throws SQLException {
        int index = customerTableView.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No customer selected");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                Customer customer = customerTableView.getSelectionModel().getSelectedItem();
                DBCustomers.deleteCustomerAppointments(customer);
                main.fillAppointmentList();
                DBCustomers.deleteCustomer(customer);
                main.fillCustomerList();
                sortTableView();
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Customer Removed");
                alert.showAndWait();
            }
        }
    }

    /** This method handles a selection from a menu. It opens an appointment by type and date report */
    public void onAppointmentReportClicked() { main.appointmentReportWindow();  }

    /** This method handles a selection from a menu. It opens an appointment by contact report */
    public void onContactReportClicked() { main.contactReportWindow(); }

    /** This method handles a selection from a menu. It opens an appointment by customer report */
    public void onCustomerReportClicked() { main.customerReportWindow(); }

    /** This method closes the program */
    public void onCloseClicked() { main.closeWindow(); }
}

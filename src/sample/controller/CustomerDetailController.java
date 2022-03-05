package sample.controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Utility.DBCustomers;
import sample.Main;
import sample.model.Customer;
import java.sql.*;

/** This class creates a customer form to add or update customer information. */
public class CustomerDetailController {
    @FXML Button saveButton, cancelButton;
    @FXML TextField customerIdField, customerField, addressField, postalField, phoneField;
    @FXML ComboBox<String> countryComboBox, divisionComboBox;

    private Main main;
    private Stage newStage;
    private Customer customer;
    private ObservableList<String> countryNameList = FXCollections.observableArrayList();
    private ObservableList<String> divisionNameList = FXCollections.observableArrayList();

    /** This method initializes a JavaFX controller after its root element has been processed.
     Here it sets items in two comboBoxes and uses a lambda expression to add a listener to the country comboBox, so that
     when a country is selected, the division comboBox will be filled with that countries first-level-divisions. */
    public void initialize() throws SQLException {
        DBCustomers.fillCountryNameList(countryNameList);
        DBCustomers.fillDivisionNameList(countryComboBox.getSelectionModel().getSelectedItem(), divisionNameList);
        countryComboBox.setItems(countryNameList);
        countryComboBox.setVisibleRowCount(8);
        countryComboBox.setPromptText("Select a Country");
        divisionComboBox.setItems(divisionNameList);
        divisionComboBox.setVisibleRowCount(8);
        divisionComboBox.setPromptText("Choose a Division");
        divisionComboBox.setVisible(false);

        countryComboBox.getSelectionModel().selectedItemProperty().addListener(((observable, oldVal, newVal) -> {
            divisionNameList.clear();
            try {
                DBCustomers.setDivisionsByCountry(newVal, divisionNameList);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            divisionComboBox.setVisible(true);
        }));
    }

    /** This method connects the Main class to the controller class.
     @param main a Main object
     @param newStage a Stage object
     @param customer a Customer object */
    public void setMain(Main main, Stage newStage, Customer customer) throws SQLException {
        this.main = main;
        this.newStage = newStage;
        this.customer = customer;
        if (customer != null) {
            fillCustomerDetails();
        }
    }

    /** This method fills the customer form with customer details. */
    public void fillCustomerDetails() {
        customerIdField.setText(String.valueOf(customer.getId()));
        customerField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        postalField.setText(customer.getPostalCode());
        phoneField.setText(customer.getPhoneNumber());
        countryComboBox.getSelectionModel().select(customer.getCountry());
        divisionComboBox.getSelectionModel().select(customer.getDivision());
        divisionComboBox.setVisible(true);
    }

    /** This method handles a button click. It adds or updates a customer. */
    @FXML public void onSaveButtonClicked() throws SQLException {
        if (!validateInput()) { return; }

        Customer dummy;
        if (customer != null) {
            dummy = new Customer(customer.getId(), customerField.getText(), addressField.getText(), postalField.getText(), phoneField.getText(),
                    divisionComboBox.getSelectionModel().getSelectedItem(), countryComboBox.getSelectionModel().getSelectedItem());
        } else {
            dummy = new Customer(-1, customerField.getText(), addressField.getText(), postalField.getText(), phoneField.getText(),
                    divisionComboBox.getSelectionModel().getSelectedItem(), countryComboBox.getSelectionModel().getSelectedItem());
        }

        int divisionId = DBCustomers.getDivisionIdSelection(divisionComboBox.getSelectionModel().getSelectedItem());
        DBCustomers.addOrUpdateCustomer(dummy, divisionId);
        main.fillCustomerList();
        newStage.close();
    }

    /** This method closes the customer detail window. */
    public void onCancelButtonClicked() {
        newStage.close();
    }

    /** This method checks to see if the data in the form is valid. It returns true if so, and if not, it generates an alert and returns false.
     @return Returns a boolean */
    public boolean validateInput() {
        String s = "";
        if (customerField.getText().trim().isEmpty()) { s += "\tCustomer Name\n"; }
        if (addressField.getText().trim().isEmpty()) { s += "\tAddress\n"; }
        if (postalField.getText().trim().isEmpty()) { s += "\tPostal Code\n"; }
        if (countryComboBox.getSelectionModel().isEmpty()) { s += "\tCountry\n\tDivision\n"; }
        if (divisionComboBox.getSelectionModel().isEmpty()) { s += "\tState / Province / Region\n"; }
        if (phoneField.getText().trim().isEmpty()) { s += "\tPhone Number"; }

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

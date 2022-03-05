package sample.controller;

import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Main;
import sample.Utility.DBAppointments;
import sample.Utility.DBMain;
import sample.model.Appointment;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.*;

/** This class creates appointment report by month and type. */
public class AppointmentReportController {
    @FXML private ComboBox<String> monthComboBox, typeComboBox;
    @FXML private Label monthLabel, typeLabel, resultLabel, reportLabel;

    private Main main;
    private Stage newStage;
    private ObservableList<String> appointmentTypeList = FXCollections.observableArrayList();
    private List<String> monthList = Stream.of("January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December")
            .collect(Collectors.toList());

    /** This method initializes a JavaFX controller after its root element has been processed.
     Here it sets the items in two ComboBoxes. */
    public void initialize() throws SQLException {
        monthComboBox.getItems().addAll(monthList);
        DBAppointments.fillAppointmentTypeList(appointmentTypeList);
        ObservableList<String> distinctAppointmentTypes = FXCollections.observableArrayList();
        distinctAppointmentTypes.addAll(appointmentTypeList.stream().distinct().collect(Collectors.toList()));
        typeComboBox.setItems(distinctAppointmentTypes);
    }

    /** This method connects the Main class to the controller class.
     @param main a Main object
     @param newStage a Stage object  */
    public void setMain(Main main, Stage newStage) throws SQLException {
        this.main = main;
        this.newStage = newStage;
    }

    /** This method handles a button click. It generates a report of the total number of appointments by the selected month and date.
     It alerts the user if month or type isn't selected.
     It includes a lambda expression that filters a stream of appointments to calculate the result. */
    public void onButtonClicked() throws SQLException {
        String alertString = "";
        if (monthComboBox.getSelectionModel().getSelectedItem() == null)
            alertString += "Month is required, please select a month\n";
        if (typeComboBox.getSelectionModel().getSelectedItem() == null)
            alertString += "Type is required, please select a type";

        if (!alertString.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Your report is missing information");
            alert.setContentText(alertString);
            alert.showAndWait();
        } else {
            String month = monthComboBox.getSelectionModel().getSelectedItem();
            String type = typeComboBox.getSelectionModel().getSelectedItem();

            List<Appointment> list = main.getAppointmentData();
            long streamResult = list.stream()
                    .filter(s -> s.getStart().getMonth().toString().equals(month.toUpperCase()))
                    .filter(s -> s.getType().equals(type))
                    .count();

            resultLabel.setVisible(true);
            monthLabel.setVisible(true);
            typeLabel.setVisible(true);
            reportLabel.setVisible(true);
            monthLabel.setText(monthComboBox.getSelectionModel().getSelectedItem());
            typeLabel.setText(typeComboBox.getSelectionModel().getSelectedItem());
            resultLabel.setText(String.valueOf(streamResult));
            reportLabel.setText("For the month of " + monthComboBox.getSelectionModel().getSelectedItem() +
                    " the number of Appointments of Type: " + typeComboBox.getSelectionModel().getSelectedItem() +
                    " is " + streamResult);
        }
    }
}

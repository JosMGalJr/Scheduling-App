package Controllers;

import Classes.Customer;
import Classes.Directory;
import Classes.Division;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustController implements Initializable {

    private enum MODE {ADD, UPDATE}

    private MODE mode;
    public Label titleLabel;
    public TextField IDText;
    public TextField nameText;
    public TextField addressText;
    public TextField postalText;
    public TextField phoneText;
    public ComboBox<String> divisionCombo;
    public ComboBox<String> countryCombo;

    private Directory directory = null;

    /**
     * Lambda Expression - Expression is used to populate the combo box for the countries.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        directory = new Directory();

        ObservableList<String> countryNames = FXCollections.observableArrayList();

        //Lambda Expression
        directory.getAllCountries().forEach((n) -> countryNames.add(n.getName()));
        countryCombo.getItems().addAll(countryNames);


    }

    /**
     * Initializes Customer From in Add Mode.
     * Title is set to 'Add Customer'.
     */
    public void addMode() {
        mode = MODE.ADD;
        titleLabel.setText("Add Customer");
    }

    public void updateMode(Customer updateCustomer) {
        mode = MODE.UPDATE;
        titleLabel.setText("Update Customer");
        IDText.setText(String.valueOf(updateCustomer.getId()));
        nameText.setText(updateCustomer.getName());
        addressText.setText(updateCustomer.getAddress());
        postalText.setText(updateCustomer.getPostalCode());
        phoneText.setText(updateCustomer.getPhoneNum());

        countryCombo.setValue(updateCustomer.getCountry());
        onCountrySelected(new ActionEvent());
        divisionCombo.setValue(updateCustomer.getDivision());
    }

    public void onSave(ActionEvent actionEvent) throws IOException {
        String name = nameText.getText();
        String address = addressText.getText();
        String postalCode = postalText.getText();
        String phoneNum = phoneText.getText();

        boolean errorCheck = false;
        //All text fields must have input
        if (!name.isEmpty()) {
            nameText.setStyle(null);
        } else {
            nameText.setStyle("-fx-border-color: rgb(255, 0, 0);");
            errorCheck = true;
        }
        if (!address.isEmpty()) {
            addressText.setStyle(null);
        } else {
            addressText.setStyle("-fx-border-color: rgb(255, 0, 0);");
            errorCheck = true;
        }
        if (!postalCode.isEmpty()) {
            postalText.setStyle(null);
        } else {
            postalText.setStyle("-fx-border-color: rgb(255, 0, 0);");
            errorCheck = true;
        }
        if (!phoneNum.isEmpty()) {
            phoneText.setStyle(null);
        } else {
            phoneText.setStyle("-fx-border-color: rgb(255, 0, 0);");
            errorCheck = true;
        }

        //All Combo Boxes MUST have selection
        if (countryCombo.getValue() != null) {
            countryCombo.setStyle(null);
        } else {
            countryCombo.setStyle("-fx-border-color: rgb(255, 0, 0);");
            errorCheck = true;
        }
        if (divisionCombo.getValue() != null) {
            divisionCombo.setStyle(null);
        } else {
            divisionCombo.setStyle("-fx-border-color: rgb(255, 0, 0);");
            errorCheck = true;
        }

        if (errorCheck) {
            
        } else {
            Customer newCustomer = new Customer();
            newCustomer.setName(name);
            newCustomer.setAddress(address);
            newCustomer.setPostalCode(postalCode);
            newCustomer.setPhoneNum(phoneNum);

            int countryIndex = countryCombo.getItems().indexOf(countryCombo.getValue());
            int divisionIndex = divisionCombo.getItems().indexOf(divisionCombo.getValue());
            int divisionID = directory.getAllCountries().get(countryIndex).getAllDivisions().get(divisionIndex).getId();
            newCustomer.setDivisionID(divisionID);

            if (mode == MODE.ADD) {
                if (!directory.addCustomer(newCustomer)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Add Failed.");
                    alert.setContentText("Could not add Customer. Please check database connection.");
                    alert.showAndWait();
                }
            } else if (mode == MODE.UPDATE) {
                newCustomer.setId(Integer.parseInt(IDText.getText()));
                if (!directory.updateCustomer(newCustomer)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Add Failed.");
                    alert.setContentText("Could not update Customer. Please check database connection.");
                    alert.showAndWait();
                }
            }

            toHome(actionEvent);
        }
    }

    /**
     * Populates Division combo box when a Country is selected.
     */
    public void onCountrySelected(ActionEvent actionEvent) {
        int countryIndex = countryCombo.getItems().indexOf(countryCombo.getValue());
        ObservableList<String> divisionNames = FXCollections.observableArrayList();
        for (Division division : directory.getAllCountries().get(countryIndex).getAllDivisions()) {
            divisionNames.add(division.getName());
        }
        divisionCombo.getItems().clear();
        divisionCombo.getItems().addAll(divisionNames);
        divisionCombo.setValue(null);
    }

    /**
     * Transfers back to All Customers From.
     * Triggered when the 'Cancel' button is selected.
     * */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        toHome(actionEvent);
    }

    private void toHome(ActionEvent actionEvent) throws IOException {
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}















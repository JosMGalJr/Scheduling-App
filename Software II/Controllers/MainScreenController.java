package Controllers;

import Classes.Appointment;
import Classes.Calendar;
import Classes.Directory;
import Classes.Customer;
import Helper.JDBC;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    /**
     * Setup appointment table
     */

    @FXML
    TableView<Appointment> allAppointmentsTable;
    @FXML
    TableColumn<? , ? > colAptID;
    @FXML
    TableColumn<?, ?> colAptTitle;
    @FXML
    TableColumn<?, ?> colAptDescription;
    @FXML
    TableColumn<?, ?> colAptLocation;
    @FXML
    TableColumn<?, ?> colAptContact;
    @FXML
    TableColumn<?, ?> colAptType;
    @FXML
    TableColumn<?, ?> colAptStart;
    @FXML
    TableColumn<?, ?> colAptEnd;
    @FXML
    TableColumn<?, ?> colAptCustomerID;
    @FXML
    TableColumn<?, ?> colAptUserID;
    @FXML Button addAptButton;
    @FXML Button updateAptButton;
    @FXML Button deleteAptButton;
    @FXML RadioButton allRadio;
    @FXML RadioButton weekRadio;
    @FXML RadioButton monthRadio;
    @FXML ToggleGroup aptToggle;
    private ObservableList<Classes.Appointment> displayAppointments = FXCollections.observableArrayList();
    private Calendar appointmentCalendar = null;

    /**
     * Setup customer table
     */

    @FXML
    TableView<Customer> allCustomersTable;
    @FXML
    TableColumn<?, ?> colCustID;
    @FXML
    TableColumn<?, ?> colCustName;
    @FXML
    TableColumn<?, ? > colCustAddress;
    @FXML
    TableColumn<? , ? > colCustPostal;
    @FXML
    TableColumn<? , ? > colCustDiv;
    @FXML
    TableColumn<? , ?> colCustCountry;
    @FXML
    TableColumn<? , ? > colCustPhone;
    private ObservableList<Classes.Customer> displayCustomers = FXCollections.observableArrayList();
    private Directory directory = null;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /**
         * Initialize appointment information
         */
        appointmentCalendar = new Calendar();

        displayAppointments = appointmentCalendar.getAllAppointments();
        allAppointmentsTable.setItems(displayAppointments);

        colAptID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAptDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAptLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colAptContact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        colAptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAptStart.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        colAptEnd.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        colAptCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colAptUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));

        aptToggle.selectToggle(allRadio);

        /**
         * Initialize customer information
         */

        directory = new Directory();

        displayCustomers = directory.getAllCustomers();
        allCustomersTable.setItems(displayCustomers);

        colCustID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCustAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCustPostal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        colCustDiv.setCellValueFactory(new PropertyValueFactory<>("division"));
        colCustCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        colCustPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));

    }


    /**
     * Displays all Appointments in the database.
     * Triggered by the 'All Appointments'(radioAll) radio button.
     * */
    public void viewAll() {
        displayAppointments = appointmentCalendar.getAllAppointments();
        allAppointmentsTable.setItems(displayAppointments);
    }

    /**
     * Displays Appointments in the database scheduled this month.
     * Triggered by the 'Appointments this Month'(monthRadio) radio button.
     * */
    public void viewMonth() {
        displayAppointments.clear();

        ObservableList<Appointment> allAppointments = appointmentCalendar.getAllAppointments();
        Month thisMonth = LocalDate.now().getMonth();
        allAppointments.stream().filter(n -> n.getStartDateTimeLocal().getMonth() == thisMonth)
                .forEach(n -> displayAppointments.add(n));
    }

    /**
     * Displays Appointments in the database scheduled this week.
     * Week is defined as Monday through Sunday.
     * Triggered by the 'Appointments this Week'(radioWeek) radio button.
     * */
    public void viewWeek() {
        displayAppointments.clear();
        int dayOfWeek = LocalDate.now().getDayOfWeek().getValue();
        LocalDate monday = LocalDate.now().minusDays(dayOfWeek - 1);
        LocalDate sunday = monday.plusDays(6);

        for (Appointment appointment : appointmentCalendar.getAllAppointments()) {
            LocalDate meetingStartDate = appointment.getStartDateTimeLocal().toLocalDate();
            LocalDate meetingEndDate = appointment.getEndDateTimeLocal().toLocalDate();
            if (meetingStartDate.compareTo(monday) * meetingStartDate.compareTo(sunday) <= 0) {
                displayAppointments.add(appointment);
            } else if (meetingEndDate.compareTo(monday) * meetingEndDate.compareTo(sunday) <= 0) {
                displayAppointments.add(appointment);
            }
        }
    }

    /**
     * Opens up Appointment form in "Add" mode
     */
    public void aptGoToAdd(ActionEvent actionEvent) {
        try {
            final Node source = (Node) actionEvent.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddAppointment.fxml"));
            Parent root = loader.load();
            ApptController apptController = loader.getController();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            apptController.addApptsMode();
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to Open Form.");
            alert.setContentText("Unable to access 'Add Appointment'. Please check database connection.");
            alert.showAndWait();
        }
    }

    /**
     * Opens up Appointment form in "Update" mode
     */

    public void aptGoToUpdate(ActionEvent actionEvent) throws IOException {
        Appointment selectedAppointment = allAppointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) return;

        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/View/AddAppointment.fxml")));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ApptController apptController = loader.getController();
        stage.setScene(scene);
        apptController.updateApptsMode(selectedAppointment);
        stage.show();
    }

    /**
     * Deletes selected Appointment
     */
    public void onDeleteApt() {
        Appointment selectedAppointment = allAppointmentsTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Appointment");
        alert.setHeaderText("ID: " + selectedAppointment.getId() + "\nTitle: " + selectedAppointment.getTitle() + "\nType: " + selectedAppointment.getType());
        alert.setContentText("Are you sure you want to delete this appointment?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        if (!appointmentCalendar.deleteAppointment(selectedAppointment)) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to Perform Action.");
            alert.setContentText("Could not delete Appointment. Please check database connection.");
            alert.showAndWait();
        } else {
            if (aptToggle.getSelectedToggle() == allRadio) {
                viewAll();
            } else if (aptToggle.getSelectedToggle() == monthRadio) {
                viewMonth();
            } else if (aptToggle.getSelectedToggle() == weekRadio) {
                viewWeek();
            }

        }
    }

    /**
     * Opens up Customer form in "Add" Mode
     */
    public void custGoToAdd(ActionEvent actionEvent) {
        try {
            final Node source = (Node) actionEvent.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AddCustomer.fxml"));
            Parent root = loader.load();
            CustController custController = loader.getController();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            custController.addMode();
            stage.show();


        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to Open Form.");
            alert.setContentText("Unable to access 'Add Customer'. Please check database connection.");
            alert.showAndWait();
        }
    }

    /**
     * Opens up Customer form in "Update" Mode
     */
    public void custGoToUpdate(ActionEvent actionEvent) throws IOException {

        Customer selectedCustomer = allCustomersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) return;

        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/View/AddCustomer.fxml")));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        CustController custController = loader.getController();
        stage.setScene(scene);
        custController.updateMode(selectedCustomer);
        stage.show();



        }


    /**
     * Deletes selected Customer
     */
    public void onDeleteCust( ActionEvent actionEvent) throws IOException {
        Customer selectedCustomer = allCustomersTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Customer");
        alert.setHeaderText(selectedCustomer.getName());
        alert.setContentText("Do you want to delete this customer?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) {
            return;
        }
        if (directory.deleteCustomer(selectedCustomer)) {
            displayCustomers = directory.getAllCustomers();
            allCustomersTable.setItems(displayCustomers);
            updateAppts();





        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to perform action.");
            alert.setContentText("Unable to delete customer. Please check database connection.");
            alert.showAndWait();
        }



    }

    /**
     * Goes to Reports form
     */

    public void goToReports(ActionEvent actionEvent) throws IOException {

        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/ThreeReports.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }

    public void updateAppts(){
        appointmentCalendar = new Calendar();

        displayAppointments = appointmentCalendar.getAllAppointments();
        allAppointmentsTable.setItems(displayAppointments);

        colAptID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAptDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAptLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colAptContact.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        colAptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colAptStart.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        colAptEnd.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        colAptCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colAptUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));

        aptToggle.selectToggle(allRadio);
    }


    /**
     * Closes database connection and exits program
     */
    public void onExit(){
        JDBC.closeConnection();
        Platform.exit();
    }

}





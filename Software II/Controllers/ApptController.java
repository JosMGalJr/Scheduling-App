package Controllers;
import Classes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class ApptController implements Initializable {


    private enum MODE {ADD_APPTS, UPDATE_APPTS}

    private MODE mode;

    public Label titleLabel;
    public TextField IDText;
    public TextField titleText;
    public TextField descriptionText;
    public TextField locationText;
    public TextField typeText;
    public ComboBox<String> customerCombo;
    public ComboBox<String> userCombo;
    public DatePicker dateStart;
    public ComboBox<String> comboStart;
    public DatePicker dateEnd;
    public ComboBox<String> comboEnd;
    public ComboBox<String> contactCombo;

    private Directory directory = null;
    private Calendar appointmentCalendar = null;

    private final DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("hh:mm a");

    private final String delimiter = ": ";


    /**
     * Lambda Expression - Expression is used to populate the Combo Boxes for Customers, Users, and Contacts.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        directory = new Directory();
        appointmentCalendar = new Calendar();

        BiConsumer<Person, ComboBox<String>> comboAdder = (x, y) -> y.getItems().add(x.getId() + delimiter + x.getName());
        directory.getAllCustomers().forEach(x -> comboAdder.accept(x, customerCombo));
        directory.getAllUsers().forEach(x -> comboAdder.accept(x, userCombo));
        directory.getAllContacts().forEach(x -> comboAdder.accept(x, contactCombo));

        getComboStart().getItems().addAll(getMeetingTimes());
        comboEnd.getItems().addAll(getComboStart().getItems());




    }

    private ComboBox<String> getComboStart() {
        return comboStart;
    }

    private ObservableList<String> getMeetingTimes() {
        ObservableList<String> meetingTimes = FXCollections.observableArrayList();

        LocalTime midnight = LocalTime.of(0, 0);
        meetingTimes.add(midnight.format(formatTime));

        int minutes = 15;
        LocalTime meetingTime = midnight.plusMinutes(minutes);
        while (!meetingTime.equals(midnight)) {
            meetingTimes.add(meetingTime.format(formatTime));
            meetingTime = meetingTime.plusMinutes(minutes);
        }

        return meetingTimes;
    }

    /**
     * Initializes Appointment Form in Add Mode.
     * Title is set to 'Add Appointment'. Dates are automatically defaulted to the next closest half/full hour from current time.
     * */
    public void addApptsMode() {
        mode = MODE.ADD_APPTS;
        titleLabel.setText("Add Appointment");
        dateStart.setValue(LocalDate.now());
        dateEnd.setValue(LocalDate.now());

        int hr = LocalTime.now().getHour();
        LocalTime d_StartTime;
        LocalTime d_EndTime;
        if (LocalTime.now().getMinute() < 30) {
            d_StartTime = LocalTime.of(hr, 30);
            ++hr;
            d_EndTime = LocalTime.of(hr, 0);
        } else {
            ++hr;
            d_StartTime = LocalTime.of(hr, 0);
            d_EndTime = LocalTime.of(hr, 30);
        }

        getComboStart().setValue(d_StartTime.format(formatTime));
        comboEnd.setValue(d_EndTime.format(formatTime));
    }

    public void updateApptsMode(Appointment updateAppointment) {
        mode = MODE.UPDATE_APPTS;

        titleLabel.setText("Update Appointment");

        IDText.setText(String.valueOf(updateAppointment.getId()));
        titleText.setText(updateAppointment.getTitle());
        descriptionText.setText(updateAppointment.getDescription());
        locationText.setText(updateAppointment.getLocation());
        typeText.setText(updateAppointment.getType());

        dateStart.setValue(updateAppointment.getStartDateTimeLocal().toLocalDate());
        dateEnd.setValue(updateAppointment.getEndDateTimeLocal().toLocalDate());

        getComboStart().setValue(updateAppointment.getStartDateTimeLocal().toLocalTime().format(formatTime));
        comboEnd.setValue(updateAppointment.getEndDateTimeLocal().toLocalTime().format(formatTime));

        Customer customer = directory.getCustomer(updateAppointment.getCustomerID());
        User user = directory.getUser(updateAppointment.getUserID());
        Contact contact = directory.getContact(updateAppointment.getContactID());

        customerCombo.setValue(customer.getId() + delimiter + customer.getName());
        userCombo.setValue(user.getId() + delimiter + user.getName());
        contactCombo.setValue(contact.getId() + delimiter + contact.getName());
    }

    public void onSave(ActionEvent actionEvent) throws IOException {
        String title = titleText.getText();
        String description = descriptionText.getText();
        String location = locationText.getText();
        String type = typeText.getText();

        LocalDate startDate = dateStart.getValue();
        LocalDate endDate = dateEnd.getValue();
        LocalTime startTime = LocalTime.parse(getComboStart().getValue(), formatTime);
        LocalTime endTime = LocalTime.parse(comboEnd.getValue(), formatTime);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        boolean errorChecker = false;
        //EVERY text field must have an input - this
        if (!title.isEmpty()) {
            titleText.setStyle(null);
        } else {
            errorChecker = true;
            titleText.setStyle("-fx-border-color: rgb(255, 0, 0);");
        }
        if (!description.isEmpty()) {
            descriptionText.setStyle(null);
        } else {
            errorChecker = true;
            descriptionText.setStyle("-fx-border-color: rgb(255, 0, 0);");
        }
        if (!location.isEmpty()) {
            locationText.setStyle(null);
        } else {
            errorChecker = true;
            locationText.setStyle("-fx-border-color: rgb(255, 0, 0);");
        }
        if (!type.isEmpty()) {
            typeText.setStyle(null);
        } else {
            errorChecker = true;
            typeText.setStyle("-fx-border-color: rgb(255, 0, 0);");
        }

        //End time MUST be after Start time
        if (endDateTime.compareTo(startDateTime) > 0) {
            dateStart.setStyle(null);
            dateEnd.setStyle(null);
            getComboStart().setStyle(null);
            comboEnd.setStyle(null);

        } else {
            errorChecker = true;
            dateStart.setStyle("-fx-border-color: rgb(255, 0, 0);");
            dateEnd.setStyle("-fx-border-color: rgb(255, 0, 0);");
            getComboStart().setStyle("-fx-border-color: rgb(255, 0, 0);");
            comboEnd.setStyle("-fx-border-color: rgb(255, 0, 0);");

        }

        //Check if appointment in business hours
        if (!Calendar.withinBusinessHours(startDateTime, endDateTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Appointment Time");
            alert.setHeaderText("All appointment times must be within business hours of " +
                    "\nMonday through Friday, 8:00 a.m. to 10:00 p.m. Eastern Time.\n" +
                    "\nEntered Start Time: " + Calendar.toZoneID(startDateTime).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)) +
                    "\nEntered End Time:   " + Calendar.toZoneID(endDateTime).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG)));
            alert.showAndWait();
            dateStart.setStyle("-fx-border-color: rgb(255, 0, 0);");
            dateEnd.setStyle("-fx-border-color: rgb(255, 0, 0);");
            getComboStart().setStyle("-fx-border-color: rgb(255, 0, 0);");
            comboEnd.setStyle("-fx-border-color: rgb(255, 0, 0);");
            errorChecker = true;
        } else {
            dateStart.setStyle(null);
            dateEnd.setStyle(null);
            getComboStart().setStyle(null);
            comboEnd.setStyle(null);
        }

        Appointment newAppointment = new Appointment();

        newAppointment.setTitle(title);
        newAppointment.setDescription(description);
        newAppointment.setLocation(location);
        newAppointment.setType(type);

        newAppointment.setStartDateTime(startDateTime);
        newAppointment.setEndDateTime(endDateTime);

        //Combo Boxes MUST all have selection
        if (customerCombo.getValue() != null) {
            customerCombo.setStyle(null);
        } else {
            errorChecker = true;
            customerCombo.setStyle("-fx-border-color: rgb(255, 0, 0);");
        }

        if (userCombo.getValue() != null) {
            userCombo.setStyle(null);

            String userID = userCombo.getValue().split(delimiter)[0];
            newAppointment.setUserID(Integer.parseInt(userID));

            if (mode == MODE.UPDATE_APPTS) newAppointment.setId(Integer.parseInt(IDText.getText()));

            //Overlapping Appointments
            ObservableList<Appointment> overlappingAppointments = FXCollections.observableArrayList();
            //Lambda Expression
            appointmentCalendar.getAllAppointments().forEach((n) -> {
                if (n.overlap(newAppointment)) overlappingAppointments.add(n);
            });
            if (overlappingAppointments.size() > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Appointment Overlap");
                StringBuilder headerText = new StringBuilder("Appointment overlaps with existing appointment(s): ");
                for (Appointment appointment : overlappingAppointments) {
                    headerText.append("\n\nAppointment ID: ").append(appointment.getId())
                            .append("\nStart Time: ").append(appointment.getStartDateTime())
                            .append("\nEnd Time:   ").append(appointment.getEndDateTime());
                }
                alert.setHeaderText(headerText.toString());
                dateStart.setStyle("-fx-border-color: rgb(255, 0, 0);");
                dateEnd.setStyle("-fx-border-color: rgb(255, 0, 0);");
                getComboStart().setStyle("-fx-border-color: rgb(255, 0, 0);");
                comboEnd.setStyle("-fx-border-color: rgb(255, 0, 0);");
                alert.showAndWait();
                errorChecker = true;
            } else {
                dateStart.setStyle(null);
                dateEnd.setStyle(null);
                getComboStart().setStyle(null);
                comboEnd.setStyle(null);
            }
        } else {
            errorChecker = true;
            userCombo.setStyle("-fx-border-color: rgb(255, 0, 0);");
        }

        if (contactCombo.getValue() != null) {
            contactCombo.setStyle(null);
        } else {
            errorChecker = true;
            contactCombo.setStyle("-fx-border-color: rgb(255, 0, 0);");
        }

        if (!errorChecker) {
            String customerID = customerCombo.getValue().split(delimiter)[0];
            String contactID = contactCombo.getValue().split(delimiter)[0];

            newAppointment.setCustomerID(Integer.parseInt(customerID));
            newAppointment.setContactID(Integer.parseInt(contactID));


            if (mode == MODE.ADD_APPTS) {
                if (!appointmentCalendar.addAppointment(newAppointment)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Add Failed.");
                    alert.setContentText("Could not add Appointment. Please check database connection.");
                    alert.showAndWait();
                }
            } else if (mode == MODE.UPDATE_APPTS) {
                newAppointment.setId(Integer.parseInt(IDText.getText()));
                if (!appointmentCalendar.updateAppointment(newAppointment)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Update Failed.");
                    alert.setContentText("Could not update Appointment. Please check database connection.");
                    alert.showAndWait();
                }
            }
            toHome(actionEvent);
        }
    }

    /**
     * Returns to All Appointments Form when 'Cancel' button is selected.
     * */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        toHome(actionEvent);
    }

    /**
     * Returns to Main Form.
     * */
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






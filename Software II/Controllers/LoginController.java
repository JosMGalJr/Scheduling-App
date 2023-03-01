package Controllers;

import Classes.Appointment;
import Classes.Calendar;
import Classes.User;
import Helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private ResourceBundle lb;
    static boolean loggedIn;

    public TextField userID;
    public PasswordField password;
    public Label labelLocation;
    public Button Login;

    public LoginController() {
    }

    /**
     * Opens connection to database, as well as displays user's location & language (French or English)
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();
        loggedIn = false;

        //System Language
        Locale locale = Locale.getDefault();

        lb = ResourceBundle.getBundle("LanguageBundle_" + locale.getLanguage());
        Login.setText(lb.getString("login"));
        labelLocation.setText(lb.getString("location") + ZoneId.systemDefault());
    }

    /**
     */
    public void onLoginEvent(ActionEvent actionEvent) throws IOException {
        if (JDBC.login(userID.getText(), password.getText())) {
            showApptReminders();
            final Node source = (Node) actionEvent.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();



        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(lb.getString("error"));
            alert.setTitle(lb.getString("errorTitle"));
            alert.setContentText(lb.getString("errorText"));
            alert.showAndWait();
        }
    }

    /**
     * Displays reminder if any appointments begin within 15 minutes.
     */
    public void showApptReminders() {
        Calendar appointmentCalendar = new Calendar();
        Appointment nextAppointment = null;
        User loginUser = JDBC.getLoginUser();

        LocalDateTime timeNow = LocalDateTime.now();
        for (Appointment appointment : appointmentCalendar.getAllAppointments()) {
            LocalDateTime startDateTime = appointment.getStartDateTimeLocal();
            if (timeNow.toLocalDate().equals(startDateTime.toLocalDate())) {
                if (appointment.getUserID() == loginUser.getId()) {
                    long timeToAppointment = Duration.between(timeNow, startDateTime).toMinutes();
                    if (timeToAppointment <= 15 && timeToAppointment >= 0) {
                        nextAppointment = appointment;
                        break;
                    }
                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upcoming Appointment");
        if (nextAppointment != null) {
            alert.setHeaderText("You have an upcoming appointment.");
            alert.setContentText("Appointment ID: " + nextAppointment.getId() + "\n" + "Appointment Date: " + nextAppointment.getStartDateTime() + "\n" + "UserID: " + nextAppointment.getUserID());
        } else {
            alert.setHeaderText("No upcoming appointments.");
        }
        alert.showAndWait();
    }
}

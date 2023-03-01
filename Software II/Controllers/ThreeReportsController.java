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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;


public class ThreeReportsController implements Initializable {

    public TableView<ReportOne> reportOne;
    public TableColumn<Object, Object> monthCol;
    public TableColumn<Object, Object> aptTypeCol;
    public TableColumn<Object, Object> countCol;

    public TableView<Appointment> reportTwo;
    public ComboBox<String> contactCombo;
    public TableColumn<?, ?> ID;
    public TableColumn<?, ?> title;
    public TableColumn<?, ?> description;
    public TableColumn<?, ?> type;
    public TableColumn<?, ?> start;
    public TableColumn<?, ?> end;
    public TableColumn<?, ?> custID;

    public TableView reportThree;
    public TableColumn customer;
    public TableColumn<?, ?> hours;


    private final ObservableList<ReportOne> showReportOne = FXCollections.observableArrayList();
    private final ObservableList<Appointment> displayAppointments = FXCollections.observableArrayList();
    private final ObservableList<ReportThree> showReportThree = FXCollections.observableArrayList();
    private Calendar appointmentCalendar = null;
    private Directory directory = null;

    private final String delimiter = ": ";









    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        appointmentCalendar = new Calendar();
        directory = new Directory();


        /**
         * Initialization of Report One
         */

        fillReportOne();
        reportOne.setItems(showReportOne);

        aptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        monthCol.setCellValueFactory(new PropertyValueFactory<>("monthYear"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));

        /**
         * Initialization of Report Two
         */

        reportTwo.setItems(displayAppointments);

        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        end.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        custID.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        BiConsumer<Person, ComboBox<String>> comboAdder = (n, c) -> c.getItems().add(n.getId() + delimiter + n.getName());
        directory.getAllContacts().forEach(n -> comboAdder.accept(n, contactCombo));


        /**
         * Initialization of Report Three
         */

        showReportThree();
        reportThree.setItems(showReportThree);

        customer.setCellValueFactory(new PropertyValueFactory<>("name"));
        hours.setCellValueFactory(new PropertyValueFactory<>("hrs"));

        reportThree.getSortOrder().add(customer);
        reportThree.getSortOrder().clear();




    }


    /**
     * Fills Report One table with data
     */
    public void fillReportOne() {
        ObservableList<Appointment> allAppointments = appointmentCalendar.getAllAppointments();

        Appointment appointment = allAppointments.get(0);
        int month = appointment.getStartDateTimeLocal().getMonth().getValue();
        int year = appointment.getStartDateTimeLocal().getYear();
        String type = appointment.getType();
        showReportOne.add(new ReportOne(month, year, type));

        for (int i = 1; i < allAppointments.size(); ++i) {
            appointment = allAppointments.get(i);
            month = appointment.getStartDateTimeLocal().getMonth().getValue();
            year = appointment.getStartDateTimeLocal().getYear();
            type = appointment.getType();

            boolean counted = false;
            for (ReportOne reportOne : showReportOne) {
                if (reportOne.getMonth() == month && reportOne.getYear() == year && reportOne.getType().equals(type)) {
                    reportOne.incrementCount();
                    counted = true;
                    break;
                }
            }
            if (!counted) {
                showReportOne.add(new ReportOne(month, year, type));
            }
        }

    }
    /**
     * Populates table data for Report Two based on selected Contact.
     * */
    public void onSelectContact() {
        int contactID = Integer.parseInt(contactCombo.getValue().split(delimiter)[0]);
        displayAppointments.clear();
        //Lambda
        appointmentCalendar.getAllAppointments().forEach((n) -> {
            if (n.getContactID() == contactID) displayAppointments.add(n);
        });
    }

    public void showReportThree(){
        ObservableList<Appointment> allAppointments = appointmentCalendar.getAllAppointments();

        Appointment appointment = allAppointments.get(0);
        LocalDateTime start = appointment.getStartDateTimeLocal();
        LocalDateTime end = appointment.getEndDateTimeLocal();

        int customerID = appointment.getCustomerID();
        String name = directory.getCustomer(customerID).getName();
        double hours = Duration.between(start, end).toMinutes() / 60.0;

        showReportThree.add(new ReportThree(customerID, name, hours));
        for (int i = 1; i < allAppointments.size(); ++i) {
            appointment = allAppointments.get(i);
            start = appointment.getStartDateTimeLocal();
            end = appointment.getEndDateTimeLocal();

            customerID = appointment.getCustomerID();
            hours = Duration.between(start, end).toMinutes() / 60.0;

            boolean counted = false;
            for (ReportThree reportThree : showReportThree) {
                if (reportThree.getId() != customerID) {
                    continue;
                }
                reportThree.addHrs(hours);
                counted = true;
                break;
            }
            if (!counted) {
                name = directory.getCustomer(customerID).getName();
                showReportThree.add(new ReportThree(customerID, name, hours));
            }
        }

        for (Customer customer : directory.getAllCustomers()) {
            boolean counted = false;
            for (ReportThree reportThree : showReportThree) {
                if (customer.getId() != reportThree.getId()) {
                    continue;
                }
                counted = true;
                break;
            }
            if (!counted) {
                showReportThree.add(new ReportThree(customer.getId(), customer.getName(), 0));
            }
        }


        }



    public void goBack (ActionEvent actionEvent) throws IOException {
        final Node source = (Node) actionEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Main.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Used to display data for Report One.
     */
    public static class ReportOne {
        private int month;
        private int year;
        private String type;
        private int count;

        public ReportOne(int month, int year, String type, int count) {
            this.month = month;
            this.year = year;
            this.type = type;
            this.count = count;
        }

        public ReportOne(int month, int year, String type) {
            this.month = month;
            this.year = year;
            this.type = type;
            this.count = 1;
        }

        public ReportOne(ReportOne reportOneToCopy) {
            this.month = reportOneToCopy.month;
            this.year = reportOneToCopy.year;
            this.type = reportOneToCopy.type;
            this.count = reportOneToCopy.count;
        }

        public ReportOne() {
            month = 1;
            year = 9999;
            type = "Dummy";
            count = 0;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void incrementCount() {
            ++count;
        }

        public int getMonth() {
            return month;
        }

        public int getYear() {
            return year;
        }

        public String getMonthYear() {
            LocalDate date = LocalDate.of(year, month, 1);
            return date.format(DateTimeFormatter.ofPattern("MMM yyyy"));
        }

        public String getType() {
            return type;
        }

        public int getCount() {
            return count;
        }
    }

    public static class ReportThree extends Person {
        private double hrs;

        public ReportThree(int id, String name, double hrs) {
            super(id, name);
            this.hrs = hrs;
        }

        public ReportThree(ReportThree reportToCopy) {
            super(reportToCopy.getId(), reportToCopy.getName());
            this.hrs = reportToCopy.hrs;
        }

        public void setHours(double hrs) {
            this.hrs = hrs;
        }

        public void addHrs(double addHrs) {
            hrs += addHrs;
        }

        public double getHrs() {
            return hrs;
        }

        @Override
        public String getName() {
            return super.getId() + ": " + super.getName();
        }
    }
}

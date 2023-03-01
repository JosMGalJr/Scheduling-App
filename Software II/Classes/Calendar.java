package Classes;

import Helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Calendar to track and organize all appointments.
 */
public class Calendar {
    private static final ZoneId hqZoneID = ZoneId.of("America/New_York");
    private ObservableList<Appointment> allAppointments;


    public Calendar() {
        allAppointments = JDBC.getAllAppointments();
    }

    /**
     * Determines if the startTime and endTime of an appointment are inside business hours.
     * @param startTime Part to be copied
     * @param endTime   Part to be copied
     * @return true if in business hours , false if outside business hours
     */
    public static boolean withinBusinessHours(LocalDateTime startTime, LocalDateTime endTime) {
        //EST
        ZonedDateTime[] meetingTimes = {startTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(hqZoneID),
                endTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(hqZoneID)};

        for (ZonedDateTime meetingTime : meetingTimes) {
            if (meetingTime.getDayOfWeek().getValue() == 6 || (meetingTime.getDayOfWeek().getValue() == 7) || (meetingTime.getHour() < 8) || (meetingTime.getHour() > 22) || (meetingTime.getHour() == 22 && meetingTime.getMinute() != 0)) {
                return false;
            }

        }
        return true;
    }

    /**
     * @param dateTime Part to be copied
     * @return copy of dateTime in Eastern Time zone.
     */
    public static ZonedDateTime toZoneID(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(hqZoneID);
    }

    /**
     * @param newAppointment Appointment added to Appointment Calendar
     */
    public boolean addAppointment(Appointment newAppointment) {
        return JDBC.addAppointment(newAppointment);
    }

    /**
     * @param appointmentToUpdate Appointment data to update existing Appointment in database
     */
    public boolean updateAppointment(Appointment appointmentToUpdate) {
        return JDBC.updateAppointment(appointmentToUpdate);
    }

    /**
     * @param appointmentToDelete Appointment to be deleted from database
     * @return true if deleted, false if not deleted
     */
    public boolean deleteAppointment(Appointment appointmentToDelete) {
        if (JDBC.deleteAppointment(appointmentToDelete.getId())) {
            allAppointments = JDBC.getAllAppointments();
            return true;
        }
        return false;
    }

    /**
     * @return all Appointments in Appointment Calendar
     */
    public ObservableList<Appointment> getAllAppointments() {
        return FXCollections.observableArrayList(allAppointments);
    }
}

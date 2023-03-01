package Classes;

import Helper.JDBC;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

/**
 * Appointment Class
 */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int customerID;
    private int userID;
    private int contactID;

    /**
     * Constructor
     * @param id
     * @param title
     * @param description
     * @param location
     * @param type
     * @param startDateTime
     * @param endDateTime
     * @param customerID
     * @param userID
     * @param contactID
     */
    public Appointment(int id, String title, String description, String location, String type, LocalDateTime startDateTime, LocalDateTime endDateTime, int customerID, int userID, int contactID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * Copy Constructor
     *
     * @param appointmentToCopy Appointment to be copied.
     */
    public Appointment(Appointment appointmentToCopy) {
        this.id = appointmentToCopy.id;
        this.title = appointmentToCopy.title;
        this.description = appointmentToCopy.description;
        this.location = appointmentToCopy.location;
        this.type = appointmentToCopy.type;
        this.startDateTime = appointmentToCopy.startDateTime;
        this.endDateTime = appointmentToCopy.endDateTime;
        this.customerID = appointmentToCopy.customerID;
        this.userID = appointmentToCopy.userID;
        this.contactID = appointmentToCopy.contactID;
    }


    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title Title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description Description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location Location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type Type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return startDateTime in localized date time format, short
     */
    public String getStartDateTime() {
        return startDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    /**
     * @param startDateTimeZoned startDateTime to set from a ZonedDateTime
     */
    public void setStartDateTime(ZonedDateTime startDateTimeZoned) {
        ZoneId localZoneId = ZoneId.systemDefault();
        LocalDateTime startDateTimeLocal = startDateTimeZoned.withZoneSameInstant(localZoneId).toLocalDateTime();
        setStartDateTime(startDateTimeLocal);
    }

    /**
     * @param startDateTimeLocal startDateTime to set from a LocalDateTime
     */
    public void setStartDateTime(LocalDateTime startDateTimeLocal) {
        startDateTime = startDateTimeLocal;
    }

    /**
     * @return startDateTime
     */
    public LocalDateTime getStartDateTimeLocal() {
        return startDateTime;
    }

    /**
     * @return startDateTime with system default zone
     */
    public ZonedDateTime getStartDateTimeZoned() {
        return startDateTime.atZone(ZoneId.systemDefault());
    }

    /**
     * @return endDateTime
     */
    public String getEndDateTime() {
        return endDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    /**
     * @param endDateTimeZoned endDateTime to set from a ZonedDateTime
     */
    public void setEndDateTime(ZonedDateTime endDateTimeZoned) {
        ZoneId localZoneId = ZoneId.systemDefault();
        LocalDateTime endDateTimeLocal = endDateTimeZoned.withZoneSameInstant(localZoneId).toLocalDateTime();
        setEndDateTime(endDateTimeLocal);
    }

    /**
     * @param endDateTimeLocal endDateTime to set from a LocalDateTime
     */
    public void setEndDateTime(LocalDateTime endDateTimeLocal) {
        endDateTime = endDateTimeLocal;
    }

    /**
     * @return endDateTime in localized date time format, short
     */
    public LocalDateTime getEndDateTimeLocal() {
        return endDateTime;
    }

    /**
     * @return endDateTime with system default zone
     */
    public ZonedDateTime getEndDateTimeZoned() {
        return endDateTime.atZone(ZonedDateTime.now().getZone());
    }

    /**
     * @return customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID customerID to set
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID userID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return contactID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID contactID to set
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * @return Contact Name
     */
    public String getContactName() {
        return Objects.requireNonNull(JDBC.getContact(contactID)).getName();
    }

    /**
     * Checks if Appointment overlaps with another Appointment.
     * Returns true if meeting times overlap. Returns false if they do not.
     *
     * @param checkAppointment the Appointment to be checked against this Appointment
     */
    public boolean overlap(Appointment checkAppointment) {
        if (this.id == checkAppointment.id) return false;
        if (this.userID != checkAppointment.getUserID()) return false;

        LocalDateTime newStartTime = checkAppointment.getStartDateTimeLocal();
        LocalDateTime newEndTime = checkAppointment.getEndDateTimeLocal();

        LocalDateTime thisStartTime = this.getStartDateTimeLocal();
        LocalDateTime thisEndTime = this.getEndDateTimeLocal();

        if (newStartTime.equals(thisStartTime) && newEndTime.equals(thisEndTime)) {
            return true;
        }
        if (newStartTime.compareTo(thisStartTime) * newStartTime.compareTo(thisEndTime) < 0) {
            return true;
        }
        if (newEndTime.compareTo(thisStartTime) * newEndTime.compareTo(thisEndTime) < 0) {
            return true;
        }
        if (thisStartTime.compareTo(newStartTime) * thisStartTime.compareTo(newEndTime) < 0) {
            return true;
        }
        return thisEndTime.compareTo(newStartTime) * thisEndTime.compareTo(newEndTime) < 0;
    }

    /**
     * Dummy Constructor. Creates an Appointment with all strings set to 'Dummy' + variable name, all integers set to 0, and times set to now().
     */
    public Appointment() {
        this.id = 0;
        this.title = "Dummy Title";
        this.description = "Dummy Description";
        this.location = "Dummy Location";
        this.type = "Dummy Type";
        this.startDateTime = LocalDateTime.now();
        this.endDateTime = LocalDateTime.now();
        this.customerID = 0;
        this.userID = 0;
        this.contactID = 0;
    }
}

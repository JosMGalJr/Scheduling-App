package Classes;

/** Defines a country division.
 * */
public class Division {
    int id;
    String name;

    /**
     * Parametrized constructor.
     * @param id ID number
     * @param name Name
     * */
    public Division(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Copy constructor.
     * @param divisionToCopy the Division to be copied.
     * */
    public Division(Division divisionToCopy) {
        this.id = divisionToCopy.id;
        this.name = divisionToCopy.name;
    }

    /** Dummy Constructor. Creates a Division with ID of 0 and name 'Dummy Division'.*/
    public Division() {
        id = 0;
        name = "Dummy Division";
    }

    /**
     * @param id ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @param name Name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * @return Name
     */
    public String getName() {
        return name;
    }
}

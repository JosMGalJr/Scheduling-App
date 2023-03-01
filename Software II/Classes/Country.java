package Classes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Defines one country including divisions.
 */
public class Country {
    private int id;
    private String name;
    private ObservableList<Division> allDivisions;

    /**
     * Parametrized constructor.
     *
     * @param id           ID number
     * @param name         Name
     * @param allDivisions allDivisions
     */
    public Country(int id, String name, ObservableList<Division> allDivisions) {
        this.id = id;
        this.name = name;
        this.allDivisions = FXCollections.observableArrayList(allDivisions);
    }

    /**
     * Copy constructor.
     *
     * @param countryToCopy the Country to be copied.
     */
    public Country(Country countryToCopy) {
        this.id = countryToCopy.id;
        this.name = countryToCopy.name;
        this.allDivisions = FXCollections.observableArrayList(countryToCopy.allDivisions);
    }

    /**
     * Dummy Constructor. Creates a Country with ID of 0, name 'Dummy Country', and allDivisions empty.
     */
    public Country() {
        id = 0;
        name = "Dummy Country";
        allDivisions = FXCollections.observableArrayList();
    }

    /**
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * @param id id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return allDivisions for the Country
     */
    public ObservableList<Division> getAllDivisions() {
        return FXCollections.observableArrayList(allDivisions);
    }

    /**
     * @param allDivisions allDivisions to set
     */
    public void setAllDivisions(ObservableList<Division> allDivisions) {
        this.allDivisions = FXCollections.observableArrayList(allDivisions);
    }

}

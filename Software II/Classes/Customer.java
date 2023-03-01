package Classes;

import Helper.JDBC;

public class Customer extends Person {
    private String address;
    private String postalCode;
    private String phoneNum;
    public int divisionID;

    /**
     * Parametrized constructor.
     *
     * @param id         the ID number
     * @param name       the name
     * @param address    the address
     * @param postalCode the postalCode
     * @param phoneNum   the phoneNum
     * @param divisionID the divisionID
     */
    public Customer(int id, String name, String address, String postalCode, String phoneNum, int divisionID) {
        super(id, name);
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNum = phoneNum;
        this.divisionID = divisionID;
    }

    /**
     * Copy constructor.
     *
     * @param customerToCopy the Contact to be copied.
     */
    public Customer(Customer customerToCopy) {
        super(customerToCopy.getId(), customerToCopy.getName());
        this.address = customerToCopy.address;
        this.postalCode = customerToCopy.postalCode;
        this.phoneNum = customerToCopy.phoneNum;
        this.divisionID = customerToCopy.divisionID;
    }

    /**
     * Dummy Constructor.
     * Creates a Contact with ID of 0, name 'Dummy Name', address "Dummy Address"
     * postal code '99999', phone number '999-999-9999', and division ID 0.
     */
    public Customer() {
        super(0, "Dummy Name");
        address = "Dummy Address";
        postalCode = "99999";
        phoneNum = "999-999-9999";
        divisionID = 0;
    }

    /**
     * @return Address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address Address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return PostalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode PostalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return phoneNum
     */
    public String getPhoneNum() {
        return phoneNum;
    }

    /**
     * @param phoneNum phoneNum to set
     */
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    /**
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @param divisionID divisionID to set
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * @return divisionID
     */
    public String getDivision() {
        return JDBC.getDivisionName(divisionID);
    }

    /**
     * @return divisionID
     */
    public String getCountry() {
        return JDBC.getCountryName(divisionID);
    }
}

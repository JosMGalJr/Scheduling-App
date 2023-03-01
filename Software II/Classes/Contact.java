package Classes;

public class Contact extends Person {
    private String email;

    /**
     * Parametrized constructor.
     *
     * @param id    ID number
     * @param name  Name
     * @param email Email
     */
    public Contact(int id, String name, String email) {
        super(id, name);
        this.email = email;
    }

    /**
     * Copy constructor.
     *
     * @param contactToCopy Contact to be copied.
     */
    public Contact(Contact contactToCopy) {
        super(contactToCopy.getId(), contactToCopy.getName());
        this.email = contactToCopy.email;
    }

    /**
     * Dummy Constructor. Creates a Contact with ID of 0, name 'Dummy Name', and email 'dummy@email.com'.
     */
    public Contact() {
        super(0, "Dummy Name");
        email = "dummy@email.com";
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

}

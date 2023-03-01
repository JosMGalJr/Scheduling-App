package Classes;

public class User extends Person {
    private String password;

    /**
     * Parametrized constructor.
     *
     * @param id       ID number
     * @param name     Name
     * @param password Password
     */
    public User(int id, String name, String password) {
        super(id, name);
        this.password = password;
    }

    /**
     * Copy constructor.
     *
     * @param userToCopy the User to be copied.
     */
    public User(User userToCopy) {
        super(userToCopy.getId(), userToCopy.getName());
        this.password = userToCopy.password;
    }

    /**
     * Dummy Constructor. Creates a User with ID of 0, name 'Dummy Name', and password 'Dummy Password'.
     */
    public User() {
        super(0, "Dummy Name");
        password = "Dummy Password";
    }

    /**
     * @param password Password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return Password
     */
    public String getPassword() {
        return password;
    }
}

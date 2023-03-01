package Classes;


/** Abstract Class for Customer, User, and Contact. Contains ID and Name.
 * */
public abstract class Person {
    private int id;
    private String name;



    /**
     * Parametrized constructor.
     * @param id ID number
     * @param name Name
     * */
    public Person(int id, String name) {
        this.id = id;
        this.name = name;
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


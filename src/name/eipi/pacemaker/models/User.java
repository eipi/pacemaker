package name.eipi.pacemaker.models;

import java.util.Collection;
import java.util.ArrayList;

public class User extends BaseEntity {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Collection<Long> activities;


    /**
     * No  default constructor .
     */
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        activities = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addActivity(Long id) {
        activities.add(id);

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Long> getActivities() {
        return activities;
    }

}
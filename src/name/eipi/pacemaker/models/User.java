package name.eipi.pacemaker.models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

public class User extends BaseEntity {

    @Getter
    @Setter
    protected String firstName;
    @Getter
    @Setter
    protected String lastName;
    @Getter
    @Setter
    protected String email;
    @Getter
    @Setter
    protected String password;
    @Getter
    protected Collection<Long> activities;


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

    public void addActivity(Long id) {
        activities.add(id);
    }

}
package name.eipi.pacemaker.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.ArrayList;

public class User extends BaseEntity {

    @Getter @Setter
    private String firstName;
    @Getter @Setter
    private String lastName;
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String password;
    @Getter
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

    public void addActivity(Long id) {
        activities.add(id);
    }

}
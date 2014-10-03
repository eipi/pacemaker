package name.eipi.pacemaker.views;

import asg.cliche.Command;
import asg.cliche.Param;
import name.eipi.pacemaker.controllers.PacemakerAPI;
import name.eipi.pacemaker.models.User;
import name.eipi.pacemaker.util.Utilities;

import java.util.Collection;

/**
 * Created by dbdon_000 on 27/09/2014.
 */
public class PacemakerUI {

    private final PacemakerAPI paceApi;

    public PacemakerUI(PacemakerAPI api) {
        paceApi = api;
    }

    @Command(description = "Get all Users")
    public void getUsers() {
        Collection<User> users = paceApi.getUsers();
        System.out.println(users);
    }

    @Command(description = "Create a new User")
    public void createUser(
            @Param(name = "first name") String firstName,
            @Param(name = "last name") String lastName,
            @Param(name = "email") String email,
            @Param(name = "password") String password) {
        paceApi.createUser(firstName, lastName, email, password);
    }

    @Command(description = "Get User Details")
    public void getUser(
            @Param(name = "email") String email) {
        User user = paceApi.getUserByEmail(email);
        System.out.println(Utilities.toFancyString(user));
    }

    @Command(description = "Delete a User")
    public void deleteUser(
            @Param(name = "email") String email) {
        paceApi.deleteUser(email);
    }

    @Command(description = "Add an Activity")
    public void addActivity(
            @Param(name = "userId") Long userId,
            @Param(name = "type") String type,
            @Param(name = "location") String location,
            @Param(name = "distance") Double distance) {

        paceApi.addActivity(userId, type, location, distance);
    }

    @Command(description = "Get Activities for User")
    public void getActivities(@Param(name = "user id") Integer userId) {
        //TODO
    }

    @Command(description = "Get Locations for Activity")
    public void getLocations(@Param(name = "activity id") Integer activityId) {
        //TODO
    }

    @Command(description = "Add a Location")
    public void addLocation(
            @Param(name = "activityId") Long activityId,
            @Param(name = "latitude") Integer latitude,
            @Param(name = "longitude") Integer longitude) {

        paceApi.addLocation(activityId, latitude, longitude);
    }

    @Command(description = "Save all changes")
    public void save() {
        paceApi.save();
    }

    @Command(description = "Load data from disk")
    public void load() {
        paceApi.load();
    }

    @Command(description = "Reset all")
    public void reset() {
        paceApi.reset();
    }

    @Command(description = "Change file format")
    public void changeFileFormat() {
        paceApi.changeFileFormat();
    }

}

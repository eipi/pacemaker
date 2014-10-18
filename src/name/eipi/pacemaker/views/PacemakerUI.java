package name.eipi.pacemaker.views;

import asg.cliche.Command;
import asg.cliche.Param;
import name.eipi.pacemaker.controllers.PacemakerAPI;
import name.eipi.pacemaker.controllers.Response;
import name.eipi.pacemaker.util.StringUtils;

import java.io.PrintStream;

/**
 * Created by dbdon_000 on 27/09/2014.
 */
public class PacemakerUI {

    private final PacemakerAPI paceApi;

    private final PrintStream out;
    private final PrintStream err;

    public PacemakerUI(PacemakerAPI api) {
        paceApi = api;
        out = System.out;
        err = System.err;
    }

    @Command(description = "List all Users")
    public void listUsers() {
        out.println(StringUtils.toFancyString(paceApi.getUsers()));
    }

    @Command(description = "Create a new User")
    public void createUser(
            @Param(name = "first name") String firstName,
            @Param(name = "last name") String lastName,
            @Param(name = "email") String email,
            @Param(name = "password") String password) {
        Response response = paceApi.createUser(firstName, lastName, email, password);
        if (response.getSuccess()) {
            out.println("Created User\r\n" + StringUtils.toFancyString(response));
        } else {
            err.println(response.getMessage());
        }
    }

    @Command(description = "Get User Details")
    public void listUser(
            @Param(name = "email") String email) {
        out.println((StringUtils.toFancyString(paceApi.getUserByEmail(email))));
    }

    @Command(description = "Delete a User")
    public void deleteUser(
            @Param(name = "id") Long id) {
        Response response = paceApi.deleteUser(id);
        if (response.getSuccess()) {
            out.println("Deleted User\r\n" + StringUtils.toFancyString(response));
        } else {
            err.println(response.getMessage());
        }

    }

    @Command(description = "Add an Activity")
    public void addActivity(
            @Param(name = "user-id") Long userId,
            @Param(name = "type") String type,
            @Param(name = "location") String location,
            @Param(name = "distance") Double distance) {
        Response response = paceApi.addActivity(userId, type, location, distance);
        if (response.getSuccess()) {
            out.println("Added activity to user " + userId + "\r\n" + StringUtils.toFancyString(response));
        } else {
            err.println(response.getMessage());
        }
    }

    @Command(description = "List all Activities for User")
    public void listActivities(@Param(name = "user-id") Long userId) {
        Response response = paceApi.getActivities(userId);
        if (response.getSuccess()) {
            out.println(StringUtils.toFancyString(response));
        } else {
            err.println(response.getMessage());
        }
    }


    @Command(description = "List all Activities for User, sorted by specified field ascending")
    public void listActivities(@Param(name = "user-id") Long userId,
                               @Param(name = "sortBy: type, location, distance, date, duration") String sortBy) {
        Response response = paceApi.getActivities(userId, sortBy);
        if (response.getSuccess()) {
            out.println(StringUtils.toFancyString(response));
        } else {
            err.println(response.getMessage());
        }
    }


    @Command(description = "List all Locations for Activity")
    public void listLocations(@Param(name = "activity-id") Long activityId) {
        out.println(StringUtils.toFancyString(paceApi.getLocations(activityId)));
    }

    @Command(description = "Add a Location")
    public void addLocation(
            @Param(name = "activit-id") Long activityId,
            @Param(name = "latitude") Integer latitude,
            @Param(name = "longitude") Integer longitude) {
        Response response = paceApi.addLocation(activityId, latitude, longitude);
        if (response.getSuccess()) {
            out.println("Added location to activity " + activityId + "\r\n" + StringUtils.toFancyString(response));
        } else {
            err.println(response.getMessage());
        }
    }

    @Command(description = "Save all changes")
    public void store() {
        if (paceApi.save()) {
            out.println("Saved ok.");
        } else {
            err.println("Error saving changes. Please check configuration and try again.");
        }

    }

    @Command(description = "Load data from disk")
    public void load() {
        if (paceApi.load()) {
                out.println("Loaded ok");
            } else {
                err.println("Error loading data. Please check configuration and try again.");
            }

    }

    @Command(description = "Change file format")
    public void changeFileFormat(@Param(name = "file format: xml, json, sql") String format) {
        paceApi.changeFormat(format);
    }

}

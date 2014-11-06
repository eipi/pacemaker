package name.eipi.pacemaker.views;

import asg.cliche.Command;
import asg.cliche.Param;
import name.eipi.pacemaker.controllers.ApiResponse;
import name.eipi.pacemaker.controllers.PacemakerApi;
import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.User;
import name.eipi.pacemaker.util.StringUtils;

import java.io.PrintStream;

/**
 * Created by dbdon_000 on 27/09/2014.
 */
public class PacemakerUI {

    private final PacemakerApi paceApi;

    private PrintStream out;
    private PrintStream err;

    /**
     * Display deep entity relationships on read.
     */
    private static boolean extendedPrettyPrint = false;

    public PacemakerUI(PacemakerApi api) {
        paceApi = api;
        out = System.out;
        err = System.err;
    }

    @Command(description = "List all Users")
    public void listUsers() {
        out.println(StringUtils.toFancyString(paceApi.getUsers()));
    }


    @Command(description = "Get User by id", abbrev = "lius")
    public void listUser(@Param(name = "id") Long id) {
        User user = paceApi.getUser(id);
        out.println(StringUtils.toFancyString(user));
        if (extendedPrettyPrint && user != null) {
            listActivities(user.getId());
        }
    }

    @Command(description = "Create a new User")
    public void createUser(
            @Param(name = "first name") String firstName,
            @Param(name = "last name") String lastName,
            @Param(name = "email") String email,
            @Param(name = "password") String password) {
        ApiResponse response = paceApi.createUser(firstName, lastName, email, password);
        if (response.isSuccess()) {
            out.println("Created User\r\n" + StringUtils.toFancyString(response));
        } else {
            err.println(response.getMessage());
        }
    }

    @Command(description = "Get User by email")
    public void listUser(
            @Param(name = "email") String email) {
        User user = paceApi.getUserByEmail(email);
        out.println((StringUtils.toFancyString(user)));
        if (extendedPrettyPrint && user != null) {
            listActivities(user.getId());
        }
    }

    @Command(description = "Delete a User")
    public void deleteUser(
            @Param(name = "id") Long id) {
        ApiResponse response = paceApi.deleteUser(id);
        if (response.isSuccess()) {
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
            @Param(name = "distance") Double distance,
            @Param(name = "start-time") String startTime,
            @Param(name = "duration") String duration) {
        ApiResponse response = paceApi.addActivity(userId, type, location, distance, startTime, duration);
        if (response.isSuccess()) {
            out.println("Added activity to user " + userId + "\r\n" + StringUtils.toFancyString(response));
        } else {
            err.println(response.getMessage());
        }
    }

    @Command(description = "List all Activities for User")
    public void listActivities(@Param(name = "user-id") Long userId) {
        ApiResponse<Activity> response = paceApi.getActivities(userId);
        if (response.isSuccess()) {
            out.println(StringUtils.toFancyString(response));
            if (extendedPrettyPrint) {
                for (Activity a : response) {
                    listLocations(a.getId());
                }
            }
        } else {
            err.println(response.getMessage());
        }
    }


    @Command(description = "List all Activities for User, sorted by specified field ascending")
    public void listActivities(@Param(name = "user-id") Long userId,
                               @Param(name = "sortBy: type, location, distance, date, duration") String sortBy) {
        ApiResponse<Activity> response = paceApi.getActivities(userId, sortBy);
        if (response.isSuccess()) {
            out.println(StringUtils.toFancyString(response));
            if (extendedPrettyPrint) {
                for (Activity a : response) {
                    listLocations(a.getId());
                }
            }
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
            @Param(name = "activity-id") Long activityId,
            @Param(name = "latitude") Double latitude,
            @Param(name = "longitude") Double longitude) {
        ApiResponse response = paceApi.addLocation(activityId, latitude, longitude);
        if (response.isSuccess()) {
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
    public void changeFileFormat(@Param(name = "file format: xml, json") String format) {
        ApiResponse response = paceApi.changeFormat(format);
        if (response.isSuccess()) {
            out.println("DataBaser converted to " + format);
        } else {
            err.println(response.getMessage());
        }
    }

    // UI Specific option.
    @Command(description = "Extended Output On/Off")
    public void extendedOutput(
            @Param(name = "Y/N") String value) {
        switch (value.toLowerCase().trim()) {
            case "y":
                extendedPrettyPrint = true;
                break;
            case "n":
                extendedPrettyPrint = false;
                break;
            default:
                out.println("Invalid value \"" + value + "\".\r\n" +
                        "Valid choices are \"y\" and \"n\".");
        }

    }
}


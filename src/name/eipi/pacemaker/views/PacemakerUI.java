package name.eipi.pacemaker.views;

import asg.cliche.Command;
import asg.cliche.Param;
import lombok.Getter;
import lombok.Setter;
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
     * Mixed will display data and regular messages in white, but error messages in red.
     * White will display all information in white, and Red all information in red.
     * TODO: Investigate alternate methods of specifying output colour.
     */
    public enum ColorScheme {
        mixed,
        white,
        red

    }

    private static ColorScheme colorScheme = ColorScheme.mixed;

    /** Display deep entity relationships on read. */
    private static boolean extendedPrettyPrint = false;

    public PacemakerUI(PacemakerApi api) {
        paceApi = api;
        setColorScheme();
    }

    private void setColorScheme() {
        switch (colorScheme) {
            case mixed : this.out = System.out; this.err = System.err; break;
            case white : this.out = this.err = System.out; break;
            case red : this.out = this.err = System.err; break;
        }
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
    @Command(description = "Change UI Settings")
    public void updateInterface(
        @Param(name = "option: extended | colour") String option,
        @Param(name = "value: extended(Y|N), colour(mixed|red|white)")String value) {
        if (option.startsWith("extended")) {
            switch (value.toLowerCase().trim()) {
                case "on" :
                    extendedPrettyPrint=true;
                    break;
                case "off" :
                    extendedPrettyPrint=false;
                    break;
                default:
                    out.println("Invalid value \"" + value + "\".\r\n" +
                            "Valid choices are \"on\" and \"off\".");
            }

        } else if (option.startsWith("colour")) {
            switch (value.toLowerCase().trim()) {
                case "mixed" : colorScheme = ColorScheme.mixed; break;
                case "red" :  colorScheme = ColorScheme.red; break;
                case "white" : colorScheme = ColorScheme.white; break;
                default:
                    out.println("Invalid value \"" + value + "\".\r\n" +
                        "Valid choices are \"mixed\", \"red\", or \"white\".");
            }
            setColorScheme();
        }
    }

}

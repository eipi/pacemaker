package name.eipi.pacemaker.controllers;

import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.Location;
import name.eipi.pacemaker.models.User;

import java.util.Collection;

/**
 * Created by dbdon_000 on 21/10/2014.
 */
public interface PacemakerApi {
    // User operations
    ApiResponse<User> createUser(String firstName, String lastName, String email, String password);

    ApiResponse<User> createUser(User user);

    User getUserByEmail(String email);

    User getUser(Long id);

    Collection<User> getUsers();

    ApiResponse deleteUser(Long id);

    ApiResponse<Activity> addActivity(Long userId, Activity activity);

    ApiResponse<Activity> addActivity(Long userId, String type, String location, Double distance, String startTime, String duration);

    ApiResponse getActivities(Long userId, String sortBy);

    ApiResponse<Activity> getActivities(Long userId);

    ApiResponse<Location> addLocation(Long activityId, Double latitude, Double longitude);

    ApiResponse<Location> addLocation(Long activityId, Location location);

    ApiResponse<Location> getLocations(Long actvId);

    // DB Operations
    boolean save();

    boolean load();

    ApiResponse changeFormat(String format);
}

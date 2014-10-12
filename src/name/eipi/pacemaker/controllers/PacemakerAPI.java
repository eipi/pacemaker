package name.eipi.pacemaker.controllers;

import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.Location;
import name.eipi.pacemaker.models.User;
import name.eipi.pacemaker.persistence.DataLodge;
import name.eipi.services.logger.Logger;
import name.eipi.services.logger.LoggerFactory;

import java.util.*;

public class PacemakerAPI {

    /** Logger. */
    private static final Logger LOG = LoggerFactory.getInstance(PacemakerAPI.class);

    /** Manages read and write operations for objects extending BaseEntity. */
    private final DataLodge db;

    /** A convenience index of users by email. Allows quick lookup and validation. */
    private Map<String, User> emailIndex = new HashMap<>();

    public PacemakerAPI() {
        db = new DataLodge();
    }

    public PacemakerAPI(String fileName) {
        db =  new DataLodge(fileName);
    }

    // User operations
    public User createUser(String firstName, String lastName, String email, String password) {
        if (emailIndex.containsKey(email)) {
            System.err.println("User already exists : " + email);
        }
        User user = db.edit(new User(firstName, lastName, email, password));
        emailIndex.put(email, user);
        return user;

    }
    public User getUserByEmail(String email) {
        if (emailIndex.containsKey(email)) {
            return emailIndex.get(email);
        } else {
            System.err.println("User not found : " + email);
            return null;
        }
    }
    private User getUser(Long id) {
        User user = db.read(User.class, id);
        if (user != null) {
            return user;
        } else {
            System.err.println("User not found : " + id);
            return null;
        }
    }
    public Collection<User> getUsers() {
        return db.getAll(User.class);
    }
    public void deleteUser(String email) {
        if (emailIndex.containsKey(email)) {
            db.delete(emailIndex.remove(email));
        } else {
            System.err.println("User not found : " + email);
        }
    }


    public Set getActivities(Long userId) {
        Set<Activity> activities = new HashSet<Activity>();
        User u = db.read(User.class, userId);
        Collection<Long> c = u.getActivities();
        for (Long actvId : c) {
            activities.add(db.read(Activity.class, actvId));
        }
        return activities;
    }

    public Set getLocations(Long actvId) {
        Set<Location> locations = new HashSet<Location>();
        Activity a = db.read(Activity.class, actvId);
        Collection<Long> c = a.getRoutes();
        for (Long locId : c) {
            locations.add(db.read(Location.class, locId));
        }
        return locations;
    }

    public Activity addActivity(Long userId, String type, String location, Double distance) {
        Activity activity = db.edit(new Activity(type, location, distance));
        User user = getUser(userId);
        user.addActivity(activity.getId());
        return activity;
    }

    public Location addLocation(Long activityId, Integer latitude, Integer longitude) {
        Activity activity = db.read(Activity.class, activityId);
        if (activity != null) {
            Location location = db.edit(new Location(latitude, longitude));
            activity.addRoute(location.getId());
            return location;
        }
        LOG.error("Unknown activity " + activityId);
        return null;

    }

    // DB Operations
    public void save() {
        db.save();
    }

    public void load() {
        if (db.load()) {
            System.err.println("Loaded ok");
        } else {
            System.err.println("No data found");
        }
    }

    public void reset() {
        db.reset();
    }

    public void toggleFormat() {
        db.toggleFormat();
    }

    public void cleanUp() {db.cleanUp();}

}
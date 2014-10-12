package name.eipi.pacemaker.controllers;

import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.Location;
import name.eipi.pacemaker.models.User;
import name.eipi.pacemaker.persistence.DataLodge;
import name.eipi.services.logger.Logger;
import name.eipi.services.logger.LoggerFactory;

import java.util.*;

public class PacemakerAPI {

    private static final Logger LOG = LoggerFactory.getInstance(PacemakerAPI.class);

    private static final DataLodge db = new DataLodge();

    private Map<String, User> emailIndex = new HashMap<>();

    public Collection<User> getUsers() {
        return db.getAll(User.class);
    }

    public void deleteUsers() {
        db.reset();
        emailIndex.clear();
    }

    public User createUser(String firstName, String lastName, String email, String password) {
        if (emailIndex.containsKey(email)) {
            throw new IllegalArgumentException("User already exists : " + email);
        }
        User user = new User(firstName, lastName, email, password);
        return emailIndex.put(email, db.edit(user));
    }

    public void deleteUser(String email) {
        if (emailIndex.containsKey(email)) {
            db.delete(emailIndex.remove(email));
        } else {
            throw new IllegalArgumentException("User not found : " + email);
        }
    }

    public User getUserByEmail(String email) {
        if (emailIndex.containsKey(email)) {
            return emailIndex.get(email);
        } else {
            throw new IllegalArgumentException("User not found : " + email);
        }
    }

    private User getUser(Long id) {
        User user = db.read(User.class, id);
        if (user != null) {
            return user;
        } else {
            throw new IllegalArgumentException("User not found : " + id);
        }
    }

    public Set getActivities(Long userId) {
        SortedSet<Activity> activities = new TreeSet<Activity>();
        User u = db.read(User.class, userId);
        Collection<Long> c = u.getActivities();
        for (Long actvId : c) {
            activities.add(db.read(Activity.class, actvId));
        }
        return activities;
    }

    public Set getLocations(Long actvId) {
        SortedSet<Location> locations = new TreeSet<Location>();
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

    public void save() {
        db.save();
    }

    public void load() {
        if (db.load()) {
            System.out.println("Loaded ok");
        }
    }

    public void reset() {
        db.reset();
    }

    public void toggleFormat() {
        db.toggleFormat();
    }

}
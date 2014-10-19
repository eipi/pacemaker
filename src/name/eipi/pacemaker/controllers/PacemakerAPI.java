package name.eipi.pacemaker.controllers;

import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.BaseEntity;
import name.eipi.pacemaker.models.Location;
import name.eipi.pacemaker.models.User;
import name.eipi.pacemaker.persistence.DataLodge;
import name.eipi.pacemaker.util.DateTimeUtils;
import name.eipi.pacemaker.util.SortingUtils;
import name.eipi.services.logger.Logger;
import name.eipi.services.logger.LoggerFactory;

import java.util.*;

public class PacemakerAPI {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getInstance(PacemakerAPI.class);

    /**
     * Manages read and write operations for objects extending BaseEntity.
     */
    private final DataLodge db;

    /**
     * A convenience index of users by email. Allows quick lookup and validation.
     * Needs to be build on load/reload, and updated when users are added/deleted.
     */
    private Map<String, User> emailIndex = new HashMap<>();

    public PacemakerAPI() {
        db = new DataLodge();
        rebuildUserIndex();
    }

    public PacemakerAPI(String fileName) {
        db = new DataLodge(fileName);
        rebuildUserIndex();
    }

    private void rebuildUserIndex() {
        emailIndex.clear();
        Collection<User> users = getUsers();
        for (User user : users) {
            emailIndex.put(user.getEmail(), user);
        }
    }

    // User operations
    public APIResponse<User> createUser(String firstName, String lastName, String email, String password) {
        User user = new User(firstName, lastName, email, password);
        return createUser(user);
    }

    /**
     * Convenience method for above
     */
    public APIResponse<User> createUser(User user) {
        APIResponse response = new APIResponse();
        if (emailIndex.containsKey(user.getEmail())) {
            response.setSuccess(Boolean.FALSE);
            response.setMessage("This email is already registered.");
            LOG.error("User already exists : " + user.getEmail());
        } else {
            user = db.edit(user);
            emailIndex.put(user.getEmail(), user);
            response.setSuccess(Boolean.TRUE);
            response.add(user);
        }
        return response;
    }

    public User getUserByEmail(String email) {
        if (emailIndex.containsKey(email)) {
            return emailIndex.get(email);
        } else {
            LOG.error("User not found : " + email);
            return null;
        }
    }

    public User getUser(Long id) {
        User user = db.read(User.class, id);
        if (user != null) {
            return user;
        } else {
            LOG.error("User not found : " + id);
            return null;
        }
    }

    public Collection<User> getUsers() {
        return db.getAll(User.class);
    }

    /**
     * For testing purposes only.
     *
     * @param clazz
     * @return
     */
    protected Collection<? extends BaseEntity> getAll(Class clazz) {
        return db.getAll(clazz);
    }

    public APIResponse deleteUser(Long id) {
        APIResponse response = new APIResponse();
        User user = getUser(id);
        if (user != null) {
            emailIndex.remove(user.getEmail());
            // First delete all children.
            for (Long actvId : user.getActivities()) {
                Activity activity = db.read(Activity.class, actvId);
                if (activity != null) {
                    for (Long locId : activity.getRoutes()) {
                        Location location = db.read(Location.class, locId);
                        db.delete(location);
                    }
                    db.delete(activity);
                }
            }

            db.delete(user);

            response.setSuccess(Boolean.TRUE);
            response.add(user);

        } else {
            response.setSuccess(Boolean.FALSE);
            response.setMessage("User not found : " + id);

        }
        return response;
    }

    public APIResponse<Activity> addActivity(Long userId, Activity activity) {
        APIResponse response = new APIResponse();
        User user = getUser(userId);
        if (user != null) {
            activity = db.edit(activity);
            user.addActivity(activity.getId());
            response.setSuccess(Boolean.TRUE);
            response.add(activity);
        } else {
            response.setSuccess(Boolean.FALSE);
            response.setMessage("User not found : " + userId);
        }
        return response;
    }

    public APIResponse<Activity> addActivity(Long userId, String type, String location, Double distance, String startTime, String duration) {
        Activity activity = new Activity(type, location, distance);
        activity.setStartTime(DateTimeUtils.parseDateTime(startTime));
        activity.setDuration(DateTimeUtils.parseDuration(duration));
        return addActivity(userId, activity);
    }

    public APIResponse getActivities(Long userId, String sortBy) {
        APIResponse response = new APIResponse();
        APIResponse unsorted = getActivities(userId);
        Comparator comparator = SortingUtils.getComparator(Activity.class, sortBy.toLowerCase());
        if (comparator == null || !unsorted.isSuccess()) {
            return unsorted;
        } else {
            response.addAll(unsorted);
            Collections.sort(response, comparator);
            response.setSuccess(Boolean.TRUE);
        }
        return response;
    }

    public APIResponse<Activity> getActivities(Long userId) {
        APIResponse response = new APIResponse();
        Collection<Activity> activities = new ArrayList<>();
        User u = db.read(User.class, userId);
        if (u != null) {
            Collection<Long> c = u.getActivities();
            for (Long actvId : c) {
                activities.add(db.read(Activity.class, actvId));
            }
            response.setSuccess(Boolean.TRUE);
            response.addAll(activities);
        } else {
            response.setSuccess(Boolean.FALSE);
            response.setMessage("User not found : " + userId);
        }
        return response;
    }

    public APIResponse<Location> addLocation(Long activityId, Integer latitude, Integer longitude) {
        Location location = new Location(latitude, longitude);
        return addLocation(activityId, location);
    }

    public APIResponse<Location> addLocation(Long activityId, Location location) {
        APIResponse response = new APIResponse();
        Activity activity = db.read(Activity.class, activityId);
        if (activity != null) {
            location = db.edit(location);
            activity.addRoute(location.getId());
            response.setSuccess(Boolean.TRUE);
            response.add(location);
        } else {
            response.setSuccess(Boolean.FALSE);
            response.setMessage("Activity not found : " + activityId);
        }
        return response;
    }

    public APIResponse<Location> getLocations(Long actvId) {
        APIResponse response = new APIResponse();
        Collection<Location> locations = new ArrayList<>();
        Activity a = db.read(Activity.class, actvId);
        if (a != null) {
            Collection<Long> c = a.getRoutes();
            for (Long locId : c) {
                locations.add(db.read(Location.class, locId));
            }
            response.setSuccess(Boolean.TRUE);
            response.addAll(locations);
        } else {
            response.setSuccess(Boolean.FALSE);
            response.setMessage("Activity not found : " + actvId);
        }
        return response;
    }

    // DB Operations
    public boolean save() {
        return db.save();
    }

    public boolean load() {
        if (db.load()) {
            rebuildUserIndex();
            return true;
        }
        return false;

    }

    public void changeFormat(String format) {
        db.changeFormat(format);
    }

}
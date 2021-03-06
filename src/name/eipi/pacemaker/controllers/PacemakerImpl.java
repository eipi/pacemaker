package name.eipi.pacemaker.controllers;

import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.BaseEntity;
import name.eipi.pacemaker.models.Location;
import name.eipi.pacemaker.models.User;
import name.eipi.pacemaker.persistence.DataLodge;
import name.eipi.pacemaker.util.DateTimeUtils;
import name.eipi.pacemaker.util.SortingUtils;

import java.util.*;

public class PacemakerImpl implements PacemakerApi {

    /**
     * Manages read and write operations for objects extending BaseEntity.
     */
    private final DataLodge db;

    /**
     * A convenience index of users by email. Allows quick lookup and validation.
     * Needs to be build on load/reload, and updated when users are added/deleted.
     */
    private Map<String, User> emailIndex = new HashMap<>();

    public PacemakerImpl() {
        db = new DataLodge();
        rebuildUserIndex();
    }

    public PacemakerImpl(String fileName) {
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
    @Override
    public ApiResponse<User> createUser(String firstName, String lastName, String email, String password) {
        User user = new User(firstName, lastName, email, password);
        return createUser(user);
    }

    /**
     * Convenience method for above
     */
    @Override
    public ApiResponse<User> createUser(User user) {
        ApiResponse response = new ApiResponse();
        if (emailIndex.containsKey(user.getEmail())) {
            response.setSuccess(Boolean.FALSE);
            response.setMessage("This email is already registered.");
        } else {
            user = db.edit(user);
            emailIndex.put(user.getEmail(), user);
            response.setSuccess(Boolean.TRUE);
            response.add(user);
        }
        return response;
    }

    @Override
    public User getUserByEmail(String email) {
        if (emailIndex.containsKey(email)) {
            return emailIndex.get(email);
        } else {
            return null;
        }
    }

    @Override
    public User getUser(Long id) {
        User user = db.read(User.class, id);
        if (user != null) {
            return user;
        } else {
            return null;
        }
    }

    @Override
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

    @Override
    public ApiResponse deleteUser(Long id) {
        ApiResponse response = new ApiResponse();
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

    @Override
    public ApiResponse<Activity> addActivity(Long userId, Activity activity) {
        ApiResponse response = new ApiResponse();
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

    @Override
    public ApiResponse<Activity> addActivity(Long userId, String type, String location, Double distance, String startTime, String duration) {
        ApiResponse response = new ApiResponse();
        Activity activity = new Activity(type, location, distance);
        User user = getUser(userId);
        if (user != null) {
            // TODO Next line throws an exception when passed bad arg, handle nicer?
            activity.setStartTime(DateTimeUtils.parseDateTime(startTime));
            activity.setDuration(DateTimeUtils.parseDuration(duration));
            response = addActivity(userId, activity);
        } else {
            response.setSuccess(Boolean.FALSE);
            response.setMessage("User not found : " + userId);
        }
        return response;
    }

    @Override
    public ApiResponse getActivities(Long userId, String sortBy) {
        ApiResponse response = new ApiResponse();
        ApiResponse unsorted = getActivities(userId);
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

    @Override
    public ApiResponse<Activity> getActivities(Long userId) {
        ApiResponse response = new ApiResponse();
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

    @Override
    public ApiResponse<Location> addLocation(Long activityId, Double latitude, Double longitude) {
        Location location = new Location(latitude, longitude);
        return addLocation(activityId, location);
    }

    @Override
    public ApiResponse<Location> addLocation(Long activityId, Location location) {
        ApiResponse response = new ApiResponse();
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

    @Override
    public ApiResponse<Location> getLocations(Long actvId) {
        ApiResponse response = new ApiResponse();
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
    @Override
    public boolean save() {
        return db.save();
    }

    @Override
    public boolean load() {
        if (db.load()) {
            rebuildUserIndex();
            return true;
        }
        return false;

    }

    @Override
    public ApiResponse changeFormat(String format) {
        ApiResponse response = new ApiResponse();
        response.setSuccess(db.changeFormat(format));
        if (!response.isSuccess()) {
            response.setMessage("Unrecognized format \"" + format + "\"");
        }
        return response;

    }

}
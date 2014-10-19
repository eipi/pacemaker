package name.eipi.pacemaker.controllers;

import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.Location;
import name.eipi.pacemaker.models.User;
import name.eipi.pacemaker.persistence.DataLodge;
import name.eipi.pacemaker.util.SortingUtils;
import name.eipi.services.logger.Logger;
import name.eipi.services.logger.LoggerFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

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
        rebuildUserIndex();
    }

    public PacemakerAPI(String fileName) {
        db =  new DataLodge(fileName);
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
    public Response<User> createUser(String firstName, String lastName, String email, String password) {
        Response response = new Response();
        if (emailIndex.containsKey(email)) {
            response.setSuccess(Boolean.FALSE);
            response.setMessage("This email is already registered.");
            LOG.error("User already exists : " + email);
        } else {
            User user = db.edit(new User(firstName, lastName, email, password));
            emailIndex.put(email, user);
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

    private User getUser(Long id) {
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

    public Response deleteUser(Long id) {
        Response response = new Response();
        User user = getUser(id);
        if (user != null) {
            emailIndex.remove(user.getEmail());
            db.delete(user);

            response.setSuccess(Boolean.TRUE);
            response.add(user);

        } else {
            response.setSuccess(Boolean.FALSE);
            response.setMessage("User not found : " + id);

        }
        return response;
    }

    public Response<Activity> addActivity(Long userId, String type, String location, Double distance, String startTime, String duration) {
        Response response = new Response();
        User user = getUser(userId);
        if (user != null) {
            Activity activity = new Activity(type, location, distance);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
            activity.setStartTime(fmt.parseDateTime(startTime));
            PeriodFormatter hoursMinutesSeconds= new PeriodFormatterBuilder()
                    .appendHours()
                    .appendSeparator(":")
                    .appendMinutes()
                    .appendSeparator(":")
                    .appendSeconds()
                    .toFormatter();
            activity.setDuration(hoursMinutesSeconds.parsePeriod(duration).toStandardDuration());
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

    public Response getActivities(Long userId, String sortBy) {
        Response response = new Response();
        Response unsorted = getActivities(userId);
        Comparator comparator = SortingUtils.getComparator(Activity.class, sortBy.toLowerCase());
        if (comparator == null || !unsorted.getSuccess()) {
            return unsorted;
        } else  {
           response.addAll(unsorted);
           Collections.sort(response, comparator);
           response.setSuccess(Boolean.TRUE);
        }
        return response;
    }

    public Response<Activity> getActivities(Long userId) {
        Response response = new Response();
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

    public Response<Location> addLocation(Long activityId, Integer latitude, Integer longitude) {
        Response response = new Response();
        Activity activity = db.read(Activity.class, activityId);
        if (activity != null) {
            Location location = db.edit(new Location(latitude, longitude));
            activity.addRoute(location.getId());
            response.setSuccess(Boolean.TRUE);
            response.add(location);
        } else {
            response.setSuccess(Boolean.FALSE);
            response.setMessage("Activity not found : " + activityId);
        }
        return response;
    }

    public Response<Location> getLocations(Long actvId) {
        Response response = new Response();
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
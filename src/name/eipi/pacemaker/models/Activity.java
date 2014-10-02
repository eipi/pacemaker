package name.eipi.pacemaker.models;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class Activity extends BaseEntity {

    /**
     * routes
     */
    private List<Long> routes = new ArrayList<>();

    private Long id;
    private String type;
    private String location;
    private double distance;

    /**
     * No default constructor, just this.
     *
     * @param type
     * @param location
     * @param distance
     */
    public Activity(String type, String location, double distance) {
        this.type = type;
        this.location = location;
        this.distance = distance;
    }

    public void addRoute(Long id) {
        routes.add(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Activity setType(String type) {
        this.type = type;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Activity setLocation(String location) {
        this.location = location;
        return this;
    }

    public double getDistance() {
        return distance;
    }

    public Activity setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public Collection<Long> getRoutes() {
        return routes;
    }

}

package name.eipi.pacemaker.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class Activity extends BaseEntity {

    @Getter @Setter
    private String type;
    @Getter @Setter
    private String location;
    @Getter @Setter
    private double distance;
    @Getter
    private Collection<Long> routes;

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
        this.routes = new ArrayList<>();
    }

    public void addRoute(Long id) {
        routes.add(id);
    }

}

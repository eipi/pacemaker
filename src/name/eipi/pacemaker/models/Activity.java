package name.eipi.pacemaker.models;

import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;
import java.util.Collection;

public class Activity extends BaseEntity {

    @Getter
    @Setter
    protected String type;
    @Getter
    @Setter
    protected String location;
    @Getter
    @Setter
    protected Double distance;
    @Getter
    protected Collection<Long> routes;
    @Getter
    @Setter
    protected DateTime startTime;
    @Getter
    @Setter
    protected Duration duration;

    /**
     * No default constructor, just this.
     *
     * @param type
     * @param location
     * @param distance
     */
    public Activity(String type, String location, Double distance) {
        this.type = type;
        this.location = location;
        this.distance = distance;
        this.routes = new ArrayList<>();
    }

    /**
     * No default constructor, just this.
     *
     * @param type
     * @param location
     * @param distance
     */
    public Activity(String type, String location, Double distance, DateTime startTime, Duration duration) {
        this(type, location, distance);
        this.startTime = startTime;
        this.duration = duration;

    }

    public void addRoute(Long id) {
        routes.add(id);
    }

}

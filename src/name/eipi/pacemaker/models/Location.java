package name.eipi.pacemaker.models;

import lombok.Getter;
import lombok.Setter;

public class Location extends BaseEntity {

    @Getter
    @Setter
    protected Double latitude;
    @Getter
    @Setter
    protected Double longitude;

    /**
     * No default, just this.
     */
    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}


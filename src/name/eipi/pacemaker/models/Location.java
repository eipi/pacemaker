package name.eipi.pacemaker.models;

import lombok.Getter;
import lombok.Setter;

public class Location extends BaseEntity {

    @Getter
    @Setter
    protected int latitude;
    @Getter
    @Setter
    protected int longitude;

    /**
     * No default, just this.
     */
    public Location(int latitude, int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}


package name.eipi.pacemaker.models;

import lombok.Getter;
import lombok.Setter;

public class Location extends BaseEntity {

    @Getter @Setter
    private int latitude;
    @Getter @Setter
    private int longitude;

    /**
     * No default, just this.
     */
    public Location(int latitude, int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}


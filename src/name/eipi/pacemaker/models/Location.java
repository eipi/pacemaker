package name.eipi.pacemaker.models;

public class Location extends BaseEntity {

    private int latitude;
    private int longitude;

    /**
     * No default, just this.
     */
    public Location(int latitude, int longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

}


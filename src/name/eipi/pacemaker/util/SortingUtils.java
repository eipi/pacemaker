package name.eipi.pacemaker.util;

import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.BaseEntity;

import java.util.Comparator;

/**
 * Created by naysayer on 18/10/2014.
 */
public class SortingUtils {

    public static <T extends BaseEntity> Comparator getComparator(Class<T> t, String field) {
        if (t.equals(Activity.class)) {
            return new ActivityComparator(field);
        }
        return null;
    }


}

class ActivityComparator implements Comparator<Activity> {

    private String sortBy;

    ActivityComparator(final String sortBy) {
        this.sortBy = sortBy.toLowerCase();
    }

    @Override
    public int compare(Activity o1, Activity o2) {
        switch (sortBy) {
            case "type":
                return o1.getType().compareTo(o2.getType());
            case "location":
                return o1.getLocation().compareTo(o2.getLocation());
            case "distance":
                return ((Double) o1.getDistance()).compareTo(o2.getDistance());
            case "starttime":
                return o1.getStartTime().compareTo(o2.getStartTime());
            case "duration":
                return o1.getDuration().compareTo(o2.getDuration());
            default:
                return o1.getId().compareTo(o2.getId());
        }
    }
}


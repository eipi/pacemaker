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
            switch(field) {
                case "type" : return new TypeComparator();
                case "location" : return new LocationComparator();
                case "distance" : return new DistanceComparator();
            }
        }
        return null;
    }


}

class TypeComparator implements Comparator<Activity> {

    @Override
    public int compare(Activity o1, Activity o2) {
        return o1.getType().compareTo(o2.getType());
    }
}

class LocationComparator implements Comparator<Activity> {

    @Override
    public int compare(Activity o1, Activity o2) {
        return o1.getLocation().compareTo(o2.getLocation());
    }
}

class DistanceComparator implements Comparator<Activity> {

    @Override
    public int compare(Activity o1, Activity o2) {
        return ((Double) o1.getDistance()).compareTo(o2.getDistance());
    }
}

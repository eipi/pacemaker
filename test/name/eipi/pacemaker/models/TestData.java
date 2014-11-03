package name.eipi.pacemaker.models;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Date;

/**
 * Created by dbdon_000 on 19/10/2014.
 */
public class TestData {

    static int userCount = 0;
    static int activityCount;

    public static User createUser() {
        userCount++;
        return new User("marco" + userCount, "polo" + userCount, "a@b." + userCount, "pw" + userCount);
    }


    public static User createUser(String email) {
        User user = createUser();
        user.setEmail(email);
        return user;
    }

    public static Activity createActivity() {
        return new Activity("activity" + activityCount,
                "location" + activityCount,
                Double.valueOf(activityCount),
                new DateTime(),
                new Duration(System.currentTimeMillis()));
    }

    public static Location createLocation() {
        return new Location(Double.valueOf(new DateTime().hashCode()), Double.valueOf(System.currentTimeMillis()));
    }

}

package name.eipi.pacemaker.models;

/**
 * Created by dbdon_000 on 19/10/2014.
 */
public class TestData {

    static int userCount = 0;

    public static User createUser() {
        userCount++;
        return new User("marco"+userCount, "polo" + userCount, "a@b." + userCount, "pw" + userCount);
    }



}

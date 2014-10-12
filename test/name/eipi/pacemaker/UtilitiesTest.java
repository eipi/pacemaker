package name.eipi.pacemaker;

import name.eipi.pacemaker.persistence.DataLodge;
import name.eipi.pacemaker.models.User;
import name.eipi.pacemaker.util.Utilities;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by dbdon_000 on 28/09/2014.
 */
public class UtilitiesTest {

    @Test
    public void testFancyToString() {
        DataLodge db = new DataLodge("utilities.test");
        User user = db.edit(new User("Damien", "Donovan", "123@abc.ie", "Something"));
        System.out.println(Utilities.toFancyString(user));
        user = null;
        System.out.println(Utilities.toFancyString(user));


    }


    @Test
    public void testFancyToStringCollections() {
        DataLodge db = new DataLodge("utilities.test");
        User u1 = db.edit(new User("Damien", "Donovan", "123@abc.ie", "Something"));
        User u2 = db.edit(new User("Bones", "Malone", "bones@snoop.com", "Something"));
        Collection<User> c = new ArrayList<>();
        c.add(u1);
        c.add(u2);
        System.out.println(Utilities.toFancyString(c));


    }

}

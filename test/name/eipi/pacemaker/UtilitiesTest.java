package name.eipi.pacemaker;

import name.eipi.pacemaker.controllers.DataLodge;
import name.eipi.pacemaker.models.User;
import name.eipi.pacemaker.utils.Utilities;
import org.junit.Test;

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

}

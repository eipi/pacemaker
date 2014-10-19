package name.eipi.pacemaker.persistence;

import name.eipi.pacemaker.BaseTestPacemaker;
import name.eipi.pacemaker.controllers.PacemakerAPI;
import name.eipi.pacemaker.models.TestData;
import name.eipi.pacemaker.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by naysayer on 19/10/2014.
 */
public class DatabaserXStreamImplTest extends BaseTestPacemaker {

    PacemakerAPI api;
    User user = TestData.createUser();

    @Before
    public void setUp() {
        api = new PacemakerAPI("XStreamTest");
        api.changeFormat("json");

    }

    @After
    public void tearDown() {
        user = null;
    }

    @Test
    public void testDataBaser() {
        user = api.createUser(user).value();
        api.save();
        api = null;
        api = new PacemakerAPI("XStreamTest");
        api.changeFormat("json");
        api.load();
        assertTrue(api.getUsers().contains(user));
        api.changeFormat("xml");
        api.save();
        api = null;
        api = new PacemakerAPI("XStreamTest");
        api.changeFormat("xml");
        api.load();
        assertTrue(api.getUsers().contains(user));
        api.deleteUser(user.getId());
        api.changeFormat("json");
        api.save();
        api = null;
        api = new PacemakerAPI("XStreamTest");
        api.changeFormat("json");
        api.load();
        assertFalse(api.getUsers().contains(user));


    }


}

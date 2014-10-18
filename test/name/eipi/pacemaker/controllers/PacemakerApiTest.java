package name.eipi.pacemaker.controllers;

import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.Location;
import name.eipi.pacemaker.models.User;
import name.eipi.pacemaker.persistence.DataBaserXStreamImpl;
import org.junit.*;
import org.junit.Test;


import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by naysayer on 12/10/2014.
 */
public class PacemakerApiTest {

    PacemakerAPI api;
    User me;
    User you;

    private static final String email = "dbdonovan@gmail.com";

    @BeforeClass
    public static void testInit() {
        DataBaserXStreamImpl.DELETE_ON_EXIT = Boolean.TRUE;
    }

    @Before
    public void setUp() {
        api = new PacemakerAPI("PacemakerApiTest");

        me = api.createUser("Damien", "Donovan", email, "elephantastic").value();
        you = api.createUser("The Great", "Anon", "us@them.pi", "anonymous").value();
    }

    @After
    public void tearDown() {
        me = you = null;
    }

    @Test
    public void testUserCreate() {
        assertNotNull(me.getId());
        assertNotNull(you.getId());
        assertFalse(api.getUsers().isEmpty());
        assertTrue(api.getUsers().contains(me));
        assertTrue(api.getUsers().contains(you));
        assertTrue(me.equals(api.getUserByEmail(email)));
        assertFalse(you.equals(api.getUserByEmail(email)));
        Activity activity = api.addActivity(me.getId(), "Run", "Beach", 10.0).value();
        assertNotNull(activity.getId());
        assertTrue(api.getActivities(me.getId()).contains(activity));
        Location location = api.addLocation(activity.getId(), 1, 1).value();
        assertTrue(api.getLocations(activity.getId()).contains(location));

    }

}

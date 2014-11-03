package name.eipi.pacemaker.controllers;

import name.eipi.pacemaker.BaseTestPacemaker;
import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.Location;
import name.eipi.pacemaker.models.TestData;
import name.eipi.pacemaker.models.User;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Created by naysayer on 19/10/2014.
 */
public class PacemakerApiTestLocationActions extends BaseTestPacemaker {

    PacemakerImpl api;

    private User user = TestData.createUser();
    private Activity activity = TestData.createActivity();
    private Location location = TestData.createLocation();
    private ApiResponse<Location> apiResponse = null;

    @Before
    public void setUp() {
        api = new PacemakerImpl("PacemakerApiTest");
        user = api.createUser(user).value();
        activity = api.addActivity(user.getId(), activity).value();
        apiResponse = api.addLocation(activity.getId(), location);
    }


    @Test
    public void testCreate() {

        assertTrue(apiResponse.isSuccess());
        location = apiResponse.value();
        assertNotNull(location.getId());

    }

    @Test
    public void testRead() {
        assertTrue(api.getActivities(activity.getId()).value().getRoutes().contains(location.getId()));
    }

    @Test
    public void testCreateFail() {
        User newUser = api.createUser(TestData.createUser()).value();
        api.deleteUser(newUser.getId());
        ApiResponse<Location> create = api.addLocation(newUser.getId(), location);
        assertFalse(create.isSuccess());
    }

    @Test
    public void testDelete() {
        api.deleteUser(user.getId());
        assertFalse(api.getAll(Location.class).contains(location));

    }

}

package name.eipi.pacemaker.controllers;

import name.eipi.pacemaker.BaseTestPacemaker;
import name.eipi.pacemaker.models.Activity;
import name.eipi.pacemaker.models.TestData;
import name.eipi.pacemaker.models.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by naysayer on 19/10/2014.
 */
public class PacemakerApiTestActivityActions extends BaseTestPacemaker {

    PacemakerImpl api;

    private User user = TestData.createUser();
    private Activity activity = TestData.createActivity();
    private ApiResponse<Activity> response = null;

    @Before
    public void setUp() {
        api = new PacemakerImpl("PacemakerApiTest");
        user = api.createUser(user).value();
        response = api.addActivity(user.getId(), activity);
    }


    @Test
    public void testCreate() {
        assertTrue(response.isSuccess());
        activity = response.value();
        assertNotNull(activity.getId());
        assertTrue(api.getUserByEmail(user.getEmail()).getActivities().contains(activity.getId()));
    }

    @Test
    public void testRead() {
        Collection<Activity> readIn = api.getActivities(user.getId());
        assertTrue(readIn.contains(activity));
    }

    @Test
    public void testCreateFail() {
        User newUser = api.createUser(TestData.createUser()).value();
        api.deleteUser(newUser.getId());
        ApiResponse<Activity> create = api.addActivity(newUser.getId(), activity);
        assertFalse(create.isSuccess());

    }

    @Test
    public void testDelete() {
        api.deleteUser(user.getId());
        assertFalse(api.getAll(Activity.class).contains(activity));
    }

}

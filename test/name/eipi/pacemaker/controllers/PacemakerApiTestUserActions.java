package name.eipi.pacemaker.controllers;

import name.eipi.pacemaker.BaseTestPacemaker;
import name.eipi.pacemaker.models.TestData;
import name.eipi.pacemaker.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by naysayer on 12/10/2014.
 */
public class PacemakerApiTestUserActions extends BaseTestPacemaker {

    private static final String email = "dbdonovan@gmail.com";
    PacemakerImpl api;
    User me;
    User you;

    @Before
    public void setUp() {
        api = new PacemakerImpl("PacemakerApiTest");
        me = api.createUser(TestData.createUser(email)).value();
        you = api.createUser("The Great", "Anon", "us@them.pi", "anonymous").value();

    }

    @After
    public void tearDown() {
        me = you = null;
    }

    @Test
    public void testCreate() {

        assertNotNull(me.getId());
        assertNotNull(you.getId());
        assertTrue(api.getUserByEmail(me.getEmail()).equals(me));
    }

    @Test
    public void testRead() {

        assertFalse(api.getUsers().isEmpty());
        assertTrue(api.getUsers().contains(me));
        assertTrue(api.getUsers().contains(you));

        assertTrue(me.equals(api.getUserByEmail(email)));

        assertFalse(you.equals(api.getUserByEmail(email)));
    }

    @Test
    public void testCreateFail() {
        User user = TestData.createUser(email);
        ApiResponse response = api.createUser(user);
        assertFalse(response.isSuccess());
        assertNotNull(response.getMessage());
    }

    @Test
    public void testDelete() {
        ApiResponse<User> response = api.deleteUser(you.getId());
        assertTrue(response.isSuccess());
        assertNotNull(response.value());
        assertTrue(response.value().getEmail().equals(you.getEmail()));
        assertFalse(api.getAll(User.class).contains(you));
    }

}

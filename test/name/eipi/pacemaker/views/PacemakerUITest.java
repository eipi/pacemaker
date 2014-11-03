package name.eipi.pacemaker.views;

import name.eipi.pacemaker.BaseTestPacemaker;
import name.eipi.pacemaker.controllers.PacemakerApi;
import name.eipi.pacemaker.controllers.PacemakerImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.util.Collection;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;


/**
 * Created by dbdon_000 on 20/10/2014.
 */
public class PacemakerUITest extends BaseTestPacemaker {

    private static final PacemakerApi API = new PacemakerImpl("UI_TEST");
    private static final PacemakerUI UI =  new PacemakerUI(API);

    private static final String ONE_STR = "1";

    @Test
    public void testUI() throws Exception {
        UI.createUser(ONE_STR, ONE_STR, ONE_STR, ONE_STR);
        Collection users =  API.getUsers();
        assertNotNull(users);
        assertFalse(users.isEmpty());
        UI.listUsers();
    }


}

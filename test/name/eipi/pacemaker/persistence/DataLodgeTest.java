package name.eipi.pacemaker.persistence;

import name.eipi.pacemaker.BaseTestPacemaker;
import name.eipi.pacemaker.models.User;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class DataLodgeTest extends BaseTestPacemaker {

    @Test
    public void testIO() throws Exception {


        User user = new User("Banana", "Joe", "abc", "etc");
        DataLodge db = new DataLodge("datalodge.test");
        // defaults to json, changing to xml
        db.changeFormat("xml");
        User u = db.edit(user);
        assertFalse(u.getId() == null);
        db.save();
        db.reset();
        assertTrue(db.getAll(User.class).isEmpty());
        db.load();
        User reloaded = db.read(User.class, u.getId());
        assertFalse(reloaded == null);
        assertTrue(reloaded.getEmail().equals(user.getEmail()));

    }

}

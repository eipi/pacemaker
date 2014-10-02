package name.eipi.pacemaker.controllers;

import name.eipi.pacemaker.models.User;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DataLodgeTest {

    @Test
    public void testWriteToFile() throws Exception {


        User user = new User("Banana", "123", "abc", "etc");
        DataLodge db = new DataLodge("datalodge.test");
        User u = db.edit(user);
        assertFalse(u.getId() == null);
        db.save();
        db.reset();
        db.load();
        User reloaded = db.read(User.class, u.getId());
        assertFalse(reloaded == null);
        assertTrue(reloaded.getEmail().equals(user.getEmail()));

    }

}

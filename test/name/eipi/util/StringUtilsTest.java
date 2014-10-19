package name.eipi.util;

import name.eipi.pacemaker.models.User;
import name.eipi.pacemaker.persistence.DataLodge;
import name.eipi.pacemaker.util.StringUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by dbdon_000 on 28/09/2014.
 */
public class StringUtilsTest {

    @Test
    public void testFancyToString() throws Exception {
        DataLodge db = new DataLodge("utilities.test");
        User user = db.edit(new User("Damien", "Donovan", "123@abc.ie", "Something"));

        String output = StringUtils.toFancyString(user);
        validateOutput(output);

        user = null;
        assertTrue("\u2205".equals(StringUtils.toFancyString(user)));


    }


    @Test
    public void testFancyToStringCollections() throws Exception {
        DataLodge db = new DataLodge("utilities.test");
        User u1 = db.edit(new User("Damien", "Donovan", "123@abc.ie", "Something"));
        User u2 = db.edit(new User("Bones", "Malone", "bones@snoop.com", "Something"));
        Collection<User> c = new ArrayList<>();
        c.add(u1);
        c.add(u2);
        String output = StringUtils.toFancyString(c);
        validateOutput(output);

    }

    /**
     * Basic structural validation of the outer table only.
     *
     * @param output
     * @throws Exception
     */
    private void validateOutput(String output) throws Exception {
        BufferedReader reader = new BufferedReader(new StringReader(output));
        int firstLineLength = -1;

        String line = null;
        while ((line = reader.readLine()) != null) {
            if (firstLineLength == -1) {
                if (line.length() > 0) {
                    firstLineLength = line.length();
                }
            } else {
                assertTrue(line.length() == 0 || line.length() == firstLineLength);
            }

        }

    }

}

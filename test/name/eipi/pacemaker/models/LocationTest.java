package name.eipi.pacemaker.models;

import name.eipi.pacemaker.persistence.DataLodge;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by naysayer on 18/10/2014.
 */
public class LocationTest {

    DataLodge dataLodge = new DataLodge();

    @Test
    public void testCreate()
    {
        Location one = dataLodge.edit(new Location(23, 33));
        Location two = dataLodge.edit(new Location(46, 66));
        assertNotNull(one.getId());
        assertNotNull(one.toString());
        assertNotNull(two.getId());
        assertFalse(one.getId().equals(two.getId()));
        assertFalse(one.equals(two));
        assertEquals (23, one.getLatitude());
        assertEquals (33, one.getLongitude());
        assertEquals (46, two.getLatitude());
        assertEquals (66, two.getLongitude());

    }



}

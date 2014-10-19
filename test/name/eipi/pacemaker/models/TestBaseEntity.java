package name.eipi.pacemaker.models;

import name.eipi.pacemaker.BaseTestPacemaker;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Created by naysayer on 12/10/2014.
 */
public class TestBaseEntity extends BaseTestPacemaker {

    @Test
    public void testBaseEntity() {
        TestEntity testEntity = new TestEntity();
        testEntity.uid = Math.random();
        assertNotNull(testEntity.toString());
        assertTrue(!testEntity.toString().isEmpty());
        TestEntity testEntity1 = new TestEntity();
        testEntity1.uid = Math.random();
        TestEntity testEntity2 = testEntity1;
        TestEntity testEntity3 = testEntity1;
        testEntity3.uid = null;
        assertTrue(testEntity1.hashCode() == testEntity2.hashCode());
        assertTrue(testEntity1.equals(testEntity2));
        assertTrue(testEntity2.equals(testEntity1));
        assertFalse(testEntity.equals(testEntity1));
        assertFalse(testEntity2.equals(testEntity));
        assertFalse(testEntity.equals(testEntity3));
        assertFalse(testEntity3.equals(testEntity));
    }

}

class TestEntity extends BaseEntity {
    Double uid;
}

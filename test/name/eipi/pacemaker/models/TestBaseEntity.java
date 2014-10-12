package name.eipi.pacemaker.models;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by naysayer on 12/10/2014.
 */
public class TestBaseEntity {

    @Test
    public void testBaseEntity() {
        TestEntity testEntity = new TestEntity();
        testEntity.uid = Math.random();
        assertNotNull(testEntity.toString());
        assertTrue(!testEntity.toString().isEmpty());
        TestEntity testEntity1 = new TestEntity();
        testEntity1.uid = Math.random();
        TestEntity testEntity2 = testEntity1;
        assertTrue(testEntity1.hashCode() == testEntity2.hashCode());
        assertTrue(testEntity1.equals(testEntity2));
        assertTrue(testEntity2.equals(testEntity1));
        assertFalse(testEntity.equals(testEntity1));
        assertFalse(testEntity2.equals(testEntity));
    }

}

class TestEntity extends BaseEntity {
    double uid;
}

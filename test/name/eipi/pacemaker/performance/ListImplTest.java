package name.eipi.pacemaker.performance;

import junit.framework.TestCase;
import name.eipi.pacemaker.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class ListImplTest extends TestCase {

    @Override
    @Before
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    @After
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testEach() {
        double numTests = 30;
        long arrayListWriteTime = 0;
        long arrayListReadTime = 0;
        long linkedListWriteTime = 0;
        long linkedListReadTime = 0;

        for (short i = 0; i < numTests; i++) {

            // Test ArrayList
            long[] arrayListResults = implTest(new ArrayList<User>());
            arrayListWriteTime += arrayListResults[0];
            arrayListReadTime += arrayListResults[1];

            // Test LinkedList
            long[] linkedListResults = implTest(new LinkedList<User>());
            linkedListWriteTime += linkedListResults[0];
            linkedListReadTime += linkedListResults[1];

        }

        System.out.println("ArrayList :: Write : " + arrayListWriteTime
                / numTests + ", Read : " + arrayListReadTime / numTests);
        System.out.println("LinkedList :: Write : " + linkedListWriteTime
                / numTests + ", Read : " + linkedListReadTime / numTests);
    }

    public long[] implTest(Collection<User> collectionImpl) {

        long writeTime = 0;
        long readTime = 0;

        for (int i = 1; i < 100; i++) {
            writeTime += addDataTo(collectionImpl);
            readTime += iterateOver(collectionImpl);

        }

        return new long[]{writeTime, readTime};
    }

    private long iterateOver(Collection<User> c) {
        long startTime = System.currentTimeMillis();
        for (Iterator<User> it = c.iterator(); it.hasNext(); ) {
            User u = it.next();
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private long addDataTo(Collection<User> c) {
        long startTime = System.currentTimeMillis();
        for (int i = 1; i < 100; i++) {
            c.add(new User("" + i + 1, "" + 1001, "" + i + 1 + "@" + 1001, ""
                    + System.currentTimeMillis()));
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

}

package name.eipi.services.logger;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by naysayer on 18/10/2014.
 */
public class LoggerTest {

    private Logger LOGGER;

    @Before
    public void setUp() {
        LoggerFactory.setTestMode(Boolean.TRUE);
        LOGGER = LoggerFactory.getInstance(LoggerTest.class);

    }

    @Test
    public void testLogger() {

        LOGGER.debug("Debug message");
        LOGGER.error("Error message");
        LOGGER.debug("Debug with object", new Date());
        LOGGER.error("Error with exception", new Exception("Test Exception"));

    }

}

package name.eipi.services.logger;

/**
 * Created by dbdon_000 Date: 10/08/13
 */
public class LoggerFactory {

    // TODO move to props file
    private static String PATH = "logs/";
    private static String PREFIX = "Pacemaker";


    private static boolean DELETE_ON_EXIT;

    private LoggerFactory() {
        // no
    }

    public static void setTestMode(Boolean isTest) {
        DELETE_ON_EXIT = isTest;
        if (!PREFIX.endsWith("Test")) {
            PREFIX += "Test";
        }
    }

    public static Logger getInstance(final Class clazz) {
        return new LoggerImpl(PATH, PREFIX, clazz, DELETE_ON_EXIT);
    }


}

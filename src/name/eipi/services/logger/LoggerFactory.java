package name.eipi.services.logger;

/**
 * Created by dbdon_000 Date: 10/08/13
 */
public class LoggerFactory {

    private static final char PATH_SEP = '/';
    private static String LOG_PATH = "logs/";
    private static String APP_NAME = "Pacemaker";

    private LoggerFactory() {
        // no
    }

    public static Logger getInstance(final Class clazz) {
        return new LoggerImpl(clazz);
    }

    public static String getLogPath() {

        return LOG_PATH;
    }

    public static void setLogPath(String logPath) {
        if (logPath == null) {
            LOG_PATH = "";
        } else {
            LOG_PATH = logPath
                    + (PATH_SEP == logPath.charAt(LOG_PATH.length() - 1) ? "" : PATH_SEP);
        }
    }

    public static String getAppName() {
        return APP_NAME;
    }

    public static void setAppName(String appName) {
        APP_NAME = appName;
    }

}

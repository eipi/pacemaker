package name.eipi.services.logger;

import com.google.gson.Gson;
import name.eipi.services.constants.LoggerConstants;
import name.eipi.services.fileservice.TextFile;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dbdon_000 Date: 10/08/13
 */
public class LoggerImpl implements Logger {

    private static final Gson Gsonisfier = new Gson();

    private final String className;

    private final String path;

    private final String prefix;

    private final boolean deleteOnExit;

    private static final char PATH_SEP = '/';

    LoggerImpl(final String path, final String prefix, final Class clazz, final boolean deleteOnExit) {
        className = clazz.getName();
        this.path = validatePath(path);
        this.prefix = prefix;
        this.deleteOnExit = deleteOnExit;
    }

    private static String createLog(LoggerImpl logger, String message,
                                    Object object) {

        String innerText = "";

        if (object != null) {
            innerText = "\r\n"
                    .concat((object instanceof Throwable) ? ExceptionUtils
                            .getStackTrace((Throwable) object) : Gsonisfier
                            .toJson(object));
        }

        return "[" + logger.className + "] " + new Date() + " [" + message
                + innerText + "]\r\n";

    }

    @Override
    public void debug(String message, Object object) {

        try {
            String filePath = buildPath(LoggerConstants.DEBUG);
            String logMessage = createLog(this, message, object);
            TextFile.at(filePath).write(logMessage);
            if (deleteOnExit) {
                TextFile.at(filePath).deleteOnExit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void error(String message, Object object) {
        try {
            String filePath = buildPath(LoggerConstants.ERROR);
            String errorMessage = createLog(this, message, object);
            TextFile.at(filePath).write(errorMessage);
            if (deleteOnExit) {
                TextFile.at(filePath).deleteOnExit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void debug(String message) {
        debug(message, null);
    }

    @Override
    public void error(String message) {
        error(message, null);
    }

    private String validatePath(String logPath) {
        if (logPath == null) {
            return "";
        } else {
            return logPath + (PATH_SEP == logPath.charAt(logPath.length() - 1) ? "" : PATH_SEP);
        }
    }

    private String buildPath(String logLevel) {
        final Calendar CALENDAR = Calendar.getInstance();
        return path + prefix + logLevel + CALENDAR.get(Calendar.YEAR)
                + CALENDAR.get(Calendar.MONTH) + CALENDAR.get(Calendar.DATE);
    }

}

package name.eipi.services.logger;

import com.google.gson.Gson;
import name.eipi.services.constants.LoggerConstants;
import name.eipi.services.fileservice.FetchFileCmd;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dbdon_000 Date: 10/08/13
 */
public class LoggerImpl implements Logger {

    private static final Calendar CALENDAR = Calendar.getInstance();

    private static final Gson Gsonisfier = new Gson();

    private final String className;

    LoggerImpl(final Class clazz) {
        className = clazz.getName();
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

    private static String getPath() {
        return LoggerFactory.getLogPath() + LoggerFactory.getAppName()
                + LoggerConstants.DEBUG + CALENDAR.get(Calendar.YEAR)
                + CALENDAR.get(Calendar.MONTH) + CALENDAR.get(Calendar.DATE);
    }

    @Override
    public void debug(String message, Object object) {

        try {
            FetchFileCmd.fetchTextFile(getPath()).write(
                    createLog(this, message, object));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void error(String message, Object object) {
        try {
            FetchFileCmd.fetchTextFile(getPath()).write(
                    createLog(this, message, object));
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

}

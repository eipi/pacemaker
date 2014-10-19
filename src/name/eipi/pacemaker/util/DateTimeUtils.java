package name.eipi.pacemaker.util;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Created by naysayer on 19/10/2014.
 */
public class DateTimeUtils {

    public static DateTime parseDateTime(String dateTimeString) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd:MM:yyyy HH:mm:ss");
        return fmt.parseDateTime(dateTimeString);

    }

    public static Duration parseDuration(String durationString) {
        PeriodFormatter hoursMinutesSeconds = new PeriodFormatterBuilder()
                .appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .appendSeparator(":")
                .appendSeconds()
                .toFormatter();
        return hoursMinutesSeconds.parsePeriod(durationString).toStandardDuration();

    }

}

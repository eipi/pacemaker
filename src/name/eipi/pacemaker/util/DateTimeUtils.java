package name.eipi.pacemaker.util;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by naysayer on 19/10/2014.
 */
public class DateTimeUtils {

    private static final Collection<DateTimeFormatter> dateTimeFormatters;
    private static final Collection<PeriodFormatter> periodFormatters;

    static {
        // No need to be so strict.
        dateTimeFormatters = new ArrayList<>();
        dateTimeFormatters.addAll(getDateTimeFormatters(":"));
        dateTimeFormatters.addAll(getDateTimeFormatters("/"));
        dateTimeFormatters.addAll(getDateTimeFormatters("-"));

        periodFormatters = new ArrayList<>();
        periodFormatters.addAll(getPeriodFormatters());

    }

    public static DateTime parseDateTime(String dateTimeString) {
        DateTime result = null;
        Iterator<DateTimeFormatter> it =  dateTimeFormatters.iterator();
        while (it.hasNext() && result == null) {
            try {
                result = it.next().parseDateTime(dateTimeString);
            } catch (Exception ex) {
                // try it again with a different format if we have one, else except
              if (!it.hasNext()) {
                  throw ex;
              }
            }
        }
        return result;

    }

    public static Duration parseDuration(String durationString) {
        Period result = null;
        Iterator<PeriodFormatter> it =  periodFormatters.iterator();
        while (it.hasNext() && result ==  null) {
            try {
                result = it.next().parsePeriod(durationString);
            } catch (Exception ex) {
                // try it again with a different format if we have one, else except
                if (!it.hasNext()) {
                    throw ex;
                }
            }
        }
        return result.toStandardDuration();
    }

    private static final Collection<DateTimeFormatter> getDateTimeFormatters(String separator) {
        Collection<DateTimeFormatter> formatters = new ArrayList<>();

        StringBuffer buffer = new StringBuffer();
        buffer.append("dd").append(separator);
        buffer.append("MM").append(separator);
        buffer.append("yyyy");
        formatters.add(DateTimeFormat.forPattern(buffer.toString()));
        formatters.add(DateTimeFormat.forPattern(buffer.append(" HH:mm").toString()));
        formatters.add(DateTimeFormat.forPattern(buffer.append(":ss").toString()));

        return formatters;

    }

    private static final Collection<PeriodFormatter> getPeriodFormatters() {
        Collection<PeriodFormatter> formatters = new ArrayList<>();
        PeriodFormatterBuilder builder = new PeriodFormatterBuilder()
                .appendHours().appendSeparator(":")
                .appendMinutes();
        formatters.add(builder.toFormatter());
        formatters.add(builder.appendSeparator(":").appendSeconds().toFormatter());

        return formatters;
    }

}

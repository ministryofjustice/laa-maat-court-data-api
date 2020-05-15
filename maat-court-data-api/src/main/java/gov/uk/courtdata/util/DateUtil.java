package gov.uk.courtdata.util;

import java.time.LocalDate;

public final class DateUtil {

    /***
     *  Parse date string to localdate if not null
     *  else
     *  return null;
     * @param date
     * @return
     */
    public static LocalDate parse(final String date) {
        return date != null ? LocalDate.parse(date) : null;
    }


}

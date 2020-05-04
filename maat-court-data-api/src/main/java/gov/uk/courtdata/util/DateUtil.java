package gov.uk.courtdata.util;

import java.time.LocalDate;

public class DateUtil {

    public static LocalDate toDate(final String date) {
        return date != null ? LocalDate.parse(date) : null;
    }
}

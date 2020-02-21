package gov.uk.courtdata.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CourtDataUtil {

    public LocalDate getDate(String date) {
        return date != null ? LocalDate.parse(date) : null;
    }
}

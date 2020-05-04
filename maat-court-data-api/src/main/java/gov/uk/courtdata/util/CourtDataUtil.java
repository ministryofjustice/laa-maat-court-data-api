package gov.uk.courtdata.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class CourtDataUtil {

    public LocalDate getDate(String date) {
        return date != null ? LocalDate.parse(date) : null;
    }

    public LocalDateTime getDateTime(String date) {
        return date != null ? LocalDateTime.parse(date) : null;
    }
}

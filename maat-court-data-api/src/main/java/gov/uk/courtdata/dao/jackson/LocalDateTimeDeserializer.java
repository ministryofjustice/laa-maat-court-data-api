package gov.uk.courtdata.dao.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private static final String NULL_VALUE = "null";

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws
            IOException {

        String dateTime = jsonParser.getValueAsString();
        try {
            if (StringUtils.isNotBlank(dateTime) && !dateTime.trim().equals(NULL_VALUE)) {
                return LocalDateTime.parse(dateTime, ISO_LOCAL_DATE_TIME);
            }
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Invalid date value: " + dateTime, e);
        }
        return null;
    }
}

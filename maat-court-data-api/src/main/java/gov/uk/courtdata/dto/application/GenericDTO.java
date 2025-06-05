package gov.uk.courtdata.dto.application;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.ZonedDateTime;
import uk.gov.justice.laa.crime.jackson.ZonedDateTimeDeserializer;

@Data
@SuperBuilder
@NoArgsConstructor
public class GenericDTO {
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime timestamp;
    private Boolean selected;
    @Builder.Default
    private Boolean mDirty = false;
}

package gov.uk.courtdata.dto.application;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import uk.gov.justice.laa.crime.commons.jackson.ZonedDateTimeDeserializer;

import java.time.ZonedDateTime;

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

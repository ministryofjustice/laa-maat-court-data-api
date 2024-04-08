package gov.uk.courtdata.dto.application;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import gov.uk.courtdata.dao.jackson.LocalDateTimeDeserializer;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class GenericDTO {
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;
    private Boolean selected;
    @Builder.Default
    private Boolean mDirty = false;
}

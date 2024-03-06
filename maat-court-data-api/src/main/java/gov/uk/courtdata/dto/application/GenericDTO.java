package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
public class GenericDTO {
    private LocalDateTime timestamp;
    private Boolean selected;
    @Builder.Default
    private Boolean mDirty = false;
}

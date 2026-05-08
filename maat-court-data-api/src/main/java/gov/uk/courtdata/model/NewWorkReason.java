package gov.uk.courtdata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonValue;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewWorkReason {

    @JsonValue
    private String code;

    private String type;
    private String description;
    private LocalDateTime dateCreated;
    private String userCreated;
    private LocalDateTime dateModified;
    private String userModified;
    private Integer sequence;
    private String enabled;
    private String raGroup;
    private String initialDefault;
}

package gov.uk.courtdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewWorkReason {

    private String code;
    private String type;
    private String description;
    @JsonIgnore
    private LocalDateTime dateCreated;
    @JsonIgnore
    private String userCreated;
    @JsonIgnore
    private LocalDateTime dateModified;
    @JsonIgnore
    private String userModified;
    @JsonIgnore
    private Integer sequence;
    @JsonIgnore
    private String enabled;
    @JsonIgnore
    private String raGroup;
    @JsonIgnore
    private String initialDefault;
}

package gov.uk.courtdata.model.hardship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gov.uk.courtdata.enums.HardshipReviewProgressAction;
import gov.uk.courtdata.enums.HardshipReviewProgressResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HardshipReviewProgress {

    private Integer id;
    private LocalDateTime dateRequested;
    private LocalDateTime dateRequired;
    private LocalDateTime dateCompleted;
    private LocalDateTime timestamp;

    private HardshipReviewProgressAction progressAction;
    private HardshipReviewProgressResponse progressResponse;

    @JsonIgnore
    private LocalDateTime dateCreated;
    @JsonIgnore
    private LocalDateTime dateModified;
    @JsonIgnore
    private LocalDateTime removedDate;
    @JsonIgnore
    private String userCreated;
    @JsonIgnore
    private String userModified;

    public LocalDateTime getTimestamp() {
        return dateModified != null ? dateModified : dateCreated;
    }
}

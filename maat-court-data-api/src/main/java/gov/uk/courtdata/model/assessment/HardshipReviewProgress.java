package gov.uk.courtdata.model.assessment;

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
    private Integer hardshipReviewId;
    private LocalDateTime dateRequested;
    private LocalDateTime dateRequired;
    private LocalDateTime dateCompleted;
    private LocalDateTime dateCreated;
    private String userCreated;
    private LocalDateTime dateModified;
    private String userModified;
    @Builder.Default
    private String active = "N";
    private LocalDateTime removedDate;

    private HardshipReviewProgressAction progressAction;
    private HardshipReviewProgressResponse progressResponse;
}

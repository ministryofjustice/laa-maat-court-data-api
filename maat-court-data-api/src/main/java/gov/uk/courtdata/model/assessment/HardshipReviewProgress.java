package gov.uk.courtdata.model.assessment;

import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.enums.HardshipReviewDetailCode;
import gov.uk.courtdata.enums.HardshipReviewProgressAction;
import gov.uk.courtdata.enums.HardshipReviewProgressResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HardshipReviewProgress {
    private Integer id;
    private Integer hardshipReviewId;
    private HardshipReviewProgressAction hardshipReviewProgressAction;
    private HardshipReviewProgressResponse hardshipReviewProgressResponse;
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
}

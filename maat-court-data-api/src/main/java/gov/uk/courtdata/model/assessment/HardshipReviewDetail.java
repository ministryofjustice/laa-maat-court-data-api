package gov.uk.courtdata.model.assessment;

import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.enums.HardshipReviewDetailCode;
import gov.uk.courtdata.enums.HardshipReviewDetailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HardshipReviewDetail {
    private Integer id;
    private Integer hardshipReviewId;
    private HardshipReviewDetailType type;
    private LocalDateTime dateCreated;
    private String userCreated;
    private Frequency frequency;
    private LocalDateTime dateReceived;
    private String description;
    private BigDecimal amount;
    private LocalDateTime dateDue;
    private String accepted;
    private String reasonResponse;
    private LocalDateTime dateModified;
    private String userModified;
    private Integer detailReasonId;
    private String reasonNote;
    private HardshipReviewDetailCode detailCode;
    private String otherDescription;
    private Boolean active;
    private LocalDateTime removedDate;
}

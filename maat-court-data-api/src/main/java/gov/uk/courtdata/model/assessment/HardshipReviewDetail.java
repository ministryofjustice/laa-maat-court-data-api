package gov.uk.courtdata.model.assessment;

import gov.uk.courtdata.enums.Frequency;
import gov.uk.courtdata.enums.HardshipReviewDetailCode;
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
    private String hardshipDetailType;
    private LocalDateTime dateCreated;
    private String userCreated;
    private Frequency frequency;
    private LocalDateTime dateReceived;
    private String detailDescription;
    private BigDecimal amountNumber;
    private LocalDateTime dateDue;
    private Boolean accepted;
    private String reasonResponse;
    private LocalDateTime dateModified;
    private String userModified;
    private Integer hardshipReviewDetailReasonId;
    private String hardshipReviewReasonNote;
    private HardshipReviewDetailCode hardshipReviewDetailCode;
    private String otherDescription;
    private Boolean active;
    private LocalDateTime removedDate;
}

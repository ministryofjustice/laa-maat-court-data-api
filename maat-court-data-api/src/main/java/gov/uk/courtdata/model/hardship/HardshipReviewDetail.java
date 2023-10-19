package gov.uk.courtdata.model.hardship;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Frequency frequency;
    private LocalDateTime dateReceived;
    private BigDecimal amount;
    private LocalDateTime dateDue;
    private String accepted;
    private String otherDescription;
    private String reasonNote;
    private HardshipReviewDetailType detailType;
    private HardshipReviewDetailCode detailCode;
    private String detailReason;
    private LocalDateTime timestamp;
    private String userCreated;

    @JsonIgnore()
    private LocalDateTime dateCreated;
    @JsonIgnore
    private LocalDateTime dateModified;
    @JsonIgnore
    private String description;
    @JsonIgnore
    private String reasonResponse;
    @JsonIgnore
    private String userModified;
    @JsonIgnore
    private Boolean active;
    @JsonIgnore
    private LocalDateTime removedDate;

    public LocalDateTime getTimestamp() {
        return dateModified != null ? dateModified : dateCreated;
    }
}

package gov.uk.courtdata.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gov.uk.courtdata.enums.HardshipReviewStatus;
import gov.uk.courtdata.model.NewWorkReason;
import gov.uk.courtdata.model.hardship.HardshipReviewDetail;
import gov.uk.courtdata.model.hardship.HardshipReviewProgress;
import gov.uk.courtdata.model.hardship.SolicitorCosts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HardshipReviewDTO {

    private Integer id;
    private Integer caseManagementUnitId;
    private String notes;
    private String decisionNotes;
    private LocalDateTime reviewDate;
    private String reviewResult;
    private Double disposableIncome;
    private Double disposableIncomeAfterHardship;
    private LocalDateTime timestamp;
    private NewWorkReason newWorkReason;
    private SolicitorCosts solicitorCosts;
    private HardshipReviewStatus status;

    @JsonIgnore
    private LocalDateTime dateCreated;
    @JsonIgnore
    private Integer repId;
    @JsonIgnore
    private String userCreated;
    @JsonIgnore
    private LocalDateTime updated;
    @JsonIgnore
    private String userModified;
    @JsonIgnore
    private LocalDateTime resultDate;
    @JsonIgnore
    private String courtType;
    @JsonIgnore
    private Integer financialAssessmentId;
    @JsonIgnore
    private String valid;

    private List<HardshipReviewDetail> reviewDetails;
    private List<HardshipReviewProgress> reviewProgressItems;

    public LocalDateTime getTimestamp() {
        return updated != null ? updated : dateCreated;
    }
}

package gov.uk.courtdata.dto;

import gov.uk.courtdata.enums.HardshipReviewStatus;
import gov.uk.courtdata.model.assessment.HardshipReviewDetail;
import gov.uk.courtdata.model.assessment.HardshipReviewProgress;
import gov.uk.courtdata.model.assessment.SolicitorCosts;
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
    private String newWorkOrderCode;
    private LocalDateTime dateCreated;
    private Integer caseManagementUnitId;
    private String reviewResult;
    private LocalDateTime reviewDate;
    private String notes;
    private String decisionNotes;
    private SolicitorCosts solicitorCosts;
    private Double disposableIncome;
    private Double disposableIncomeAfterHardship;
    private HardshipReviewStatus status;

    private Integer repId;
    private String userCreated;
    private LocalDateTime updated;
    private String userModified;
    private LocalDateTime resultDate;
    private String courtType;
    private Integer financialAssessmentId;
    private String valid;

    private List<HardshipReviewDetail> reviewDetails;
    private List<HardshipReviewProgress> reviewProgresses;
}

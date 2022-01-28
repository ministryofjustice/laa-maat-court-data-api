package gov.uk.courtdata.model.hardship;

import gov.uk.courtdata.enums.HardshipReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(callSuper = true)
public class CreateHardshipReview {

    private Integer id;
    private Integer repId;
    private String nworCode;
    private LocalDateTime dateCreated;
    private String userCreated;
    private Integer cmuId;
    private String reviewResult;
    private LocalDateTime reviewDate;
    private LocalDateTime resultDate;
    private String notes;
    private String decisionNotes;
    private SolicitorCosts solicitorCosts;
    private BigDecimal disposableIncome;
    private BigDecimal disposableIncomeAfterHardship;
    private String courtType;
    private Integer financialAssessmentId;
    private HardshipReviewStatus status;

    private List<HardshipReviewDetail> reviewDetails;
}

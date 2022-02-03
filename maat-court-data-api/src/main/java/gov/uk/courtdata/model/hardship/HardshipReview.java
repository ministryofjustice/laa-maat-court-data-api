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
public class HardshipReview {

    private String nworCode;
    private Integer cmuId;
    private String reviewResult;
    private LocalDateTime resultDate;
    private LocalDateTime reviewDate;
    private String notes;
    private String decisionNotes;
    private SolicitorCosts solicitorCosts;
    private BigDecimal disposableIncome;
    private BigDecimal disposableIncomeAfterHardship;
    private HardshipReviewStatus status;
    private List<HardshipReviewDetail> reviewDetails;
    private List<HardshipReviewProgress> reviewProgressItems;


}

package gov.uk.courtdata.dto;

import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.model.assessment.HardshipReviewDetail;
import gov.uk.courtdata.model.assessment.HardshipReviewProgress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HardshipReviewDTO {
    private Integer id;
    private Integer repId;
    private String nworCode;
    private LocalDateTime dateCreated;
    private String userCreated;
    private Integer cmuId;
    private String reviewResult;
    private LocalDateTime reviewDate;
    private String notes;
    private String decisionNotes;
    private Double solicitorRate;
    private Double solicitorHours;
    private Double solicitorVat;
    private Double solicitorDisb;
    private Double solicitorEstTotalCost;
    private Double disposableIncome;
    private Double disposableIncomeAfterHardship;
    private LocalDateTime updated;
    private String userModified;
    private LocalDateTime resultDate;
    private String cotyCourtType;
    private String hresStatus;
    private Integer fiasId;
    @Builder.Default
    private String replaced = "N";
    private String valid;
    private Set<HardshipReviewDetail> hardshipReviewDetailSet;
    private Set<HardshipReviewProgress> hardshipReviewProgressSet;
}

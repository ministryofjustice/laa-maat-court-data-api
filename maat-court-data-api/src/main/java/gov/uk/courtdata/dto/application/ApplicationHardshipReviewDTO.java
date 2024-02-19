package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ApplicationHardshipReviewDTO extends GenericDTO {
    private Long id;
    private Long cmuId;
    private String reviewResult;
    private Date reviewDate;
    private String notes;
    private String decisionNotes;
    private String test;
    private BigDecimal disposableIncome;
    private BigDecimal disposableIncomeAfterHardship;
    @Builder.Default
    private SupplierDTO supplier = new SupplierDTO();
    @Builder.Default
    private NewWorkReasonDTO newWorkReason = new NewWorkReasonDTO();
    @Builder.Default
    private HRSolicitorsCostsDTO solictorsCosts = new HRSolicitorsCostsDTO();
    @Builder.Default
    private Collection<HRSectionDTO> section = new ArrayList<>();
    @Builder.Default
    private Collection<HRProgressDTO> progress = new ArrayList<>();
    @Builder.Default
    private AssessmentStatusDTO asessmentStatus = new AssessmentStatusDTO();
}

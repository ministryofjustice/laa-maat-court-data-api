package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class HardshipReviewDTO extends GenericDTO {

    private Long id;
    private SupplierDTO supplier;
    private NewWorkReasonDTO newWorkReason;
    private Long cmuId;
    private String reviewResult;
    private Date reviewDate;
    private String notes;
    private String decisionNotes;
    private HRSolicitorsCostsDTO solictorsCosts;
    private SysGenCurrency disposableIncome;
    private SysGenCurrency disposableIncomeAfterHardship;
    private Collection<HRSectionDTO> section;
    private Collection<HRProgressDTO> progress;
    private AssessmentStatusDTO asessmentStatus;

}

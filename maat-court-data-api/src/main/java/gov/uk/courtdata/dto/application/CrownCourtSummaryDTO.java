package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CrownCourtSummaryDTO extends GenericDTO {
    public static final String REP_ORDER_DECISION_GRANTED = "Granted";
    public static final String REP_TYPE_CC_ONLY = "Crown Court Only";

    private Long ccRepId;
    private SysGenString ccRepType;
    private Date ccRepOrderDate;
    private Date sentenceOrderDate;
    private Date ccWithDrawalDate;
    private SysGenString repOrderDecision;
    private Boolean inPrisoned;
    private Boolean benchWarrantyIssued;

    @Builder.Default
    private OutcomeDTO ccOutcome = new OutcomeDTO();
    @Builder.Default
    private OutcomeDTO ccAppealOutcome = new OutcomeDTO();
    @Builder.Default
    private EvidenceFeeDTO evidenceProvisionFee = new EvidenceFeeDTO();
    @Builder.Default
    private Collection<OutcomeDTO> outcomeDTOs = new ArrayList<>();
}

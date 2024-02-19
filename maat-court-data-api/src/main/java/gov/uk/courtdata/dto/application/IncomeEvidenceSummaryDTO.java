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
public class IncomeEvidenceSummaryDTO extends GenericDTO {
    private Date evidenceDueDate;
    private Date evidenceReceivedDate;
    private Date upliftAppliedDate;
    private Date upliftRemovedDate;
    private Date firstReminderDate;
    private Date secondReminderDate;
    private String incomeEvidenceNotes;
    private Boolean upliftsAvailable;

    @Builder.Default
    private Boolean enabled = false;
    @Builder.Default
    private Collection<ExtraEvidenceDTO> extraEvidenceList = new ArrayList<>();
    @Builder.Default
    private Collection<EvidenceDTO> applicantIncomeEvidenceList = new ArrayList<>();
    @Builder.Default
    private Collection<EvidenceDTO> partnerIncomeEvidenceList = new ArrayList<>();
}


package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Collection;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CrownCourtOverviewDTO extends GenericDTO {
    private Boolean available;

    @Builder.Default
    private AppealDTO appealDTO = new AppealDTO();
    @Builder.Default
    private ContributionsDTO contribution = new ContributionsDTO();
    @Builder.Default
    private CrownCourtSummaryDTO crownCourtSummaryDTO = new CrownCourtSummaryDTO();
    @Builder.Default
    private Collection<ContributionSummaryDTO> contributionSummary = new ArrayList<>();
    @Builder.Default
    private ApplicantPaymentDetailsDTO applicantPaymentDetailsDTO = new ApplicantPaymentDetailsDTO();
    @Builder.Default
    private Collection<CorrespondenceDTO> correspondence = new ArrayList<>();

}

package gov.uk.courtdata.hearing.crowncourt;

import gov.uk.courtdata.model.hearing.HearingDetails;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrownCourtProcessingImpl {

    private final CrownCourtProcessingRepository crownCourtProcessingRepository;


    public void execute(HearingDetails hearingDetails) {

        crownCourtProcessingRepository.invokeCrownCourtOutcomeProcess(hearingDetails.getMaatId(),
                hearingDetails.getCcooOutcome(),
                hearingDetails.getBenchWarrantIssuedYn(),
                hearingDetails.getAppealType(),
                hearingDetails.getCcImprisioned(),
                hearingDetails.getCaseUrn(),
                hearingDetails.getCrownCourtCode());
    }
}

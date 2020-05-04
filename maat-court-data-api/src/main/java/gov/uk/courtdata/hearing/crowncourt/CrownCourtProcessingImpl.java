package gov.uk.courtdata.hearing.crowncourt;

import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrownCourtProcessingImpl {

    private final CrownCourtProcessingRepository crownCourtProcessingRepository;


    public void execute(HearingResulted hearingResulted) {

        crownCourtProcessingRepository.invokeCrownCourtOutcomeProcess(hearingResulted.getMaatId(),
                hearingResulted.getCcooOutcome(),
                hearingResulted.getBenchWarrantIssuedYn(),
                hearingResulted.getAppealType(),
                hearingResulted.getCcImprisioned(),
                hearingResulted.getCaseUrn(),
                hearingResulted.getCrownCourtCode());
    }
}

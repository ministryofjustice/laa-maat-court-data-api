package gov.uk.courtdata.hearing.crowncourt.service;

import gov.uk.courtdata.enums.CrownCourtTrialOutcome;
import gov.uk.courtdata.enums.PleaTrialOutcome;
import gov.uk.courtdata.enums.VerdictTrialOutcome;
import gov.uk.courtdata.hearing.crowncourt.impl.CrownCourtProcessingImpl;
import gov.uk.courtdata.hearing.crowncourt.impl.OffenceHelper;
import gov.uk.courtdata.hearing.crowncourt.validator.CrownCourtValidationProcessor;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrownCourtHearingService {

    private final CrownCourtValidationProcessor crownCourtValidationProcessor;
    private final CrownCourtProcessingImpl crownCourtProcessingImpl;
    private final HearingResultedImpl hearingResultedImpl;
    private final OffenceHelper offenceHelper;

    public void execute(final HearingResulted hearingResulted) {

        hearingResultedImpl.execute(hearingResulted);

        if (hearingResulted.getCcOutComeData()!=null
                || hearingResulted.isProsecutionConcluded()) {
            hearingResulted.getCcOutComeData().setCcOutcome(calculateCrownCourtOutCome(hearingResulted));
            executeCrownCourtOutCome(hearingResulted);
        }
    }

    private void executeCrownCourtOutCome(HearingResulted hearingResulted) {

        crownCourtValidationProcessor.validate(hearingResulted);
        crownCourtProcessingImpl.execute(hearingResulted);
        log.info("Crown Court Outcome Processing has been Completed for MAAT ID: {}", hearingResulted.getMaatId());
    }

    private String calculateCrownCourtOutCome(HearingResulted hearingResulted) {

        if (hearingResulted.isProsecutionConcluded()) {
            List<String> offenceOutcomeList = new ArrayList<>();
            List<Offence> offenceList = offenceHelper.getOffences(hearingResulted.getMaatId());

            offenceList
                    .forEach(offence -> {

                        if (offence.getVerdict() != null) {
                            offenceOutcomeList.add(VerdictTrialOutcome.getTrialOutcome(offence.getVerdict().getCategoryType()));
                        } else if (offence.getPlea() != null) {
                            offenceOutcomeList.add(PleaTrialOutcome.getTrialOutcome(offence.getPlea().getPleaValue()));
                        }
                    });

            List<String> outcomes = offenceOutcomeList.stream().distinct().collect(Collectors.toList());
            log.info("Offence count: " + outcomes.toString());
            String offenceOutcomeStatus="";

            if (outcomes.size() == 1) {
                offenceOutcomeStatus = outcomes.get(0);
            } else if (outcomes.size() > 1) {
                offenceOutcomeStatus = CrownCourtTrialOutcome.PART_CONVICTED.getValue();
            }
            log.info("Calculated crown court outcome. " + offenceOutcomeStatus);
            return offenceOutcomeStatus;
        }
        return hearingResulted.getCcOutComeData()!=null ? hearingResulted.getCcOutComeData().getCcOutcome() : "";

    }
}

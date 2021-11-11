package gov.uk.courtdata.hearing.crowncourt.impl;

import gov.uk.courtdata.enums.CrownCourtTrialOutcome;
import gov.uk.courtdata.enums.PleaTrialOutcome;
import gov.uk.courtdata.enums.VerdictTrialOutcome;
import gov.uk.courtdata.model.Offence;
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
public class CalculateCrownCourtOutCome {

    private final OffenceHelper offenceHelper;

    public String calculate(HearingResulted hearingResulted) {

        if (hearingResulted.isProsecutionConcluded()) {
            log.info("Calculating crown court outcome");
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
            log.info("Offence count: " + outcomes.stream().collect(Collectors.joining(", ")));
            String offenceOutcomeStatus="";

            if (outcomes.size() == 1) {
                offenceOutcomeStatus = outcomes.get(0);
            } else if (outcomes.size() > 1) {
                offenceOutcomeStatus = CrownCourtTrialOutcome.PART_CONVICTED.getValue();
            }
            log.info("Calculated crown court outcome. " + offenceOutcomeStatus);
            return offenceOutcomeStatus;

        } else {
            return null;
        }
    }
}
package gov.uk.courtdata.prosecutionconcluded.helper;

import gov.uk.courtdata.enums.CrownCourtTrialOutcome;
import gov.uk.courtdata.enums.PleaTrialOutcome;
import gov.uk.courtdata.enums.VerdictTrialOutcome;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.prosecutionconcluded.model.OffenceSummary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculateOutcomeHelper {

    public String calculate(List<OffenceSummary> offenceSummaryList) {


        List<String> outcomes = buildOffenceOutComes(offenceSummaryList);

        log.info("Offence count: " + outcomes.size());
        return outcomes.size() == 1 ? outcomes.get(0) : CrownCourtTrialOutcome.PART_CONVICTED.getValue();


    }

    private List<String> buildOffenceOutComes(List<OffenceSummary> offenceSummaryList) {

        List<String> offenceOutcomeList = new ArrayList<>();
        offenceSummaryList
                .forEach(offence -> {
                    if (isVerdictAvailable(offence)) {
                        if(!isVerdictPleaMismatch(offence)){
                            offenceOutcomeList.add(VerdictTrialOutcome.getTrialOutcome(offence.getVerdict().getVerdictType().getCategoryType()));
                        } else {
                            log.error("The recent Plea outcome is different from the Verdict outcome - " +
                                    "Offence Id {} Plea Date {} Verdict Date {} Plea Outcome {} Verdict Outcome {}",
                                    offence.getOffenceId(), offence.getPlea().getPleaDate(), offence.getVerdict().getVerdictDate(),
                                    PleaTrialOutcome.getTrialOutcome(offence.getPlea().getValue()),
                                    VerdictTrialOutcome.getTrialOutcome(offence.getVerdict().getVerdictType().getCategoryType())
                                    );
                            throw new ValidationException("The recent Plea outcome is different from the Verdict outcome");
                        }
                    } else if (offence.getPlea() != null && offence.getPlea().getValue() != null) {
                        offenceOutcomeList.add(PleaTrialOutcome.getTrialOutcome(offence.getPlea().getValue()));
                    } else {
                        offenceOutcomeList.add(CrownCourtTrialOutcome.AQUITTED.getValue());
                    }
                });

        return offenceOutcomeList.stream().distinct().collect(Collectors.toList());

    }

    private boolean isVerdictPleaMismatch(OffenceSummary offence) {
        if((offence.getPlea() != null && offence.getPlea().getPleaDate() !=null)
                && offence.getVerdict().getVerdictDate().compareTo(offence.getPlea().getPleaDate()) < 0){
            String verdictOutcome = VerdictTrialOutcome.getTrialOutcome(offence.getVerdict().getVerdictType().getCategoryType());
            String pleaOutcome = PleaTrialOutcome.getTrialOutcome(offence.getPlea().getValue());
            if(!verdictOutcome.equalsIgnoreCase(pleaOutcome)){
                return true;
            }
        }
        return false;
    }

    private boolean isVerdictAvailable(OffenceSummary offence) {
        return offence.getVerdict() != null
                && offence.getVerdict().getVerdictType() != null
                && offence.getVerdict().getVerdictType().getCategoryType() != null;
    }
}

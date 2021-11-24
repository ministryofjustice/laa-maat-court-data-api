package gov.uk.courtdata.prosecutionconcluded;

import gov.uk.courtdata.enums.CrownCourtTrialOutcome;
import gov.uk.courtdata.enums.PleaTrialOutcome;
import gov.uk.courtdata.enums.VerdictTrialOutcome;
import gov.uk.courtdata.model.crowncourt.OffenceSummary;
import gov.uk.courtdata.model.crowncourt.ProsecutionConcluded;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CalculateCrownCourtOutcome {

    public String calculate(ProsecutionConcluded prosecutionConcluded) {

        if (prosecutionConcluded.isConcluded()) {
            log.info("Calculating crown court outcome for concluded case id {}", prosecutionConcluded.getProsecutionCaseId());
            List<String> offenceOutcomeList = new ArrayList<>();
            List<OffenceSummary> offenceSummaryList = prosecutionConcluded.getOffenceSummaryList();

            offenceSummaryList
                    .forEach(offence -> {

                        if (offence.getVerdict() != null) {
                            offenceOutcomeList.add(VerdictTrialOutcome.getTrialOutcome(offence.getVerdict().getVerdictType().getCategoryType()));
                        } else if (offence.getPlea() != null) {
                            offenceOutcomeList.add(PleaTrialOutcome.getTrialOutcome(offence.getPlea().getValue()));
                        }
                    });

            List<String> outcomes = offenceOutcomeList.stream().distinct().collect(Collectors.toList());
            log.info("Offence count: " + outcomes.stream().collect(Collectors.joining(", ")));
            String offenceOutcomeStatus = null;

            if (outcomes.size() == 1) {
                offenceOutcomeStatus = outcomes.get(0);
            } else if (outcomes.size() > 1) {
                offenceOutcomeStatus = CrownCourtTrialOutcome.PART_CONVICTED.getValue();
            }

            log.info("Calculated crown court outcome" + offenceOutcomeStatus);
            return offenceOutcomeStatus;
        } else {
            log.info("Case is not concluded");
            return null;
        }
    }
}

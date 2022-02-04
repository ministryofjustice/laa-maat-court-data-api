package gov.uk.courtdata.prosecutionconcluded.helper;

import gov.uk.courtdata.enums.CrownCourtTrialOutcome;
import gov.uk.courtdata.enums.PleaTrialOutcome;
import gov.uk.courtdata.enums.VerdictTrialOutcome;
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

                    if (offence.getVerdict() != null && offence.getVerdict().getVerdictType().getCategoryType() != null) {
                        offenceOutcomeList.add(VerdictTrialOutcome.getTrialOutcome(offence.getVerdict().getVerdictType().getCategoryType()));

                    } else if (offence.getPlea() != null) {
                        offenceOutcomeList.add(PleaTrialOutcome.getTrialOutcome(offence.getPlea().getValue()));
                    }
                });

        return offenceOutcomeList.stream().distinct().collect(Collectors.toList());

    }
}

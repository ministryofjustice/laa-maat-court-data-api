package gov.uk.courtdata.enums;

import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
public enum VerdictTrialOutcome {

    GUILTY(CrownCourtTrialOutcome.CONVICTED),
    GUILTY_BUT_OF_ALTERNATIVE_OFFENCE_BY_JURY_CONVICTED(CrownCourtTrialOutcome.CONVICTED),
    GUILTY_BUT_OF_LESSER_OFFENCE_BY_JURY_CONVICTED(CrownCourtTrialOutcome.CONVICTED),
    GUILTY_BY_JURY_CONVICTED(CrownCourtTrialOutcome.CONVICTED),
    GUILTY_CONVICTED(CrownCourtTrialOutcome.CONVICTED);

    private CrownCourtTrialOutcome crownCourtTrialOutcome;

    public static boolean isConvicted(String verdictValue) {
        return
                Stream
                .of(VerdictTrialOutcome.values())
                .filter(f -> f.crownCourtTrialOutcome.equals(CrownCourtTrialOutcome.CONVICTED))
                .anyMatch(v -> v.name().equalsIgnoreCase(verdictValue));
    }

    public static String getTrialOutcome(String verdictValue) {

        Optional<VerdictTrialOutcome> trialOutcomeOptional = Stream
                .of(VerdictTrialOutcome.values())
                .filter(pl -> pl.name().equalsIgnoreCase(verdictValue))
                .findFirst();

        return trialOutcomeOptional.isPresent() ? trialOutcomeOptional.get().crownCourtTrialOutcome.getValue() : CrownCourtTrialOutcome.AQUITTED.getValue();

    }
}
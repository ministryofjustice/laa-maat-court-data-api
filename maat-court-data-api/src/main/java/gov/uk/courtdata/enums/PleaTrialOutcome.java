package gov.uk.courtdata.enums;

import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
public enum PleaTrialOutcome {

    GUILTY_LESSER_OFFENCE_NAMELY(CrownCourtTrialOutcome.CONVICTED),
    GUILTY(CrownCourtTrialOutcome.CONVICTED),
    GUILTY_TO_ALTERNATIVE_OFFENCE(CrownCourtTrialOutcome.CONVICTED),
    CHANGE_TO_GUILTY_AFTER_SWORN(CrownCourtTrialOutcome.CONVICTED),
    CHANGE_TO_GUILTY_NO_JURY(CrownCourtTrialOutcome.CONVICTED),
    NO_PLEA_CROWN(CrownCourtTrialOutcome.AQUITTED),
    NOT_GUILTY(CrownCourtTrialOutcome.AQUITTED),
    CHANGE_TO_NOT_GUILTY(CrownCourtTrialOutcome.AQUITTED),
    ADMITS_CROWN(CrownCourtTrialOutcome.AQUITTED),
    DENIES_CROWN(CrownCourtTrialOutcome.AQUITTED),
    AUTREFOIS_ACQUIT(CrownCourtTrialOutcome.AQUITTED),
    AUTREFOIS_CONVICT(CrownCourtTrialOutcome.AQUITTED),
    PARDON(CrownCourtTrialOutcome.AQUITTED);

    private CrownCourtTrialOutcome crownCourtTrialOutcome;

    public static boolean isConvicted(String pleaValue) {
        return
                Stream
                .of(PleaTrialOutcome.values())
                .filter(f -> f.crownCourtTrialOutcome.equals(CrownCourtTrialOutcome.CONVICTED))
                .anyMatch(v -> v.name().equalsIgnoreCase(pleaValue));
    }

    public static String getTrialOutcome(String pleaValue) {

        Optional<PleaTrialOutcome> trialOutcomeOptional = Stream
                .of(PleaTrialOutcome.values())
                .filter(pl -> pl.name().equalsIgnoreCase(pleaValue))
                .findFirst();

        return trialOutcomeOptional.isPresent() ? trialOutcomeOptional.get().crownCourtTrialOutcome.name() : CrownCourtTrialOutcome.AQUITTED.name();
    }
}
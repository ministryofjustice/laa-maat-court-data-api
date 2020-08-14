package gov.uk.courtdata.enums;

import gov.uk.courtdata.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;


@AllArgsConstructor
@Getter
public enum CrownCourtTrialOutcome {

    CONVICTED("CONVICTED"),
    PART_CONVICTED("PART CONVICTED"),
    AQUITTED("AQUITTED");

    private String value;

    public static boolean isConvicted(String outcome) {

        return Stream.of(CONVICTED, PART_CONVICTED)
                .anyMatch(trOut -> trOut.getValue().equalsIgnoreCase(notEmpty(outcome)));
    }


    public static boolean isTrial(String outcome) {

        return Stream.of(CrownCourtTrialOutcome.values())
                .anyMatch(trOut -> trOut.getValue().equalsIgnoreCase(notEmpty(outcome)));
    }

    private static String notEmpty(String outcome) {

        return Optional.ofNullable(outcome).orElseThrow(
                () -> new ValidationException("Crown Court trial outcome can't be empty."));
    }

}

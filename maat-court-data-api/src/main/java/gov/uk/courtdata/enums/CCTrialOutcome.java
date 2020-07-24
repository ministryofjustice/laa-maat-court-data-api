package gov.uk.courtdata.enums;

import gov.uk.courtdata.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;


@AllArgsConstructor
@Getter
public enum CCTrialOutcome {

    CONVICTED("CONVICTED"),
    PART_CONVICTED("PART CONVICTED");

    private String value;

    public static boolean isConvicted(String outcome) {

        String out = Optional.ofNullable(outcome).orElseThrow(
                () -> new ValidationException("Crown Court outcome can't be empty."));

        return Stream.of(CONVICTED.getValue(), PART_CONVICTED.getValue())
                .anyMatch(o -> o.equals(out));
    }


}

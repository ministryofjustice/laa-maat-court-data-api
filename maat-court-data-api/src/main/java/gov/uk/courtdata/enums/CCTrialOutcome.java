package gov.uk.courtdata.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;


@AllArgsConstructor
@Getter
public enum CCTrialOutcome {

    CONVICTED("CONVICTED"),
    PART_CONVICTED("PART CONVICTED");

    private String value;

    public static boolean isConvicted(String outcome) {

        String out = Optional.ofNullable(outcome).orElseThrow(
                () -> new IllegalArgumentException("Crown Court outcome can't be empty."));

        return Arrays.asList(CONVICTED.getValue(), PART_CONVICTED.getValue())
                .stream().anyMatch(o -> o.equals(out));
    }


}

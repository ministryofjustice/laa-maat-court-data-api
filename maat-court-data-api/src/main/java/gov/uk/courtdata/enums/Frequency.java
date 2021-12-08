package gov.uk.courtdata.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Frequency {
    WEEKLY("WEEKLY"),
    TWO_WEEKLY("2WEEKLY"),
    FOUR_WEEKLY("4WEEKLY"),
    MONTHLY("MONTHLY"),
    ANNUALLY("ANNUALLY");

    private String code;
}

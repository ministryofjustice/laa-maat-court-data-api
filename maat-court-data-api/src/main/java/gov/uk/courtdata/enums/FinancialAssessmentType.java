package gov.uk.courtdata.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FinancialAssessmentType {

    INIT("INIT"),
    FULL("FULL");

    private String value;

}

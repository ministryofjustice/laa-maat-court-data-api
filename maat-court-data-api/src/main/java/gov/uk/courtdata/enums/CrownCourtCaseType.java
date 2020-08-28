package gov.uk.courtdata.enums;

import gov.uk.courtdata.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum CrownCourtCaseType {

    INDICTABLE("INDICTABLE"),
    SUMMARY_ONLY("SUMMARY ONLY"),
    CC_ALREADY("CC ALREADY"),
    EITHER_WAY("EITHER WAY"),
    APPEAL_CC("APPEAL CC");

    private String value;


    public static boolean caseTypeForTrial(final String caseType) {

        return Stream.of(INDICTABLE, EITHER_WAY, CC_ALREADY)
                .anyMatch(csType -> csType.getValue().equalsIgnoreCase(notEmpty(caseType)));
    }

    public static boolean caseTypeForAppeal(final String caseType) {
        return APPEAL_CC.getValue().equalsIgnoreCase(notEmpty(caseType));
    }


    private static String notEmpty(String caseType) {
        return Optional.ofNullable(caseType).orElseThrow(
                () -> new ValidationException("Case type can't be empty."));
    }
}

package gov.uk.courtdata.enums;

import gov.uk.courtdata.exception.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static gov.uk.courtdata.enums.CrownCourtCaseType.*;
import static org.junit.jupiter.api.Assertions.*;

public class CrownCourtCaseTypeTest {

    @Test
    public void givenCaseTypeForTrialIsEmpty_ExceptionThrown() {

        Assertions.assertThrows(ValidationException.class, () -> {
            caseTypeForTrial(null);
        });
    }

    @Test
    public void givenCaseTypeForTrialIsIndictable_ReturnsTrue() {

        assertAll("CaseTypeForTrial",
                () -> assertTrue(caseTypeForTrial(INDICTABLE.getValue())));
    }

    @Test
    public void givenCaseTypeForTrialIsEitherWayOnly_ReturnsTrue() {

        assertAll("CaseTypeForTrial",
                () -> assertTrue(caseTypeForTrial(EITHER_WAY.getValue())));
    }

    @Test
    public void givenCaseTypeForTrialIsCCAlready_ReturnsTrue() {

        assertAll("CaseTypeForTrial",
                () -> assertTrue(caseTypeForTrial(CC_ALREADY.getValue())));
    }

    @Test
    public void givenCaseTypeForTrialNotValid_ReturnsFalse() {

        assertAll("CaseTypeForTrial",
                () -> assertFalse(caseTypeForTrial(APPEAL_CC.getValue())));
    }

    @Test
    public void givenCaseTypeForAppealIsEmpty_ExceptionThrown() {

        Assertions.assertThrows(ValidationException.class, () -> {
            caseTypeForAppeal(null);
        });
    }


    @Test
    public void givenCaseTypeForAppealIsAppealCC_ReturnsTrue() {

        assertAll("CaseTypeForAppeal",
                () -> assertTrue(caseTypeForAppeal(APPEAL_CC.getValue())));
    }


    @Test
    public void givenCaseTypeNotForAppeal_ReturnsFalse() {

        assertAll("CaseTypeForAppeal",
                () -> assertFalse(caseTypeForAppeal(EITHER_WAY.getValue())));
    }
}

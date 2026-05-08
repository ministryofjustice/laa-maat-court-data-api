package gov.uk.courtdata.enums;

import static gov.uk.courtdata.enums.CrownCourtAppealOutcome.PART_SUCCESS;
import static gov.uk.courtdata.enums.CrownCourtAppealOutcome.SUCCESSFUL;
import static gov.uk.courtdata.enums.CrownCourtAppealOutcome.UNSUCCESSFUL;
import static gov.uk.courtdata.enums.CrownCourtAppealOutcome.isAppeal;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.uk.courtdata.exception.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CrownCourtAppealOutcomeTest {

    @Test
    public void givenOutcomeIsEmpty_ExceptionThrown() {

        Assertions.assertThrows(ValidationException.class, () -> {
            isAppeal(null);
        });
    }

    @Test
    public void givenOutComeIsSuccessful_ReturnsTrue() {

        assertAll("AppealOutcome", () -> assertTrue(isAppeal(SUCCESSFUL.getValue())));
    }

    @Test
    public void givenOutComeIsPartSuccess_ReturnsTrue() {

        assertAll("AppealOutcome", () -> assertTrue(isAppeal(PART_SUCCESS.getValue())));
    }

    @Test
    public void givenOutComeIsUnSuccessful_ReturnsTrue() {

        assertAll("AppealOutcome", () -> assertTrue(isAppeal(UNSUCCESSFUL.getValue())));
    }

    @Test
    public void givenOutComeIsNotForAppeal_ReturnsFalse() {

        assertAll("AppealOutcome", () -> assertFalse(isAppeal("ACQUITTED")));
    }
}

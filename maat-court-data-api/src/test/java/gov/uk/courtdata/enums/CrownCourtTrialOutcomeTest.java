package gov.uk.courtdata.enums;

import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.CONVICTED;
import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.PART_CONVICTED;
import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.isConvicted;
import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.isTrial;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.uk.courtdata.exception.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CrownCourtTrialOutcomeTest {

    @Test
    public void givenOutcomeIsEmpty_ExceptionThrown() {

        Assertions.assertThrows(ValidationException.class, () -> {
            isConvicted(null);
        });
    }

    @Test
    public void givenOutComeIsConvicted_ReturnsTrue() {

        assertAll("TrialOutcome", () -> assertTrue(isConvicted(CONVICTED.getValue())));
    }

    @Test
    public void givenOutComeIsPartConvicted_ReturnsTrue() {

        assertAll("TrialOutcome", () -> assertTrue(isConvicted(PART_CONVICTED.getValue())));
    }

    @Test
    public void givenOutComeIsNotConvicted_ReturnsFalse() {

        assertAll("TrialOutcome", () -> assertFalse(isConvicted("ACQUITTED")));
    }

    @Test
    public void givenOutcomeIsEmptyForTrial_ExceptionThrown() {

        Assertions.assertThrows(ValidationException.class, () -> {
            isTrial(null);
        });
    }

    @Test
    public void givenOutComeIsForTrial_ReturnsTrue() {

        assertAll("TrialOutcome", () -> assertTrue(isTrial(CONVICTED.getValue())));
    }

    @Test
    public void givenOutComeIsNotTrial_ReturnsFalse() {

        assertAll("TrialOutcome", () -> assertFalse(isTrial("INVALID")));
    }
}

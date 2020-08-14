package gov.uk.courtdata.enums;

import gov.uk.courtdata.exception.ValidationException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.*;
import static org.junit.jupiter.api.Assertions.*;

public class CrownCourtTrialOutcomeTest {

    @Test
    public void givenOutcomeIsEmpty_ExceptionThrown() {

        Assertions.assertThrows(ValidationException.class, () -> {
            isConvicted(null);
        });
    }

    @Test
    public void givenOutComeIsConvicted_ReturnsTrue() {

        assertAll("TrialOutcome",
                () -> assertTrue(isConvicted(CONVICTED.getValue())));
    }

    @Test
    public void givenOutComeIsPartConvicted_ReturnsTrue() {

        assertAll("TrialOutcome",
                () -> assertTrue(isConvicted(PART_CONVICTED.getValue())));
    }

    @Test
    public void givenOutComeIsNotConvicted_ReturnsFalse() {

        assertAll("TrialOutcome",
                () -> assertFalse(isConvicted("ACQUITTED")));
    }

    @Test
    public void givenOutcomeIsEmptyForTrial_ExceptionThrown() {

        Assertions.assertThrows(ValidationException.class, () -> {
            isTrial(null);
        });
    }

    @Test
    public void givenOutComeIsForTrial_ReturnsTrue() {

        assertAll("TrialOutcome",
                () -> assertTrue(isTrial(CONVICTED.getValue())));
    }


    @Test
    public void givenOutComeIsNotTrial_ReturnsFalse() {

        assertAll("TrialOutcome",
                () -> assertFalse(isTrial("INVALID")));
    }
}

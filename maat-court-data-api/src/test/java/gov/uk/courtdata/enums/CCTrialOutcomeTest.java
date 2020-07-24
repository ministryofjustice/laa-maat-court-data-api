package gov.uk.courtdata.enums;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static gov.uk.courtdata.enums.CCTrialOutcome.*;
import static org.junit.jupiter.api.Assertions.*;

public class CCTrialOutcomeTest {

    @Test
    public void givenOutcomeIsEmpty_ExceptionThrown() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
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
}

package gov.uk.courtdata.enums;

import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.CONVICTED;
import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.PART_CONVICTED;
import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.isConvicted;
import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.isTrial;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gov.uk.courtdata.exception.ValidationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CrownCourtTrialOutcomeTest {

    @Test
    void givenOutcomeIsEmpty_ExceptionThrown() {
        assertThatThrownBy(() -> isConvicted(null)).isInstanceOf(ValidationException.class);
    }

    @Test
    void givenOutcomeIsConvicted_ReturnsTrue() {
        assertThat(isConvicted(CONVICTED.getValue())).isTrue();
    }

    @Test
    void givenOutcomeIsPartConvicted_ReturnsTrue() {
        assertThat(isConvicted(PART_CONVICTED.getValue())).isTrue();
    }

    @Test
    void givenOutcomeIsNotConvicted_ReturnsFalse() {
        assertThat(isConvicted("ACQUITTED")).isFalse();
    }

    @Test
    void givenOutcomeIsEmptyForTrial_ExceptionThrown() {
        assertThatThrownBy(() -> isTrial(null)).isInstanceOf(ValidationException.class);
    }

    @ParameterizedTest
    @EnumSource(value = CrownCourtTrialOutcome.class)
    void givenOutcomeIsForTrial_ReturnsTrue(CrownCourtTrialOutcome outcome) {
        assertThat(isTrial(outcome.getValue())).isTrue();
    }

    @Test
    void givenOutcomeIsInvalid_ReturnsFalse() {
        assertThat(isTrial("INVALID")).isFalse();
    }
}

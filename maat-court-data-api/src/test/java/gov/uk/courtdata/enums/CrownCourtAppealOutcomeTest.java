package gov.uk.courtdata.enums;

import static gov.uk.courtdata.enums.CrownCourtAppealOutcome.isAppeal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gov.uk.courtdata.exception.ValidationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CrownCourtAppealOutcomeTest {

    @Test
    void givenOutcomeIsEmpty_ExceptionThrown() {
        assertThatThrownBy(() -> isAppeal(null)).isInstanceOf(ValidationException.class);
    }

    @EnumSource(value = CrownCourtAppealOutcome.class)
    @ParameterizedTest
    void givenOutcomeIsValid_ReturnsTrue(CrownCourtAppealOutcome outcome) {
        assertThat(isAppeal(outcome.getValue())).isTrue();
    }

    @Test
    void givenOutcomeIsNotValid_ReturnsFalse() {
        assertThat(isAppeal("ACQUITTED")).isFalse();
        assertThat(isAppeal("")).isFalse();
    }
}

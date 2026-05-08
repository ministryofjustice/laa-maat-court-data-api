package gov.uk.courtdata.enums;

import static gov.uk.courtdata.enums.CrownCourtCaseType.APPEAL_CC;
import static gov.uk.courtdata.enums.CrownCourtCaseType.CC_ALREADY;
import static gov.uk.courtdata.enums.CrownCourtCaseType.EITHER_WAY;
import static gov.uk.courtdata.enums.CrownCourtCaseType.INDICTABLE;
import static gov.uk.courtdata.enums.CrownCourtCaseType.caseTypeForAppeal;
import static gov.uk.courtdata.enums.CrownCourtCaseType.caseTypeForTrial;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gov.uk.courtdata.exception.ValidationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class CrownCourtCaseTypeTest {

    @Test
    void givenCaseTypeForTrialIsEmpty_ExceptionThrown() {
        assertThatThrownBy(() -> caseTypeForTrial(null)).isInstanceOf(ValidationException.class);
    }

    @Test
    void givenCaseTypeForTrialIsIndictable_ReturnsTrue() {
        assertThat(caseTypeForTrial(INDICTABLE.getValue())).isTrue();
    }

    @Test
    void givenCaseTypeForTrialIsEitherWayOnly_ReturnsTrue() {
        assertThat(caseTypeForTrial(EITHER_WAY.getValue())).isTrue();
    }

    @Test
    void givenCaseTypeForTrialIsCCAlready_ReturnsTrue() {
        assertThat(caseTypeForTrial(CC_ALREADY.getValue())).isTrue();
    }

    @Test
    void givenCaseTypeForTrialNotValid_ReturnsFalse() {
        assertThat(caseTypeForTrial(APPEAL_CC.getValue())).isFalse();
    }

    @Test
    void givenCaseTypeForAppealIsEmpty_ExceptionThrown() {
        assertThatThrownBy(() -> caseTypeForAppeal(null)).isInstanceOf(ValidationException.class);
    }

    @Test
    void givenCaseTypeForAppealIsAppealCC_ReturnsTrue() {
        assertThat(caseTypeForAppeal(APPEAL_CC.getValue())).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = CrownCourtCaseType.class, names = "APPEAL_CC", mode = EnumSource.Mode.EXCLUDE)
    void givenCaseTypeNotForAppeal_ReturnsFalse(CrownCourtCaseType caseType) {
        assertThat(caseTypeForAppeal(caseType.getValue())).isFalse();
    }
}

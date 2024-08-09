package gov.uk.courtdata.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class HardshipReviewDetailTypeTest {

    @Test
    void givenValidTypeString_whenGetFromIsInvoked_thenCorrectEnumValueReturned() {
        assertThat(HardshipReviewDetailType.getFrom("SoL COSTS")).isEqualTo(HardshipReviewDetailType.SOL_COSTS);
    }

    @Test
    void givenInvalidTypeString_whenGetFromIsInvoked_thenExceptionIsThrown() {
        assertThatThrownBy(() -> HardshipReviewDetailType.getFrom("DOUGHNUTS")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenBlankTypeString_whenGetFromIsInvoked_thenNullIsReturned() {
        assertThat(HardshipReviewDetailType.getFrom("")).isNull();
    }

    @Test
    void givenValidInput_thenValidEnumsValuesReturned() {
        assertThat(HardshipReviewDetailType.EXPENDITURE.getValue()).isEqualTo("EXPENDITURE");
        assertThat(HardshipReviewDetailType.EXPENDITURE.getType()).isEqualTo("EXPENDITURE");
        assertThat(HardshipReviewDetailType.EXPENDITURE.getDescription()).isEqualTo("Extra Expenditure");
    }
}

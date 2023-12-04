package gov.uk.courtdata.enums;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PleaTrialOutcomeTest {

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of ("GUILTY_LESSER_OFFENCE_NAMELY", "CONVICTED", true),
                Arguments.of ("GUILTY", "CONVICTED", true),
                Arguments.of ("GUILTY_TO_ALTERNATIVE_OFFENCE", "CONVICTED", true),
                Arguments.of ("CHANGE_TO_GUILTY_AFTER_SWORN", "CONVICTED", true),
                Arguments.of ("CHANGE_TO_GUILTY_NO JURY", "CONVICTED", true),
                Arguments.of ("NO_PLEA_CROWN", "AQUITTED", false),
                Arguments.of ("NOT_GUILTY", "AQUITTED", false),
                Arguments.of ("CHANGE_TO_NOT_GUILTY", "AQUITTED", false),
                Arguments.of ("ADMITS_CROWN", "AQUITTED", false),
                Arguments.of ("DENIES_CROWN", "AQUITTED", false),
                Arguments.of ("AUTREFOIS_ACQUIT", "AQUITTED", false),
                Arguments.of ("AUTREFOIS_CONVICT", "AQUITTED", false),
                Arguments.of ("PARDON", "AQUITTED", false),
                Arguments.of ("UNKNOWN", "AQUITTED", false)
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void givenAPleaValue_theCorrectIsConvictedBooleanIsReturned(String pleaValue, String outcome, Boolean isConvicted) {
        assertEquals(isConvicted, PleaTrialOutcome.isConvicted(pleaValue));
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void givenAPleaValue_theCorrectTrialOutcomeIsReturned(String pleaValue, String outcome, Boolean isConvicted) {
        assertEquals(outcome, PleaTrialOutcome.getTrialOutcome(pleaValue));
    }
}
package gov.uk.courtdata.enums;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class PleaTrialOutcomeTest {

    private final Boolean isConvicted;
    private final String outcome;
    private final String pleaValue;

    @Parameterized.Parameters
    public static Collection<Object[]> testData() {
        return Arrays.asList(new Object[][] {
                {"GUILTY_LESSER_OFFENCE_NAMELY", "CONVICTED", true},
                {"GUILTY", "CONVICTED", true},
                {"GUILTY_TO_ALTERNATIVE_OFFENCE", "CONVICTED", true},
                {"CHANGE_TO_GUILTY_AFTER_SWORN", "CONVICTED", true},
                {"CHANGE_TO_GUILTY_NO JURY", "CONVICTED", true},
                {"NO_PLEA_CROWN", "AQUITTED", false},
                {"NOT_GUILTY", "AQUITTED", false},
                {"CHANGE_TO_NOT_GUILTY", "AQUITTED", false},
                {"ADMITS_CROWN", "AQUITTED", false},
                {"DENIES_CROWN", "AQUITTED", false},
                {"AUTREFOIS_ACQUIT", "AQUITTED", false},
                {"AUTREFOIS_CONVICT", "AQUITTED", false},
                {"PARDON", "AQUITTED", false},
                {"UNKNOWN", "AQUITTED", false},
        });
    }


    public PleaTrialOutcomeTest(String pleaValue, String outcome, Boolean isConvicted) {
        this.isConvicted = isConvicted;
        this.outcome = outcome;
        this.pleaValue = pleaValue;
    }

    @Test
    public void givenAPleaValue_theCorrectIsConvictedBooleanIsReturned() {
        assertEquals(this.isConvicted, PleaTrialOutcome.isConvicted(this.pleaValue));
    }

    @Test
    public void givenAPleaValue_theCorrectTrialOutcomeIsReturned() {
        assertEquals(this.outcome, PleaTrialOutcome.getTrialOutcome(this.pleaValue));
    }
}
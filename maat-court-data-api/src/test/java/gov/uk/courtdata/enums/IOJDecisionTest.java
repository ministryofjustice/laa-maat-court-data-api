package gov.uk.courtdata.enums;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class IOJDecisionTest {

    @Test
    public void givenFailIOJDecision_failResultIsReturned() {

        boolean result = IOJDecision.isFailedDecision(0);
        assertThat(result).isTrue();
    }
    @Test
    public void givenPendingIOJDecision_failResultIsReturned() {

        boolean result = IOJDecision.isFailedDecision(2);
        assertThat(result).isTrue();
    }
    @Test
    public void givenGrantedIOJDecision_grantedResultIsReturned() {

        boolean result = IOJDecision.isFailedDecision(1);
        assertThat(result).isFalse();
    }
}

package gov.uk.courtdata.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class IOJDecisionTest {

    @Test
    void givenFailIOJDecision_failResultIsReturned() {

        boolean result = IOJDecision.isFailedDecision(0);
        assertThat(result).isTrue();
    }

    @Test
    void givenPendingIOJDecision_failResultIsReturned() {

        boolean result = IOJDecision.isFailedDecision(2);
        assertThat(result).isTrue();
    }

    @Test
    void givenGrantedIOJDecision_grantedResultIsReturned() {

        boolean result = IOJDecision.isFailedDecision(1);
        assertThat(result).isFalse();
    }
}

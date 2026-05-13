package gov.uk.courtdata.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class WQTypeTest {

    @Test
    void givenCommittalWQ_actionableResultIsReturned() {

        boolean result = WQType.isActionableQueue(1);
        assertThat(result).isFalse();
    }

    @Test
    void givenIndictableWQ_actionableResultIsReturned() {

        boolean result = WQType.isActionableQueue(2);
        assertThat(result).isTrue();
    }

    @Test
    void givenConclusionsWQ_actionableResultIsReturned() {

        boolean result = WQType.isActionableQueue(7);
        assertThat(result).isTrue();
    }

    @Test
    void givenNonActionableWQ_NonActionableResultIsReturned() {

        boolean result = WQType.isActionableQueue(5);
        assertThat(result).isFalse();
    }
}

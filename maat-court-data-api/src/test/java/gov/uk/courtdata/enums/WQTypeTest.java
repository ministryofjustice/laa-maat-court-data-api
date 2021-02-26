package gov.uk.courtdata.enums;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WQTypeTest {

    @Test
    public void givenCommittalWQ_actionableResultIsReturned() {

        boolean result = WQType.isActionableQueue(1);
        assertThat(result).isTrue();
    }
    @Test
    public void givenIndictableWQ_actionableResultIsReturned() {

        boolean result = WQType.isActionableQueue(2);
        assertThat(result).isTrue();
    }
    @Test
    public void givenConclusionsWQ_actionableResultIsReturned() {

        boolean result = WQType.isActionableQueue(7);
        assertThat(result).isTrue();
    }

    @Test
    public void givenNonActionableWQ_NonActionableResultIsReturned() {

        boolean result = WQType.isActionableQueue(5);
        assertThat(result).isFalse();
    }
}

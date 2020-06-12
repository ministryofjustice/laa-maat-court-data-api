package gov.uk.courtdata.enums;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WQTypeTest {

    @Test
    public void givenActionableWQ_actionableResultIsReturned() {

        boolean result = WQType.isActionableQueue(1);
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void givenNonActionableWQ_NonActionableResultIsReturned() {

        boolean result = WQType.isActionableQueue(5);
        assertThat(result).isEqualTo(false);
    }
}

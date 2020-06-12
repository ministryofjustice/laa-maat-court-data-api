package gov.uk.courtdata.enums;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class LAAStatusTest {


    @Test
    public void givenFailLAAStatus_failResultIsReturned() {

        boolean result = LAAStatus.isFailedLAAStatus("FB");
        assertThat(result).isEqualTo(true);
    }

    @Test
    public void givenGrantedLAAStatus_grantedResultIsReturned() {

        boolean result = LAAStatus.isFailedLAAStatus("GR");
        assertThat(result).isEqualTo(false);
    }
}

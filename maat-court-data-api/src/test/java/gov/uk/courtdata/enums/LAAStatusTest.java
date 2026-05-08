package gov.uk.courtdata.enums;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

public class LAAStatusTest {

    @Test
    public void givenFBLAAStatus_failResultIsReturned() {

        boolean result = LAAStatus.isFailedLAAStatus("FB");
        assertThat(result).isTrue();
    }

    @Test
    public void givenFJLAAStatus_grantedResultIsReturned() {

        boolean result = LAAStatus.isFailedLAAStatus("FJ");
        assertThat(result).isTrue();
    }

    @Test
    public void givenGRLAAStatus_grantedResultIsReturned() {

        boolean result = LAAStatus.isGrantedLAAStatus("GR");
        assertThat(result).isTrue();
    }

    @Test
    public void givenG2LAAStatus_grantedResultIsReturned() {

        boolean result = LAAStatus.isGrantedLAAStatus("G2");
        assertThat(result).isTrue();
    }

    @Test
    public void givenGQLAAStatus_grantedResultIsReturned() {

        boolean result = LAAStatus.isGrantedLAAStatus("GQ");
        assertThat(result).isTrue();
    }
}

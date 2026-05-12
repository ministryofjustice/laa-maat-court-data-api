package gov.uk.courtdata.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LAAStatusTest {

    @Test
    void givenFBLAAStatus_failResultIsReturned() {

        boolean result = LAAStatus.isFailedLAAStatus("FB");
        assertThat(result).isTrue();
    }

    @Test
    void givenFJLAAStatus_grantedResultIsReturned() {

        boolean result = LAAStatus.isFailedLAAStatus("FJ");
        assertThat(result).isTrue();
    }

    @Test
    void givenGRLAAStatus_grantedResultIsReturned() {

        boolean result = LAAStatus.isGrantedLAAStatus("GR");
        assertThat(result).isTrue();
    }

    @Test
    void givenG2LAAStatus_grantedResultIsReturned() {

        boolean result = LAAStatus.isGrantedLAAStatus("G2");
        assertThat(result).isTrue();
    }

    @Test
    void givenGQLAAStatus_grantedResultIsReturned() {

        boolean result = LAAStatus.isGrantedLAAStatus("GQ");
        assertThat(result).isTrue();
    }
}

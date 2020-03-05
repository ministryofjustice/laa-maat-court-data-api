package gov.uk.courtdata.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CourtDataUtilTest {

    private CourtDataUtil courtDataUtil = new CourtDataUtil();

    @Test
    public void givenStringFormatDate_whenGetDateIsInvoked_thenLocalDateIsReturned() {

        LocalDate localDate = courtDataUtil.getDate("2020-02-02");
        assertThat(localDate).isEqualTo(LocalDate.of(2020, 2, 2));
    }
}

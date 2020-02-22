package gov.uk.courtdata.util;

import gov.uk.MAATCourtDataApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MAATCourtDataApplication.class)
public class CourtDataUtilTest {
    @Autowired
    private CourtDataUtil courtDataUtil;

    @Test
    public void givenStringFormatDate_whenGetDateIsInvoked_thenLocalDateIsReturned() {

        LocalDate localDate = courtDataUtil.getDate("2020-02-02");
        assertThat(localDate).isEqualTo(LocalDate.of(2020, 2, 2));
    }
}

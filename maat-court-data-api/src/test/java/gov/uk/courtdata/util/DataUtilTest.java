package gov.uk.courtdata.util;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class DataUtilTest {


    @Test
    public void givenStringFormatDate_whenGetDateIsInvoked_thenLocalDateIsReturned() {

        LocalDate localDate = DateUtil.parse("2020-02-02");
        assertThat(localDate).isEqualTo(LocalDate.of(2020, 2, 2));
    }

    @Test
    public void givenDateIsNull_thenReturnsNull() {
        assertNull(DateUtil.parse(null));
    }
}


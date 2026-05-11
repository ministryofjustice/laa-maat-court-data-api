package gov.uk.courtdata.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataUtilTest {

    @Test
    void givenStringFormatDate_whenGetDateIsInvoked_thenLocalDateIsReturned() {

        LocalDate localDate = DateUtil.parse("2020-02-02");
        assertThat(localDate).isEqualTo(LocalDate.of(2020, 2, 2));
    }

    @Test
    void givenDateIsNull_thenReturnsNull() {
        assertThat(DateUtil.parse(null)).isNull();
    }
}

package gov.uk.courtdata.billing.repository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

class ApplicantCclfResetRepositoryTest {

    @Test
    void shouldExecuteStoredProcedureSuccessfully() {
        NamedParameterJdbcTemplate mockJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        ApplicantCclfResetRepository repository = new ApplicantCclfResetRepository(mockJdbcTemplate);

        when(mockJdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        assertThatCode(repository::resetApplicantCclfFlag).doesNotThrowAnyException();

        verify(mockJdbcTemplate).update(anyString(), anyMap());
    }

    @Test
    void shouldThrowException_whenJdbcTemplateFails() {
        NamedParameterJdbcTemplate mockJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        ApplicantCclfResetRepository repository = new ApplicantCclfResetRepository(mockJdbcTemplate);

        doThrow(new RuntimeException("DB Error"))
                .when(mockJdbcTemplate).update(anyString(), anyMap());

        assertThatThrownBy(repository::resetApplicantCclfFlag)
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB Error");

        verify(mockJdbcTemplate).update(anyString(), anyMap());
    }
}
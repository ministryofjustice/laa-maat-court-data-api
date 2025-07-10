package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.repository.ApplicantCclfResetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApplicantCclfResetServiceTest {

    @Mock
    private ApplicantCclfResetRepository repository;

    @InjectMocks
    private ApplicantCclfResetService service;

    @Test
    void shouldCallRepositorySuccessfully() {
        doNothing().when(repository).resetApplicantCclfFlag();

        service.resetApplicantCclfFlag();

        verify(repository).resetApplicantCclfFlag();
    }

    @Test
    void shouldThrowException_whenRepositoryFails() {
        doThrow(new RuntimeException("Repository Error"))
                .when(repository).resetApplicantCclfFlag();

        assertThatThrownBy(() -> service.resetApplicantCclfFlag())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Repository Error");
    }
}
package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.request.ApplicantResetRequest;
import gov.uk.courtdata.billing.repository.ApplicantResetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ApplicantResetServiceTest {

    @Mock
    private ApplicantResetRepository repository;

    @InjectMocks
    private ApplicantResetService service;

    @Test
    void shouldResetCclfFlagSuccessfully() {
        ApplicantResetRequest request = new ApplicantResetRequest();
        request.setUsername("test_user");
        request.setApplicantIds(List.of(1, 2, 3));

        service.resetApplicant(request);

        verify(repository).resetApplicant(request.getApplicantIds(), request.getUsername());
    }
}
package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.billing.dto.ApplicantHistoryBillingDTO;
import gov.uk.courtdata.billing.entity.ApplicantHistoryBillingEntity;
import gov.uk.courtdata.billing.mapper.ApplicantHistoryBillingMapper;
import gov.uk.courtdata.billing.repository.ApplicantHistoryBillingRepository;
import gov.uk.courtdata.billing.request.UpdateBillingRequest;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.MAATCourtDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicantHistoryBillingServiceTest {

    @Mock
    private ApplicantHistoryBillingRepository repository;
    @Mock
    private ApplicantHistoryBillingMapper mapper;
    @InjectMocks
    private ApplicantHistoryBillingService service;

    @Test
    void givenApplicantHistoriesToExtract_whenExtractApplicantHistoryBillingIsInvoked_thenApplicantHistoriesReturned() {
        when(repository.extractApplicantHistoryBilling())
                .thenReturn(List.of(TestEntityDataBuilder.getApplicantHistoryBillingEntity()));
        when(mapper.mapEntityToDTO(any(ApplicantHistoryBillingEntity.class)))
                .thenReturn(TestModelDataBuilder.getApplicantHistoryBillingDTO());

        List<ApplicantHistoryBillingDTO> dtos = service.extractApplicantHistory();

        assertEquals(List.of(TestModelDataBuilder.getApplicantHistoryBillingDTO()), dtos);
    }

    @Test
    void givenNoApplicantHistoriesToExtract_whenExtractApplicantHistoryBillingIsInvoked_thenEmptyListReturned() {
        when(repository.extractApplicantHistoryBilling()).thenReturn(new ArrayList<>());

        List<ApplicantHistoryBillingDTO> dtos = service.extractApplicantHistory();

        assertTrue(dtos.isEmpty(), "Applicant history billing data returned when none expected.");
    }

    @Test
    void givenValidRequestData_whenResetApplicantHistoryIsInvoked_thenApplicantHistoriesAreReset() {
        UpdateBillingRequest request = TestModelDataBuilder.getUpdateBillingRequest();
        when(repository.resetApplicantHistoryBilling(anyString(), anyList())).thenReturn(request.getIds().size());

        service.resetApplicantHistory(request);

        verify(repository).resetApplicantHistoryBilling(request.getUserModified(), request.getIds());
    }

    @Test
    void givenNotAllRowsUpdated_whenResetApplicantHistoryIsInvoked_thenExceptionIsRaised() {
        UpdateBillingRequest request = TestModelDataBuilder.getUpdateBillingRequest();
        when(repository.resetApplicantHistoryBilling(anyString(), anyList())).thenReturn(1);

        assertThatThrownBy(() -> {
            service.resetApplicantHistory(request);
        }).isInstanceOf(MAATCourtDataException.class)
                .hasMessageContaining(String.format(
                        "Number of applicant histories reset: %d, does not equal those supplied in request: %d.",
                        1, request.getIds().size()));
    }
}

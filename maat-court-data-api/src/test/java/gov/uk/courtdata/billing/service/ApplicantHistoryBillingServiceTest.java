package gov.uk.courtdata.billing.service;

import gov.uk.courtdata.applicant.entity.ApplicantHistoryEntity;
import gov.uk.courtdata.applicant.repository.ApplicantHistoryRepository;
import gov.uk.courtdata.billing.dto.ApplicantHistoryBillingDTO;
import gov.uk.courtdata.billing.mapper.ApplicantHistoryBillingMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ApplicantHistoryBillingServiceTest {

    @Mock
    private ApplicantHistoryRepository repository;
    @Mock
    private ApplicantHistoryBillingMapper mapper;
    @InjectMocks
    private ApplicantHistoryBillingService service;

    @Test
    void givenApplicantHistoriesToExtract_whenExtractApplicantHistoryBillingIsInvoked_thenApplicantHistoriesReturned() {
        when(repository.extractApplicantHistoryBilling())
                .thenReturn(List.of(TestEntityDataBuilder.getApplicantHistoryEntity("Y")));
        when(mapper.mapEntityToDTO(any(ApplicantHistoryEntity.class)))
                .thenReturn(TestModelDataBuilder.getApplicantHistoryBillingDTO());

        List<ApplicantHistoryBillingDTO> DTOs = service.extractApplicantHistory();

        assertEquals(List.of(TestModelDataBuilder.getApplicantHistoryBillingDTO()), DTOs);
    }

    @Test
    void givenNoApplicantHistoriesToExtract_whenExtractApplicantHistoryBillingIsInvoked_thenExceptionRaised() {
        when(repository.extractApplicantHistoryBilling()).thenReturn(new ArrayList<>());

        assertThatThrownBy(() -> {
            service.extractApplicantHistory();
        }).isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("No applicant histories retrieved based on MAAT_REFS_TO_EXTRACT table.");
    }
}

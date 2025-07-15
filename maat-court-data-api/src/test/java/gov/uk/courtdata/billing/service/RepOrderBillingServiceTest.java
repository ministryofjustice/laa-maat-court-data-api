package gov.uk.courtdata.billing.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.billing.dto.RepOrderBillingDTO;
import gov.uk.courtdata.billing.request.UpdateBillingRequest;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.billing.repository.RepOrderBillingRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RepOrderBillingServiceTest {

    @Mock
    private RepOrderBillingRepository repOrderBillingRepository;

    @InjectMocks
    private RepOrderBillingService repOrderBillingService;

    @Test
    void givenNoRepOrders_whenGetRepOrdersForBillingIsInvoked_thenEmptyListIsReturned() {
        when(repOrderBillingRepository.getRepOrdersForBilling()).thenReturn(Collections.emptyList());

        List<RepOrderBillingDTO> repOrders = repOrderBillingService.getRepOrdersForBilling();

        assertEquals(Collections.emptyList(), repOrders);
    }

    @Test
    void givenRepOrdersExist_whenGetRepOrdersForBillingIsInvoked_thenRepOrdersAreReturned() {
        when(repOrderBillingRepository.getRepOrdersForBilling()).thenReturn(List.of(
            TestEntityDataBuilder.getPopulatedRepOrderForBilling(123),
            TestEntityDataBuilder.getPopulatedRepOrderForBilling(124)
        ));

        List<RepOrderBillingDTO> expectedRepOrders = List.of(
            TestModelDataBuilder.getRepOrderBillingDTO(123),
            TestModelDataBuilder.getRepOrderBillingDTO(124)
        );

        List<RepOrderBillingDTO> repOrders = repOrderBillingService.getRepOrdersForBilling();

        assertEquals(2, repOrders.size());
        assertEquals(expectedRepOrders, repOrders);
    }

    @Test
    void givenRepOrdersNotSuccessfullyUpdated_whenResetRepOrdersSentForBillingIsInvoked_thenReturnsFalse() {
        UpdateBillingRequest request = TestModelDataBuilder.getUpdateBillingRequest();
        when(repOrderBillingRepository.resetBillingFlagForRepOrderIds(request.getUserModified(), request.getIds()))
            .thenReturn(1);

        MAATCourtDataException exception = assertThrows(MAATCourtDataException.class,
            () -> repOrderBillingService.resetRepOrdersSentForBilling(request));

        assertEquals(
            "Unable to reset rep orders sent for billing as only 1 rep order(s) could be processed (from a total of 2 rep order(s))"
            , exception.getMessage());
    }

    @Test
    void givenRepOrdersSuccessfullyUpdated_whenResetRepOrdersSentForBillingIsInvoked_thenReturnsTrue() {
        UpdateBillingRequest request = TestModelDataBuilder.getUpdateBillingRequest();
        when(repOrderBillingRepository.resetBillingFlagForRepOrderIds(request.getUserModified(), request.getIds()))
            .thenReturn(request.getIds().size());

        assertDoesNotThrow(() -> repOrderBillingService.resetRepOrdersSentForBilling(request));
    }
}

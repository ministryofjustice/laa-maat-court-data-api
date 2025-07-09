package gov.uk.courtdata.billing.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.billing.dto.RepOrderBillingDTO;
import gov.uk.courtdata.billing.mapper.RepOrderBillingMapper;
import gov.uk.courtdata.billing.request.UpdateBillingRequest;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.RepOrderRepository;
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
    private RepOrderRepository repOrderRepository;

    @InjectMocks
    private RepOrderBillingService repOrderBillingService;

    @Test
    void givenNoRepOrders_whenGetRepOrdersForBillingIsInvoked_thenEmptyListIsReturned() {
        when(repOrderRepository.getRepOrdersForBilling()).thenReturn(Collections.emptyList());

        List<RepOrderBillingDTO> repOrders = repOrderBillingService.getRepOrdersForBilling();

        assertEquals(Collections.emptyList(), repOrders);
    }

    @Test
    void givenRepOrdersExist_whenGetRepOrdersForBillingIsInvoked_thenRepOrdersAreReturned() {
        when(repOrderRepository.getRepOrdersForBilling()).thenReturn(List.of(
            TestEntityDataBuilder.getPopulatedRepOrder(123),
            TestEntityDataBuilder.getPopulatedRepOrder(124)
        ));

        List<RepOrderBillingDTO> expectedRepOrders = List.of(
            RepOrderBillingMapper.mapEntityToDTO(TestEntityDataBuilder.getPopulatedRepOrder(123)),
            RepOrderBillingMapper.mapEntityToDTO(TestEntityDataBuilder.getPopulatedRepOrder(124))
        );

        List<RepOrderBillingDTO> repOrders = repOrderBillingService.getRepOrdersForBilling();

        assertEquals(2, repOrders.size());
        assertEquals(expectedRepOrders, repOrders);
    }

    @Test
    void givenNoRepOrdersToUpdate_whenResetRepOrdersSentForBillingIsInvoked_thenReturnsTrue() {
        UpdateBillingRequest request = UpdateBillingRequest.builder()
            .userModified("joe-bloggs")
            .ids(Collections.emptyList())
            .build();

        assertDoesNotThrow(() -> repOrderBillingService.resetRepOrdersSentForBilling(request));
    }

    @Test
    void givenUsernameNotSupplied_whenResetRepOrdersSentForBillingIsInvoked_thenReturnsFalse() {
        UpdateBillingRequest request = UpdateBillingRequest.builder()
            .userModified(null)
            .ids(List.of(1003456, 1003457))
            .build();

        ValidationException exception = assertThrows(ValidationException.class,
            () -> repOrderBillingService.resetRepOrdersSentForBilling(request));

        assertEquals("Username must be provided", exception.getMessage());
    }

    @Test
    void givenRepOrdersNotSuccessfullyUpdated_whenResetRepOrdersSentForBillingIsInvoked_thenReturnsFalse() {
        UpdateBillingRequest request = UpdateBillingRequest.builder()
            .userModified("joe-bloggs")
            .ids(List.of(1003456, 1003457))
            .build();

        when(repOrderRepository.resetBillingFlagForRepOrderIds(request.getUserModified(), request.getIds()))
            .thenReturn(1);

        MAATCourtDataException exception = assertThrows(MAATCourtDataException.class,
            () -> repOrderBillingService.resetRepOrdersSentForBilling(request));

        assertEquals(
            "Unable to reset rep orders sent for billing as only 1 rep order(s) could be processed (from a total of 2 rep order(s))"
            , exception.getMessage());
    }

    @Test
    void givenRepOrdersSuccessfullyUpdated_whenResetRepOrdersSentForBillingIsInvoked_thenReturnsTrue() {
        List<Integer> repOrdersIdsToUpdate = List.of(1003456, 1003457);

        UpdateBillingRequest request = UpdateBillingRequest.builder()
            .userModified("joe-bloggs")
            .ids(List.of(1003456, 1003457))
            .build();

        when(repOrderRepository.resetBillingFlagForRepOrderIds(request.getUserModified(), request.getIds()))
            .thenReturn(repOrdersIdsToUpdate.size());

        assertDoesNotThrow(() -> repOrderBillingService.resetRepOrdersSentForBilling(request));
    }
}

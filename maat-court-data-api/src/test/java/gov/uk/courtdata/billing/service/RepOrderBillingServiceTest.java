package gov.uk.courtdata.billing.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.billing.dto.RepOrderBillingDTO;
import gov.uk.courtdata.billing.repository.RepOrderBillingRepository;
import gov.uk.courtdata.billing.request.UpdateBillingRequest;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.MAATCourtDataException;

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

        assertThat(repOrders).isEmpty();
    }

    @Test
    void givenRepOrdersExist_whenGetRepOrdersForBillingIsInvoked_thenRepOrdersAreReturned() {
        when(repOrderBillingRepository.getRepOrdersForBilling())
                .thenReturn(List.of(
                        TestEntityDataBuilder.getPopulatedRepOrderForBilling(123),
                        TestEntityDataBuilder.getPopulatedRepOrderForBilling(124)));

        List<RepOrderBillingDTO> expectedRepOrders = List.of(
                TestModelDataBuilder.getRepOrderBillingDTO(123), TestModelDataBuilder.getRepOrderBillingDTO(124));

        List<RepOrderBillingDTO> repOrders = repOrderBillingService.getRepOrdersForBilling();

        assertThat(repOrders).hasSize(2).containsExactlyInAnyOrderElementsOf(expectedRepOrders);
    }

    @Test
    void givenRepOrdersNotSuccessfullyUpdated_whenResetRepOrdersSentForBillingIsInvoked_thenReturnsFalse() {
        UpdateBillingRequest request = TestModelDataBuilder.getUpdateBillingRequest();
        when(repOrderBillingRepository.resetBillingFlagForRepOrderIds(request.getUserModified(), request.getIds()))
                .thenReturn(1);

        assertThatThrownBy(() -> repOrderBillingService.resetRepOrdersSentForBilling(request))
                .isInstanceOf(MAATCourtDataException.class)
                .hasMessage(
                        "Unable to reset rep orders sent for billing as only 1 rep order(s) could be processed (from a total of 2 rep order(s))");
    }

    @Test
    void givenRepOrdersSuccessfullyUpdated_whenResetRepOrdersSentForBillingIsInvoked_thenReturnsTrue() {
        UpdateBillingRequest request = TestModelDataBuilder.getUpdateBillingRequest();
        when(repOrderBillingRepository.resetBillingFlagForRepOrderIds(request.getUserModified(), request.getIds()))
                .thenReturn(request.getIds().size());
        assertThatCode(() -> repOrderBillingService.resetRepOrdersSentForBilling(request))
                .doesNotThrowAnyException();
    }
}

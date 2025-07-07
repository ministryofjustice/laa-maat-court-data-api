package gov.uk.courtdata.billing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.billing.dto.RepOrderBillingDTO;
import gov.uk.courtdata.billing.mapper.RepOrderBillingMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
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

}

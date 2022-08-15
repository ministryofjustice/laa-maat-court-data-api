package gov.uk.courtdata.assessment.impl;



import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.repository.RepOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RepOrderImplTest {

    @InjectMocks
    private RepOrderImpl repOrderImpl;

    @Mock
    private  RepOrderRepository repOrderRepository;


    @Test
    public void testUpdateAppDateCompleted_whenUpdateIsSuccess() {
        repOrderImpl.updateAppDateCompleted(TestModelDataBuilder.REP_ORDERS_ID, LocalDateTime.now());
        verify(repOrderRepository, times(1)).updateAppDateCompleted(any(),any());
    }

    @Test
    public void testRepOrderImpl_findRepOrder_whenRepIDHasRecords() {
        repOrderImpl.findRepOrder(TestModelDataBuilder.REP_ORDERS_ID);
        verify(repOrderRepository, times(1)).getById(TestModelDataBuilder.REP_ORDERS_ID);
    }
}
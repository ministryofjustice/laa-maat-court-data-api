package gov.uk.courtdata.repOrder.impl;


import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.repository.RepOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RepOrderImplTest {

    @InjectMocks
    private RepOrderImpl repOrderImpl;

    @Mock
    private RepOrderRepository repOrderRepository;


    @Test
    public void testUpdateAppDateCompleted_whenUpdateIsSuccess() {
        when(repOrderRepository.getById(TestModelDataBuilder.REP_ORDERS_ID)).thenReturn(new RepOrderEntity());
        repOrderImpl.updateAppDateCompleted(TestModelDataBuilder.REP_ORDERS_ID, LocalDateTime.now());
        verify(repOrderRepository, times(1)).saveAndFlush(any());
    }

    @Test
    public void testRepOrderImpl_findRepOrder_whenRepIDHasRecords() {
        repOrderImpl.find(TestModelDataBuilder.REP_ORDERS_ID);
        verify(repOrderRepository, times(1)).getById(TestModelDataBuilder.REP_ORDERS_ID);
    }
}
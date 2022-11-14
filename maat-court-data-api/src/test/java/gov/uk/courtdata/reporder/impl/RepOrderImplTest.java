package gov.uk.courtdata.reporder.impl;


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
        when(repOrderRepository.getById(TestModelDataBuilder.REP_ID)).thenReturn(new RepOrderEntity());
        repOrderImpl.updateAppDateCompleted(TestModelDataBuilder.REP_ID, LocalDateTime.now());
        verify(repOrderRepository, times(1)).saveAndFlush(any());
    }

    @Test
    public void givenRepOrderExists_whenFindIsInvoked_thenRepOrderIsRetrieved() {
        repOrderImpl.find(TestModelDataBuilder.REP_ID);
        verify(repOrderRepository, times(1)).findById(TestModelDataBuilder.REP_ID);
    }

    @Test
    public void givenAValidRepId_whenCountByIdAndSentenceOrderDateIsNotNullInvoked_thenRepOrderCountIsRetrieved() {
        repOrderImpl.repOrderCountWithSentenceOrderDate(TestModelDataBuilder.REP_ID);
        verify(repOrderRepository, times(1)).countByIdAndSentenceOrderDateIsNotNull(TestModelDataBuilder.REP_ID);
    }
}
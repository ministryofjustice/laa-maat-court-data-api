package gov.uk.courtdata.reporder.impl;


import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.repository.RepOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepOrderImplTest {

    @InjectMocks
    private RepOrderImpl repOrderImpl;

    @Mock
    private RepOrderRepository repOrderRepository;


    @Test
    void testUpdateAppDateCompleted_whenUpdateIsSuccess() {
        when(repOrderRepository.getReferenceById(TestModelDataBuilder.REP_ID)).thenReturn(new RepOrderEntity());
        repOrderImpl.updateAppDateCompleted(TestModelDataBuilder.REP_ID, LocalDateTime.now());
        verify(repOrderRepository).saveAndFlush(any());
    }

    @Test
    void givenRepOrderExists_whenFindIsInvoked_thenRepOrderIsRetrieved() {
        repOrderImpl.find(TestModelDataBuilder.REP_ID);
        verify(repOrderRepository).findById(TestModelDataBuilder.REP_ID);
    }

    @Test
    void givenAValidRepId_whenUpdateRepOrderInvoked_thenUpdateRepOrdersIsSuccess() {
        repOrderImpl.updateRepOrder(TestEntityDataBuilder.getRepOrder());
        verify(repOrderRepository).saveAndFlush(any());
    }

    @Test
    void givenAValidRepId_whenCountByIdAndSentenceOrderDateIsNotNullInvoked_thenRepOrderCountIsRetrieved() {
        repOrderImpl.countWithSentenceOrderDate(TestModelDataBuilder.REP_ID);
        verify(repOrderRepository).count(ArgumentMatchers.<Specification<RepOrderEntity>>any());
    }
}
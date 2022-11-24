package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.impl.RepOrderImpl;
import gov.uk.courtdata.reporder.mapper.RepOrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RepOrderServiceTest {

    @Mock
    private RepOrderImpl repOrderImpl;

    @Mock
    private RepOrderMapper repOrderMapper;

    @InjectMocks
    private RepOrderService repOrderService;

    @Test
    void givenValidRepIdAndSentenceOrderFlagIsFalse_whenFindIsInvoked_thenRepOrderIsReturned() {
        when(repOrderImpl.find(anyInt()))
                .thenReturn(TestEntityDataBuilder.getRepOrder());

        when(repOrderMapper.repOrderEntityToRepOrderDTO(any(RepOrderEntity.class)))
                .thenReturn(TestModelDataBuilder.getRepOrderDTO());

        repOrderService.find(TestModelDataBuilder.REP_ID, false);
        verify(repOrderImpl).find(anyInt());
    }

    @Test
    void givenValidRepIdAndSentenceOrderFlagIsTrue_whenFindIsInvoked_thenRepOrderIsReturned() {
        when(repOrderImpl.findWithSentenceOrderDate(anyInt()))
                .thenReturn(TestEntityDataBuilder.getRepOrder());

        when(repOrderMapper.repOrderEntityToRepOrderDTO(any(RepOrderEntity.class)))
                .thenReturn(TestModelDataBuilder.getRepOrderDTO());

        repOrderService.find(TestModelDataBuilder.REP_ID, true);
        verify(repOrderImpl).findWithSentenceOrderDate(anyInt());
    }

    @Test
    void givenInvalidRepId_whenFindIsInvoked_thenErrorIsThrown() {
        assertThatThrownBy(() -> repOrderService.find(TestModelDataBuilder.REP_ID, false))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("No Rep Order found for ID: " + TestModelDataBuilder.REP_ID);
    }

    @Test
    void givenDateCompletedObject_whenUpdateAppDateCompletedIsInvoked_thenAppDateIsUpdated() {
        repOrderService.updateDateCompleted(new UpdateAppDateCompleted());
        verify(repOrderImpl).updateAppDateCompleted(any(), any());
    }

    @Test
    void givenValidRepId_whenExistsIsInvoked_thenReturnTrue() {
        when(repOrderImpl.countWithSentenceOrderDate(any()))
                .thenReturn(1L);
        assertThat(repOrderService.exists(TestModelDataBuilder.REP_ID)).isTrue();
    }

    @Test
    void givenInvalidRepId_whenExistsIsInvoked_thenReturnFalse() {
        when(repOrderImpl.countWithSentenceOrderDate(any()))
                .thenReturn(0L);
        assertThat(repOrderService.exists(TestModelDataBuilder.REP_ID)).isFalse();
    }
}
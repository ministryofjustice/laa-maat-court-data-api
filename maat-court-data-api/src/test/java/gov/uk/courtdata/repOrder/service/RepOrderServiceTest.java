package gov.uk.courtdata.repOrder.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.repOrder.impl.RepOrderImpl;
import gov.uk.courtdata.repOrder.mapper.RepOrderMapper;
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
    public void givenValidRepId_whenFindIsInvoked_thenRepOrderIsReturned() {
        when(repOrderImpl.find(anyInt()))
                .thenReturn(TestEntityDataBuilder.getRepOrder());
        when(repOrderMapper.RepOrderEntityToRepOrderDTO(any(RepOrderEntity.class)))
                .thenReturn(TestModelDataBuilder.getRepOrderDTO());
        assertThat(repOrderService.find(TestModelDataBuilder.REP_ID))
                .isEqualTo(TestModelDataBuilder.getRepOrderDTO());

    }

    @Test
    public void givenInValidRepId_whenFindIsInvoked_thenErrorIsThrown() {
        assertThatThrownBy(() -> repOrderService.find(TestModelDataBuilder.REP_ID))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("No Rep Order found for ID: " + TestModelDataBuilder.REP_ID);
    }

    @Test
    public void testRepOrdersService_updateAppDateCompleted_shouldSuccess() {
        repOrderService.updateAppDateCompleted(new UpdateAppDateCompleted());
        verify(repOrderImpl).updateAppDateCompleted(any(), any());
    }

    @Test
    public void givenAValidRepId_whenUpdateRepOrderInvoked_shouldSuccess() {
        repOrderService.updateRepOrder(new UpdateRepOrder());
        verify(repOrderImpl).updateRepOrder(any());
    }
}
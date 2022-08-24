package gov.uk.courtdata.repOrder.service;

import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.repOrder.impl.RepOrderImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class RepOrderServiceTest {

    @Mock
    private RepOrderImpl repOrderImpl;

    @InjectMocks
    private RepOrderService repOrderService;

    @Test
    public void testRepOrdersService_updateAppDateCompleted_shouldSuccess() {
        repOrderService.updateAppDateCompleted(new UpdateAppDateCompleted());
        verify(repOrderImpl, times(1)).updateAppDateCompleted(any(), any());
    }
}
package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.RepOrderImpl;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RepOrdersServiceTest {
    @InjectMocks
    private RepOrdersService repOrdersService;

    @Mock
    private RepOrderImpl repOrderImpl;

    @Test
    public void testRepOrdersService_updateAppDateCompleted_shouldSuccess_whenRepIdIsFound() {
        when(repOrderImpl.findRepOrder(any())).thenReturn(TestEntityDataBuilder.getPopulatedRepOrder(1000));
        doNothing().when(repOrderImpl).updateAppDateCompleted(any(),any());
        repOrdersService.updateAppDateCompleted(new UpdateAppDateCompleted());
    }
    @Test
    public void testRepOrdersService_updateAppDateCompleted_shouldFail_whenRepIdIsNotFound() {
        when(repOrderImpl.findRepOrder(any())).thenReturn(null);
        RequestedObjectNotFoundException requestedObjectNotFoundException = Assertions.assertThrows(RequestedObjectNotFoundException.class,
                () -> repOrdersService.updateAppDateCompleted(new UpdateAppDateCompleted().builder().repId(1000).build()));
        assertThat(requestedObjectNotFoundException.getMessage()).isEqualTo("No Rep order found for ID: 1000");
    }
}
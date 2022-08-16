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

import java.time.LocalDateTime;

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
    public void testRepOrdersService_updateAppDateCompleted_shouldSuccess() {
        repOrdersService.updateAppDateCompleted(new UpdateAppDateCompleted());
        verify(repOrderImpl,times(1)).updateAppDateCompleted(any(), any());
    }
}
package gov.uk.courtdata.assessment.impl;



import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RepOrderImplTest {

   @Mock
    private RepOrderImpl repOrderImpl;


    @Test
    public void testUpdateAppDateCompleted_whenUpdateIsSuccess() {
        doNothing().when(repOrderImpl).updateAppDateCompleted(any(),any());
        repOrderImpl.updateAppDateCompleted(1000,LocalDate.now());
        verify(repOrderImpl, times(1)).updateAppDateCompleted(any(),any());
    }

    @Test
    public void testRepOrderImpl_findRepOrder_whenRepIDHasRecords() {
        when(repOrderImpl.findRepOrder(any())).thenReturn(TestEntityDataBuilder.getPopulatedRepOrder(1000));
        RepOrderEntity repOrderEntity = repOrderImpl.findRepOrder(1000);
        assertThat(repOrderEntity.getId()).isEqualTo(1000);
    }
}
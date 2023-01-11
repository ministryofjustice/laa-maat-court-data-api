package gov.uk.courtdata.wqoffence.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.wqoffence.impl.WQOffenceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WQOffenceServiceTest {
    @Mock
    private WQOffenceImpl wqOffenceImpl;

    @InjectMocks
    private WQOffenceService wqOffenceService;

    @Test
    public void givenAValidInput_whenGetNewOffenceCountIsInvoked_shouldReturnNewOffenceCount() {
        when(wqOffenceImpl.getNewOffenceCount(anyInt(), anyString())).thenReturn(1);
        wqOffenceService.getNewOffenceCount(TestEntityDataBuilder.TEST_CASE_ID, TestModelDataBuilder.TEST_OFFENCE_ID);
        verify(wqOffenceImpl, atLeastOnce()).getNewOffenceCount(anyInt(), anyString());
    }

}
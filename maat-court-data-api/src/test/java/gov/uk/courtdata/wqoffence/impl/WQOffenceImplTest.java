package gov.uk.courtdata.wqoffence.impl;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.repository.WQOffenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class WQOffenceImplTest {

    @Mock
    private WQOffenceRepository wqOffenceRepository;

    @InjectMocks
    private WQOffenceImpl wqOffenceImpl;

    @Test
    public void givenAValidCaseIdAndOffenceId_whenGetNewOffenceCountIsInvoked_shouldReturnWQOffenceCount() {
        wqOffenceImpl.getNewOffenceCount(TestModelDataBuilder.TEST_CASE_ID, TestModelDataBuilder.TEST_OFFENCE_ID);
        verify(wqOffenceRepository, atLeastOnce()).getNewOffenceCount(anyInt(), anyString());
    }

}
package gov.uk.courtdata.offence.impl;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.repository.OffenceRepository;
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
public class OffenceImplTest {

    @Mock
    private OffenceRepository offenceRepository;

    @InjectMocks
    private OffenceImpl offenceImpl;

    @Test
    public void givenAValidCaseId_whenFindByCaseIdInvoked_shouldReturnOffenceEntity() {
        offenceImpl.findByCaseId(TestModelDataBuilder.TEST_CASE_ID);
        verify(offenceRepository, atLeastOnce()).findByCaseId(anyInt());
    }

    @Test
    public void givenAValidCaseIdAndOffenceId_whenGetNewOffenceCountInvoked_shouldReturnOffenceCount() {
        offenceImpl.getNewOffenceCount(TestModelDataBuilder.TEST_CASE_ID, TestModelDataBuilder.TEST_OFFENCE_ID);
        verify(offenceRepository, atLeastOnce()).getNewOffenceCount(anyInt(), anyString());
    }
}
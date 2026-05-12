package gov.uk.courtdata.offence.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.offence.impl.OffenceImpl;
import gov.uk.courtdata.offence.mapper.OffenceMapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OffenceServiceTest {

    @Mock
    private OffenceImpl offenceImpl;

    @Mock
    private OffenceMapper offenceMapper;

    @InjectMocks
    private OffenceService offenceService;

    @Test
    public void givenAValidCaseId_whenFindByCaseIdIsInvoked_shouldReturnOffenceDTO() {
        List offenceEntityList = List.of(TestEntityDataBuilder.getOffenceEntity(8064716));
        List offenceDTOList = List.of(TestModelDataBuilder.getOffenceDTO(8064716));
        when(offenceImpl.findByCaseId(TestEntityDataBuilder.TEST_CASE_ID)).thenReturn(offenceEntityList);
        when(offenceMapper.offenceEntityToOffenceDTO(anyList())).thenReturn(offenceDTOList);
        offenceService.findByCaseId(TestEntityDataBuilder.TEST_CASE_ID);
        verify(offenceImpl, atLeastOnce()).findByCaseId(anyInt());
        verify(offenceMapper, atLeastOnce()).offenceEntityToOffenceDTO(anyList());
    }

    @Test
    public void givenAValidInput_whenGetNewOffenceCountIsInvoked_shouldReturnNewOffenceCount() {
        when(offenceImpl.getNewOffenceCount(anyInt(), anyString())).thenReturn(1);
        offenceService.getNewOffenceCount(TestEntityDataBuilder.TEST_CASE_ID, TestModelDataBuilder.TEST_OFFENCE_ID);
        verify(offenceImpl, atLeastOnce()).getNewOffenceCount(anyInt(), anyString());
    }
}

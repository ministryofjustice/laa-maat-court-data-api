package gov.uk.courtdata.wqhearing.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.wqhearing.impl.WQHearingImpl;
import gov.uk.courtdata.wqhearing.mapper.WQHearingMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WQHearingServiceTest {

    @Mock
    private WQHearingImpl wqHearingImpl;
    @InjectMocks
    private WQHearingService wqHearingService;
    @Mock
    private WQHearingMapper mapper;

    @Test
    public void givenAValidMaatIdAndHearingUuid_whenFindByMaatIdAndHearingUUIDIsInvoked_shouldReturnWQHearningDTO() {
        List wqHearingEntityList = List.of(TestEntityDataBuilder.getWQHearingEntity(8064716));
        when(wqHearingImpl.findByMaatIdAndHearingUUID(TestEntityDataBuilder.TEST_OFFENCE_ID, TestEntityDataBuilder.TEST_CASE_ID)).thenReturn(wqHearingEntityList);
        List wqHearingDTOList = List.of(TestModelDataBuilder.getWQHearingDTO(8064716));
        when(mapper.WQHearingEntityToWQHearingDTO(anyList())).thenReturn(wqHearingDTOList);
        wqHearingService.findByMaatIdAndHearingUUID(TestEntityDataBuilder.TEST_OFFENCE_ID, TestEntityDataBuilder.TEST_CASE_ID);
        verify(wqHearingImpl, atLeastOnce()).findByMaatIdAndHearingUUID(anyString(), anyInt());
        verify(mapper, atLeastOnce()).WQHearingEntityToWQHearingDTO(anyList());
    }

}
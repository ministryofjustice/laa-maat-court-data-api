package gov.uk.courtdata.iojAppeal.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.iojAppeal.impl.IOJAppealImpl;
import gov.uk.courtdata.iojAppeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.model.CreateIOJAppeal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IOJAppealServiceTest {

    @InjectMocks
    private IOJAppealService iojAppealService;

    @Mock
    private IOJAppealImpl iojAppealImpl;

    @Mock
    private IOJAppealMapper iojAppealMapper;

    @Test
    public void whenFindIsInvoked_thenIOJAppealIsRetrieved() {
        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        when(iojAppealImpl.find(any())).thenReturn(IOJAppealEntity.builder().id(IOJ_APPEAL_ID).build());
        when(iojAppealMapper.toIOJAppealDTO(any(IOJAppealEntity.class))).thenReturn(iojAppealDTO);
        var returnedIOJAppeal = iojAppealService.find(IOJ_APPEAL_ID);
        assertEquals(returnedIOJAppeal.getId(), IOJ_APPEAL_ID);
    }

    @Test
    public void whenCreateIsInvoked_thenIOJAppealIsCreated() {
        var createIOJAppeal = TestModelDataBuilder.getCreateIOJAppealObject(true);
        var createdIOJAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
//        when(iojAppealImpl.find(any())).thenReturn(IOJAppealEntity.builder().id(IOJ_APPEAL_ID).build());
//        TODO: uncomment this: //when(iojAppealMapper.toIOJAppealDTO(any(CreateIOJAppeal.class))).thenReturn(createdIOJAppealDTO);

//        var returnedIOJAppeal = iojAppealService.find(IOJ_APPEAL_ID);
//        assertEquals(returnedIOJAppeal.getId(), IOJ_APPEAL_ID);
    }
}
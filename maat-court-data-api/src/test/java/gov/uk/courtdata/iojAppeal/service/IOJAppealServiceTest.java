package gov.uk.courtdata.iojAppeal.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.iojAppeal.impl.IOJAppealImpl;
import gov.uk.courtdata.iojAppeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.model.iojAppeal.CreateIOJAppeal;
import gov.uk.courtdata.model.iojAppeal.UpdateIOJAppeal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
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
        when(iojAppealImpl.find(any())).thenReturn(TestEntityDataBuilder.getIOJAppealEntity());
        when(iojAppealMapper.toIOJAppealDTO(any(IOJAppealEntity.class))).thenReturn(iojAppealDTO);
        var returnedIOJAppeal = iojAppealService.find(IOJ_APPEAL_ID);
        assertEquals(returnedIOJAppeal.getId(), IOJ_APPEAL_ID);
    }

    @Test
    public void whenFindByRepIdIsInvoked_thenIOJAppealIsRetrieved() {
        IOJAppealEntity iojAppealEntity = IOJAppealEntity.builder().id(IOJ_APPEAL_ID).repId(IOJ_REP_ID).build();
        when(iojAppealImpl.findByRepId(IOJ_REP_ID)).thenReturn(iojAppealEntity);
        when(iojAppealMapper.toIOJAppealDTO(iojAppealEntity))
                .thenReturn(IOJAppealDTO.builder().id(IOJ_APPEAL_ID).repId(IOJ_REP_ID).build());

        IOJAppealDTO returnedIOJAppeal = iojAppealService.findByRepId(IOJ_REP_ID);

        verify(iojAppealImpl).findByRepId(IOJ_REP_ID);
        verify(iojAppealMapper).toIOJAppealDTO(iojAppealEntity);
        assertThat(returnedIOJAppeal.getId()).isEqualTo(IOJ_APPEAL_ID);
        assertThat(returnedIOJAppeal.getRepId()).isEqualTo(IOJ_REP_ID);
    }

    @Test
    public void whenFindByRepIdIsInvokedWithInvalidRepId_thenNotFoundExceptionIsThrown() {
        when(iojAppealImpl.findByRepId(IOJ_REP_ID)).thenThrow(new RequestedObjectNotFoundException("No IOJAppeal object found for repId: " + IOJ_REP_ID));

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> iojAppealService.findByRepId(IOJ_REP_ID))
                .withMessageContaining("No IOJAppeal object found for repId: 5635978");
        verify(iojAppealImpl).findByRepId(IOJ_REP_ID);
    }

    @Test
    public void whenCreateIsInvoked_thenIOJAppealIsCreated() {
        var createdIOJAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        var createIOJAppeal = TestModelDataBuilder.getCreateIOJAppealObject();

        when(iojAppealMapper.toIOJAppealDTO(any(CreateIOJAppeal.class))).thenReturn(createdIOJAppealDTO);

        when(iojAppealImpl.create(any())).thenReturn(TestEntityDataBuilder.getIOJAppealEntity());

        when(iojAppealMapper.toIOJAppealDTO(any(IOJAppealEntity.class))).thenReturn(createdIOJAppealDTO);

        var newlyCreatedIOJAppealDTO = iojAppealService.create(createIOJAppeal);

        assertEquals(newlyCreatedIOJAppealDTO.getId(), IOJ_APPEAL_ID);

        verify(iojAppealImpl).setOldIOJAppealsReplaced(IOJ_REP_ID, IOJ_APPEAL_ID);
    }

    @Test
    public void whenUpdateIsInvoked_thenIOJAppealIsUpdated() {
        //given
        var updateIOJAppeal = TestModelDataBuilder.getUpdateIOJAppealObject();
        var matchingDateModified = LocalDateTime.of(2022, 1, 1, 10, 0);
        var updatedIOJAppealDTO = TestModelDataBuilder.getIOJAppealDTO(matchingDateModified);
        var updatedIOJEntity = TestEntityDataBuilder.getIOJAppealEntity(matchingDateModified);

        when(iojAppealMapper.toIOJAppealDTO(any(UpdateIOJAppeal.class))).thenReturn(updatedIOJAppealDTO);
        when(iojAppealImpl.update(any())).thenReturn(updatedIOJEntity);
        when(iojAppealMapper.toIOJAppealDTO(any(IOJAppealEntity.class))).thenReturn(updatedIOJAppealDTO);

        var newlyUpdatedIOJAppealDTO = iojAppealService.update(updateIOJAppeal);

        assertEquals(newlyUpdatedIOJAppealDTO.getDateModified(), matchingDateModified);
    }
}
package gov.uk.courtdata.iojAppeal.impl;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.iojAppeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.repository.IOJAppealRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IOJAppealImplTest {

    @Spy
    @InjectMocks
    private IOJAppealImpl iojAppealImpl;

    @Spy
    private IOJAppealRepository iojAppealRepository;

    @Mock
    private IOJAppealMapper iojAppealMapper;

    @Captor
    private ArgumentCaptor<IOJAppealEntity> iojAppealEntityArgumentCaptor;

    @Test
    public void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(iojAppealRepository.findById(any())).thenReturn(Optional.of(IOJAppealEntity.builder().id(IOJ_APPEAL_ID).build()));
        var iojAppeal = iojAppealImpl.find(IOJ_APPEAL_ID);
        assertEquals(iojAppeal.getId(), IOJ_APPEAL_ID);
    }

    @Test
    public void whenFindByRepIdIsInvoked_thenAssessmentIsRetrieved() {
        when(iojAppealRepository.findByRepId(IOJ_REP_ID)).thenReturn(Optional.of(IOJAppealEntity.builder().id(IOJ_APPEAL_ID).repId(IOJ_REP_ID).build()));
        var iojAppeal = iojAppealImpl.findByRepId(IOJ_REP_ID);
        assertEquals(iojAppeal.getId(), IOJ_APPEAL_ID);
        assertEquals(iojAppeal.getRepId(), IOJ_REP_ID);
    }

    @Test
    public void whenCreateIsInvoked_thenIOJAppealIsSaved() {

        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        when(iojAppealMapper.toIOJIojAppealEntity(any())).thenReturn(TestEntityDataBuilder.getIOJAppealEntity());

        iojAppealImpl.create(iojAppealDTO);

        verify(iojAppealRepository).save(iojAppealEntityArgumentCaptor.capture());

        assertThat(iojAppealEntityArgumentCaptor.getValue().getId()).isEqualTo(iojAppealDTO.getId());
    }

    @Test
    public void whenSetOldIOJAppealReplaced_thenAllIOJAppealRecordsWithGivenREP_IDAreSetToReplaced(){

        iojAppealImpl.setOldIOJAppealsReplaced(IOJ_REP_ID, 124);

        verify(iojAppealRepository).setOldIOJAppealsReplaced(IOJ_REP_ID,124);
    }

    @Test
    public void whenUpdateIsInvoked_thenIOJAppealIsUpdated() {
        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        var dateModified = LocalDateTime.of(2022,1,1,10,0);
        var updatedIOJAppealEntity = TestEntityDataBuilder.getIOJAppealEntity(dateModified);

        when(iojAppealRepository.getById(any())).thenReturn(updatedIOJAppealEntity);

        iojAppealImpl.update(iojAppealDTO);

        verify(iojAppealRepository).save(iojAppealEntityArgumentCaptor.capture());

        assertThat(iojAppealEntityArgumentCaptor.getValue().getDateModified()).isEqualTo(dateModified);
    }
}
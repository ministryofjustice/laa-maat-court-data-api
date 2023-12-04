package gov.uk.courtdata.iojappeal.impl;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.iojappeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.repository.IOJAppealRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
        when(iojAppealRepository.getReferenceById(any())).thenReturn(IOJAppealEntity.builder().id(IOJ_APPEAL_ID).build());
        var iojAppeal = iojAppealImpl.find(IOJ_APPEAL_ID);
        assertEquals(IOJ_APPEAL_ID, iojAppeal.getId());
    }

    @Test
    public void whenFindByRepIdIsInvoked_thenAssessmentIsRetrieved() {
        when(iojAppealRepository.findByRepId(IOJ_REP_ID)).thenReturn(IOJAppealEntity
                .builder()
                .id(IOJ_APPEAL_ID)
                .repOrder(TestEntityDataBuilder.getPopulatedRepOrder(IOJ_REP_ID)).build());
        var iojAppeal = iojAppealImpl.findByRepId(IOJ_REP_ID);
        assertEquals(IOJ_APPEAL_ID, iojAppeal.getId());
        assertEquals(IOJ_REP_ID, iojAppeal.getRepOrder().getId());
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
    public void whenSetOldIOJAppealReplaced_thenAllIOJAppealRecordsWithGivenREP_IDAreSetToReplaced() {

        iojAppealImpl.setOldIOJAppealsReplaced(IOJ_REP_ID, 124);

        verify(iojAppealRepository).setOldIOJAppealsReplaced(IOJ_REP_ID, 124);
    }

    @Test
    public void whenUpdateIsInvoked_thenIOJAppealIsUpdated() {
        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        var dateModified = LocalDateTime.of(2022, 1, 1, 10, 0);
        var updatedIOJAppealEntity = TestEntityDataBuilder.getIOJAppealEntity(dateModified);

        when(iojAppealRepository.getReferenceById(any())).thenReturn(updatedIOJAppealEntity);

        iojAppealImpl.update(iojAppealDTO);

        verify(iojAppealRepository).save(iojAppealEntityArgumentCaptor.capture());

        assertThat(iojAppealEntityArgumentCaptor.getValue().getDateModified()).isEqualTo(dateModified);
    }
}
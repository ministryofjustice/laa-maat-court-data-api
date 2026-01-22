package gov.uk.courtdata.iojappeal.service;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.iojappeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.repository.IOJAppealRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IOJAppealPersistenceServiceTest {

    @Spy
    @InjectMocks
    private IOJAppealPersistenceService iojAppealPersistenceService;

    @Spy
    private IOJAppealRepository iojAppealRepository;

    @Mock
    private IOJAppealMapper iojAppealMapper;

    @Captor
    private ArgumentCaptor<IOJAppealEntity> iojAppealEntityArgumentCaptor;

    @Test
    void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(iojAppealRepository.getReferenceById(any())).thenReturn(IOJAppealEntity.builder().id(IOJ_APPEAL_ID).build());
        var iojAppeal = iojAppealPersistenceService.find(IOJ_APPEAL_ID);
        assertEquals(IOJ_APPEAL_ID, iojAppeal.getId());
    }

    @Test
    void whenFindByRepIdIsInvoked_thenAssessmentIsRetrieved() {
        when(iojAppealRepository.findByRepId(IOJ_REP_ID)).thenReturn(IOJAppealEntity
                .builder()
                .id(IOJ_APPEAL_ID)
                .repOrder(TestEntityDataBuilder.getPopulatedRepOrder(IOJ_REP_ID)).build());
        var iojAppeal = iojAppealPersistenceService.findByRepId(IOJ_REP_ID);
        assertEquals(IOJ_APPEAL_ID, iojAppeal.getId());
        assertEquals(IOJ_REP_ID, iojAppeal.getRepOrder().getId());
    }

    @Test
    void whenSaveIsInvoked_thenIOJAppealIsSaved() {
        var createdIOJAppealEntity = TestEntityDataBuilder.getIOJAppealEntity();
        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();

        iojAppealPersistenceService.save(createdIOJAppealEntity);

        verify(iojAppealRepository).save(iojAppealEntityArgumentCaptor.capture());

        assertThat(iojAppealEntityArgumentCaptor.getValue().getId()).isEqualTo(iojAppealDTO.getId());
    }

    @Test
    void whenSetOldIOJAppealReplaced_thenAllIOJAppealRecordsWithGivenRepId_thenIDAreSetToReplaced() {

        iojAppealPersistenceService.setOldIOJAppealsReplaced(IOJ_REP_ID, 124);

        verify(iojAppealRepository).setOldIOJAppealsReplaced(IOJ_REP_ID, 124);
    }

    @Test
    void whenUpdateIsInvoked_thenIOJAppealIsUpdated() {
        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        var dateModified = LocalDateTime.of(2022, 1, 1, 10, 0);
        var updatedIOJAppealEntity = TestEntityDataBuilder.getIOJAppealEntity(dateModified);

        when(iojAppealRepository.getReferenceById(any())).thenReturn(updatedIOJAppealEntity);

        iojAppealPersistenceService.update(iojAppealDTO);

        verify(iojAppealRepository).save(iojAppealEntityArgumentCaptor.capture());

        assertThat(iojAppealEntityArgumentCaptor.getValue().getDateModified()).isEqualTo(dateModified);
    }
}
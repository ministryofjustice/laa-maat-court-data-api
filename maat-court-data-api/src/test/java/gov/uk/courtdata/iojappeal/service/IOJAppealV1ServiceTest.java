package gov.uk.courtdata.iojappeal.service;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.iojappeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.model.iojAppeal.CreateIOJAppeal;
import gov.uk.courtdata.model.iojAppeal.UpdateIOJAppeal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IOJAppealV1ServiceTest {

    @InjectMocks
    private IOJAppealV1Service iojAppealService;

    @Mock
    private IOJAppealPersistenceService iojAppealPersistenceService;

    @Mock
    private IOJAppealMapper iojAppealMapper;

    @Test
    void whenFindIsInvoked_thenIOJAppealIsRetrieved() {
        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        when(iojAppealPersistenceService.find(any())).thenReturn(TestEntityDataBuilder.getIOJAppealEntity());
        when(iojAppealMapper.toIOJAppealDTO(any(IOJAppealEntity.class))).thenReturn(iojAppealDTO);
        var returnedIOJAppeal = iojAppealService.find(IOJ_APPEAL_ID);
        assertEquals(IOJ_APPEAL_ID, returnedIOJAppeal.getId());
    }

    @Test
    void whenFindIsInvokedWithInvalidId_thenNotFoundExceptionIsThrown() {
        when(iojAppealPersistenceService.find(IOJ_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> iojAppealService.find(IOJ_REP_ID))
                .withMessageContaining(String.format("No IoJ Appeal found for ID: %d", IOJ_REP_ID));
    }

    @Test
    void whenFindByRepIdIsInvoked_thenIOJAppealIsRetrieved() {
        IOJAppealEntity iojAppealEntity = IOJAppealEntity
                .builder()
                .id(IOJ_APPEAL_ID)
                .repOrder(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID)).build();
        when(iojAppealPersistenceService.findByRepId(IOJ_REP_ID)).thenReturn(iojAppealEntity);
        when(iojAppealMapper.toIOJAppealDTO(iojAppealEntity))
                .thenReturn(IOJAppealDTO.builder().id(IOJ_APPEAL_ID).repId(IOJ_REP_ID).build());

        IOJAppealDTO returnedIOJAppeal = iojAppealService.findByRepId(IOJ_REP_ID);

        verify(iojAppealPersistenceService).findByRepId(IOJ_REP_ID);
        verify(iojAppealMapper).toIOJAppealDTO(iojAppealEntity);
        assertThat(returnedIOJAppeal.getId()).isEqualTo(IOJ_APPEAL_ID);
        assertThat(returnedIOJAppeal.getRepId()).isEqualTo(IOJ_REP_ID);
    }

    @Test
    void whenFindByRepIdIsInvokedWithInvalidRepId_thenNotFoundExceptionIsThrown() {
        when(iojAppealPersistenceService.findByRepId(IOJ_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> iojAppealService.findByRepId(IOJ_REP_ID))
                .withMessageContaining(String.format("No IoJ Appeal found for REP ID: %d", IOJ_REP_ID));
    }

    @Test
    void whenFindCurrentPassedAppealByRepIdIsInvoked_thenIOJAppealIsRetrieved() {
        IOJAppealEntity iojAppealEntity = IOJAppealEntity
                .builder()
                .id(IOJ_APPEAL_ID)
                .repOrder(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID)).build();
        when(iojAppealPersistenceService.findCurrentPassedByRepId(IOJ_REP_ID)).thenReturn(iojAppealEntity);
        when(iojAppealMapper.toIOJAppealDTO(iojAppealEntity))
                .thenReturn(IOJAppealDTO.builder().id(IOJ_APPEAL_ID).repId(IOJ_REP_ID).build());

        IOJAppealDTO returnedIOJAppeal = iojAppealService.findCurrentPassedAppealByRepId(IOJ_REP_ID);

        verify(iojAppealPersistenceService).findCurrentPassedByRepId(IOJ_REP_ID);
        verify(iojAppealMapper).toIOJAppealDTO(iojAppealEntity);
        assertThat(returnedIOJAppeal.getId()).isEqualTo(IOJ_APPEAL_ID);
        assertThat(returnedIOJAppeal.getRepId()).isEqualTo(IOJ_REP_ID);
    }

    @Test
    void whenFindCurrentPassedAppealByRepIdIsInvokedWithInvalidRepId_thenNotFoundExceptionIsThrown() {
        when(iojAppealPersistenceService.findCurrentPassedByRepId(IOJ_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> iojAppealService.findCurrentPassedAppealByRepId(IOJ_REP_ID))
                .withMessageContaining(String.format("No IoJ Appeal found for REP ID: %d", IOJ_REP_ID));
    }

    @Test
    void whenCreateIsInvoked_thenIOJAppealIsCreated() {
        var createdIOJAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        var createIOJAppeal = TestModelDataBuilder.getCreateIOJAppealObject();
        var createdIOJAppealEntity = TestEntityDataBuilder.getIOJAppealEntity();

        when(iojAppealMapper.toIOJAppealDTO(any(CreateIOJAppeal.class))).thenReturn(createdIOJAppealDTO);

        when(iojAppealMapper.toIOJAppealDTO(any(IOJAppealEntity.class))).thenReturn(createdIOJAppealDTO);

        when(iojAppealMapper.toIojAppealEntity(any(IOJAppealDTO.class))).thenReturn(createdIOJAppealEntity);

        var newlyCreatedIOJAppealDTO = iojAppealService.create(createIOJAppeal);

        assertEquals(IOJ_APPEAL_ID, newlyCreatedIOJAppealDTO.getId());

        verify(iojAppealPersistenceService).setOldIOJAppealsReplaced(IOJ_REP_ID, IOJ_APPEAL_ID);
    }

    @Test
    void whenUpdateIsInvoked_thenIOJAppealIsUpdated() {
        //given
        var updateIOJAppeal = TestModelDataBuilder.getUpdateIOJAppealObject();
        var matchingDateModified = LocalDateTime.of(2022, 1, 1, 10, 0);
        var updatedIOJAppealDTO = TestModelDataBuilder.getIOJAppealDTO(matchingDateModified);
        var updatedIOJEntity = TestEntityDataBuilder.getIOJAppealEntity(matchingDateModified);

        when(iojAppealMapper.toIOJAppealDTO(any(UpdateIOJAppeal.class))).thenReturn(updatedIOJAppealDTO);
        when(iojAppealPersistenceService.update(any())).thenReturn(updatedIOJEntity);
        when(iojAppealMapper.toIOJAppealDTO(any(IOJAppealEntity.class))).thenReturn(updatedIOJAppealDTO);

        var newlyUpdatedIOJAppealDTO = iojAppealService.update(updateIOJAppeal);

        assertEquals(newlyUpdatedIOJAppealDTO.getDateModified(), matchingDateModified);
    }
}
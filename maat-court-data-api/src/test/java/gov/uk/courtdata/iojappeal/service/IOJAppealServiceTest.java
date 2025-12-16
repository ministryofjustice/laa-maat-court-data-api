package gov.uk.courtdata.iojappeal.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.iojappeal.impl.IOJAppealImpl;
import gov.uk.courtdata.iojappeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.model.iojAppeal.CreateIOJAppeal;
import gov.uk.courtdata.model.iojAppeal.UpdateIOJAppeal;
import gov.uk.courtdata.repository.IOJAppealRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IOJAppealServiceTest {

    @InjectMocks
    private IOJAppealService iojAppealService;

    @Mock
    private IOJAppealImpl iojAppealImpl;

    @Mock
    private IOJAppealMapper iojAppealMapper;

    @Mock
    private IOJAppealRepository iojAppealRepository;

    @Test
    public void whenFindIsInvoked_thenIOJAppealIsRetrieved() {
        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        when(iojAppealImpl.find(any())).thenReturn(TestEntityDataBuilder.getIOJAppealEntity());
        when(iojAppealMapper.toIOJAppealDTO(any(IOJAppealEntity.class))).thenReturn(iojAppealDTO);
        var returnedIOJAppeal = iojAppealService.find(IOJ_APPEAL_ID);
        assertEquals(IOJ_APPEAL_ID, returnedIOJAppeal.getId());
    }

    @Test
    public void whenFindIsInvokedWithInvalidId_thenNotFoundExceptionIsThrown() {
        when(iojAppealImpl.find(IOJ_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
            .isThrownBy(() -> iojAppealService.find(IOJ_REP_ID))
            .withMessageContaining(String.format("No IOJ Appeal found for ID: %d", IOJ_REP_ID));
    }

    @Test
    public void whenFindByRepIdIsInvoked_thenIOJAppealIsRetrieved() {
        IOJAppealEntity iojAppealEntity = IOJAppealEntity
            .builder()
            .id(IOJ_APPEAL_ID)
            .repOrder(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID)).build();
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
        when(iojAppealImpl.findByRepId(IOJ_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
            .isThrownBy(() -> iojAppealService.findByRepId(IOJ_REP_ID))
            .withMessageContaining(String.format("No IOJ Appeal found for REP ID: %d", IOJ_REP_ID));
    }

    @Test
    public void whenFindCurrentPassedAppealByRepIdIsInvoked_thenIOJAppealIsRetrieved() {
        IOJAppealEntity iojAppealEntity = IOJAppealEntity
            .builder()
            .id(IOJ_APPEAL_ID)
            .repOrder(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID)).build();
        when(iojAppealImpl.findCurrentPassedByRepId(IOJ_REP_ID)).thenReturn(iojAppealEntity);
        when(iojAppealMapper.toIOJAppealDTO(iojAppealEntity))
            .thenReturn(IOJAppealDTO.builder().id(IOJ_APPEAL_ID).repId(IOJ_REP_ID).build());

        IOJAppealDTO returnedIOJAppeal = iojAppealService.findCurrentPassedAppealByRepId(
            IOJ_REP_ID);

        verify(iojAppealImpl).findCurrentPassedByRepId(IOJ_REP_ID);
        verify(iojAppealMapper).toIOJAppealDTO(iojAppealEntity);
        assertThat(returnedIOJAppeal.getId()).isEqualTo(IOJ_APPEAL_ID);
        assertThat(returnedIOJAppeal.getRepId()).isEqualTo(IOJ_REP_ID);
    }

    @Test
    public void whenFindCurrentPassedAppealByRepIdIsInvokedWithInvalidRepId_thenNotFoundExceptionIsThrown() {
        when(iojAppealImpl.findCurrentPassedByRepId(IOJ_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
            .isThrownBy(() -> iojAppealService.findCurrentPassedAppealByRepId(IOJ_REP_ID))
            .withMessageContaining(String.format("No IOJ Appeal found for REP ID: %d", IOJ_REP_ID));
    }

    @Test
    public void whenCreateIsInvoked_thenIOJAppealIsCreated() {
        var createdIOJAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        var createIOJAppeal = TestModelDataBuilder.getCreateIOJAppealObject();

        when(iojAppealMapper.toIOJAppealDTO(any(CreateIOJAppeal.class))).thenReturn(
            createdIOJAppealDTO);

        when(iojAppealImpl.create(any())).thenReturn(TestEntityDataBuilder.getIOJAppealEntity());

        when(iojAppealMapper.toIOJAppealDTO(any(IOJAppealEntity.class))).thenReturn(
            createdIOJAppealDTO);

        var newlyCreatedIOJAppealDTO = iojAppealService.create(createIOJAppeal);

        assertEquals(IOJ_APPEAL_ID, newlyCreatedIOJAppealDTO.getId());

        verify(iojAppealImpl).setOldIOJAppealsReplaced(IOJ_REP_ID, IOJ_APPEAL_ID);
    }

    @Test
    public void whenUpdateIsInvoked_thenIOJAppealIsUpdated() {
        //given
        var updateIOJAppeal = TestModelDataBuilder.getUpdateIOJAppealObject();
        var matchingDateModified = LocalDateTime.of(2022, 1, 1, 10, 0);
        var updatedIOJAppealDTO = TestModelDataBuilder.getIOJAppealDTO(matchingDateModified);
        var updatedIOJEntity = TestEntityDataBuilder.getIOJAppealEntity(matchingDateModified);

        when(iojAppealMapper.toIOJAppealDTO(any(UpdateIOJAppeal.class))).thenReturn(
            updatedIOJAppealDTO);
        when(iojAppealImpl.update(any())).thenReturn(updatedIOJEntity);
        when(iojAppealMapper.toIOJAppealDTO(any(IOJAppealEntity.class))).thenReturn(
            updatedIOJAppealDTO);

        var newlyUpdatedIOJAppealDTO = iojAppealService.update(updateIOJAppeal);

        assertEquals(newlyUpdatedIOJAppealDTO.getDateModified(), matchingDateModified);
    }

    @Test
    void givenValidIoJAppealId_whenRollbackIsInvoked_thenIoJAppealIsRolledBack() {
        IOJAppealEntity iojAppealEntity = TestEntityDataBuilder.getIOJAppealEntity();
        when(iojAppealImpl.find(iojAppealEntity.getId())).thenReturn(iojAppealEntity);

        iojAppealService.rollback(iojAppealEntity.getId());

        assertThat(iojAppealEntity.getReplaced()).isEqualTo("Y");
        verify(iojAppealRepository).save(any(IOJAppealEntity.class));
    }

    @Test
    void givenInvalidIoJAppealId_whenRollbackIsInvoked_thenExceptionIsRaised() {
        when(iojAppealImpl.find(IOJ_APPEAL_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class).isThrownBy(
                () -> iojAppealService.rollback(IOJ_APPEAL_ID))
            .withMessageContaining(String.format("No IOJ Appeal found for ID: %d", IOJ_APPEAL_ID));
    }
}
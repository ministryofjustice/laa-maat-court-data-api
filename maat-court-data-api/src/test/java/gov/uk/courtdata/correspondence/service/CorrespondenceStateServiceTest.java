package gov.uk.courtdata.correspondence.service;

import gov.uk.courtdata.correspondence.dto.CorrespondenceStateDTO;
import gov.uk.courtdata.entity.CorrespondenceStateEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.CorrespondenceStateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static gov.uk.courtdata.builder.TestModelDataBuilder.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CorrespondenceStateServiceTest {

    private static int INVALID_REP_ID = 1235;
    @InjectMocks
    private CorrespondenceStateService correspondenceStateService;
    @Mock
    private CorrespondenceStateRepository correspondenceStateRepository;

    @Test
    void givenValidRepId_whenGetCorrespondenceStatusIsInvoked_thenStatusIsReturned() {
        when(correspondenceStateRepository.findByRepId(REP_ID))
                .thenReturn(buildCorrespondenceStateEntity(REP_ID, CORRESPONDENCE_STATUS));

        String status = correspondenceStateService.getCorrespondenceStatus(REP_ID);
        verify(correspondenceStateRepository).findByRepId(REP_ID);
        assertThat(status).isEqualTo(CORRESPONDENCE_STATUS);
    }

    @Test
    void givenInvalidRepId_whenGetCorrespondenceStatusIsInvoked_thenStatusIsReturned() {
        when(correspondenceStateRepository.findByRepId(INVALID_REP_ID)).thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> correspondenceStateService.getCorrespondenceStatus(INVALID_REP_ID))
                .withMessageContaining("No corresponsdence state found for repId= 1235");
        verify(correspondenceStateRepository).findByRepId(INVALID_REP_ID);
    }

    @Test
    void givenCorrespondenceStateDTO_whenCreateCorrespondenceStateIsInvoked_thenCorrespondenceStateIsCreated() throws Exception {
        when(correspondenceStateRepository.saveAndFlush(any(CorrespondenceStateEntity.class)))
                .thenReturn(buildCorrespondenceStateEntity(REP_ID, CORRESPONDENCE_STATUS));

        CorrespondenceStateDTO correspondenceStateDTO = correspondenceStateService
                .createCorrespondenceState(buildCorrespondenceStateDTO(REP_ID, CORRESPONDENCE_STATUS));
        assertThat(correspondenceStateDTO.getRepId()).isEqualTo(REP_ID);
        assertThat(correspondenceStateDTO.getStatus()).isEqualTo(CORRESPONDENCE_STATUS);
        verify(correspondenceStateRepository).saveAndFlush(any(CorrespondenceStateEntity.class));
    }

    @Test
    void givenCorrespondenceStateDTO_whenUpdateCorrespondenceStateIsInvoked_thenCorrespondenceStateIsUpdated() throws Exception {
        when(correspondenceStateRepository.findByRepId(REP_ID))
                .thenReturn(CorrespondenceStateEntity.builder().repId(REP_ID).status(CORRESPONDENCE_STATUS).build());
        when(correspondenceStateRepository.saveAndFlush(any(CorrespondenceStateEntity.class)))
                .thenReturn(buildCorrespondenceStateEntity(REP_ID, "none"));

        CorrespondenceStateDTO responseDTO = correspondenceStateService.updateCorrespondenceState(buildCorrespondenceStateDTO(REP_ID, "none"));
        assertThat(responseDTO.getRepId()).isEqualTo(REP_ID);
        assertThat(responseDTO.getStatus()).isEqualTo("none");
        verify(correspondenceStateRepository).findByRepId(REP_ID);
        verify(correspondenceStateRepository).saveAndFlush(any(CorrespondenceStateEntity.class));
    }

    @Test
    void givenInvalidCorrespondenceStateDTO_whenUpdateCorrespondenceStateIsInvoked_thenNotFoundIsThrown() throws Exception {
        when(correspondenceStateRepository.findByRepId(INVALID_REP_ID)).thenReturn(null);
        CorrespondenceStateDTO correspondenceStateDTO = buildCorrespondenceStateDTO(INVALID_REP_ID, CORRESPONDENCE_STATUS);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> correspondenceStateService.updateCorrespondenceState(correspondenceStateDTO))
                .withMessageContaining("No corresponsdence state found for repId=" + INVALID_REP_ID);

    }

}

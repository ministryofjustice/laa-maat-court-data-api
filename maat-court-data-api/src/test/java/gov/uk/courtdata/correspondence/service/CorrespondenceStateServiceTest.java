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

import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CorrespondenceStateServiceTest {

    @InjectMocks
    private CorrespondenceStateService correspondenceStateService;
    @Mock
    private CorrespondenceStateRepository correspondenceStateRepository;

    @Test
    void givenValidRepId_whenGetCorrespondenceStatusIsInvoked_thenStatusIsReturned() {
        when(correspondenceStateRepository.findByRepId(REP_ID))
                .thenReturn(CorrespondenceStateEntity.builder().repId(REP_ID).status("none").build());

        String status = correspondenceStateService.getCorrespondenceStatus(REP_ID);

        verify(correspondenceStateRepository).findByRepId(REP_ID);
        assertThat(status).isEqualTo("none");
    }

    @Test
    void givenInvalidRepId_whenGetCorrespondenceStatusIsInvoked_thenStatusIsReturned() {
        when(correspondenceStateRepository.findByRepId(1211)).thenReturn(null);
        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> correspondenceStateService.getCorrespondenceStatus(1211))
                .withMessageContaining("No corresponsdence state found for repId: 1211");
        verify(correspondenceStateRepository).findByRepId(1211);
    }

    @Test
    void givenCorrespondenceStateDTO_whenCreateCorrespondenceStateIsInvoked_thenCorrespondenceStateIsCreated() throws Exception {
        CorrespondenceStateEntity entity = CorrespondenceStateEntity.builder().repId(REP_ID).status("none").build();
        when(correspondenceStateRepository.saveAndFlush(any(CorrespondenceStateEntity.class))).thenReturn(entity);

        CorrespondenceStateDTO correspondenceStateDTO = correspondenceStateService
                .createCorrespondenceState(buildCorrespondenceStateDTO(REP_ID, "none"));
        assertThat(correspondenceStateDTO.getRepId()).isEqualTo(REP_ID);
        assertThat(correspondenceStateDTO.getStatus()).isEqualTo("none");
        verify(correspondenceStateRepository).saveAndFlush(any(CorrespondenceStateEntity.class));
    }

    @Test
    void givenCorrespondenceStateDTO_whenUpdateCorrespondenceStateIsInvoked_thenCorrespondenceStateIsUpdated() throws Exception {
        CorrespondenceStateEntity entity = CorrespondenceStateEntity.builder().repId(REP_ID).status("none").build();
        when(correspondenceStateRepository.findByRepId(REP_ID))
                .thenReturn(CorrespondenceStateEntity.builder().repId(REP_ID).status("appealCC").build());
        when(correspondenceStateRepository.saveAndFlush(any(CorrespondenceStateEntity.class))).thenReturn(entity);

        CorrespondenceStateDTO response = correspondenceStateService.updateCorrespondenceState(buildCorrespondenceStateDTO(REP_ID, "none"));
        assertThat(response.getRepId()).isEqualTo(REP_ID);
        assertThat(response.getStatus()).isEqualTo("none");
        verify(correspondenceStateRepository).findByRepId(REP_ID);
        verify(correspondenceStateRepository).saveAndFlush(any(CorrespondenceStateEntity.class));
    }

    @Test
    void givenInvalidCorrespondenceStateDTO_whenUpdateCorrespondenceStateIsInvoked_thenNotFoundIsThrown() throws Exception {
        when(correspondenceStateRepository.findByRepId(1211)).thenReturn(null);
        CorrespondenceStateDTO correspondenceStateDTO = buildCorrespondenceStateDTO(1211, "appealCC");

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> correspondenceStateService.updateCorrespondenceState(correspondenceStateDTO))
                .withMessageContaining("No corresponsdence state found for repId: 1211");

    }

    private CorrespondenceStateDTO buildCorrespondenceStateDTO(Integer repId, String status) {
        return CorrespondenceStateDTO.builder()
                .repId(repId)
                .status(status)
                .build();
    }

}

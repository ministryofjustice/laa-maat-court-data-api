package gov.uk.courtdata.correspondence.service;

import gov.uk.courtdata.entity.CorrespondenceStateEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.CorrespondenceStateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.enums.contribution.CorrespondenceStatus;

import static gov.uk.courtdata.builder.TestModelDataBuilder.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CorrespondenceStateServiceTest {

    private static final int INVALID_REP_ID = 1235;

    @InjectMocks
    private CorrespondenceStateService correspondenceStateService;

    @Mock
    private CorrespondenceStateRepository correspondenceStateRepository;

    @Test
    void givenValidRepId_whenGetCorrespondenceStatusIsInvoked_thenStatusIsReturned() {
        when(correspondenceStateRepository.findByRepId(REP_ID))
                .thenReturn(buildCorrespondenceStateEntity(REP_ID, CorrespondenceStatus.APPEAL_CC));
        CorrespondenceStatus status = correspondenceStateService.getCorrespondenceStatus(REP_ID);
        assertThat(status)
                .isEqualTo(CorrespondenceStatus.APPEAL_CC);
    }

    @Test
    void givenInvalidRepId_whenGetCorrespondenceStatusIsInvoked_thenExceptionIsThrown() {
        when(correspondenceStateRepository.findByRepId(INVALID_REP_ID))
                .thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> correspondenceStateService.getCorrespondenceStatus(INVALID_REP_ID))
                .withMessageContaining("No correspondence state found for repId= 1235");
    }

    @Test
    void givenValidParameters_whenCreateCorrespondenceStateIsInvoked_thenCorrespondenceStateIsCreated() {
        when(correspondenceStateRepository.saveAndFlush(any(CorrespondenceStateEntity.class)))
                .thenReturn(buildCorrespondenceStateEntity(REP_ID, CorrespondenceStatus.APPEAL_CC));

        CorrespondenceStatus status = correspondenceStateService
                .createCorrespondenceState(REP_ID, CorrespondenceStatus.APPEAL_CC);

        assertThat(status)
                .isEqualTo(CorrespondenceStatus.APPEAL_CC);
    }

    @Test
    void givenValidParameters_whenUpdateCorrespondenceStateIsInvokedAndRecordExists_thenCorrespondenceStateIsUpdated() {
        when(correspondenceStateRepository.findByRepId(REP_ID))
                .thenReturn(
                        CorrespondenceStateEntity.builder()
                                .repId(REP_ID)
                                .status(CORRESPONDENCE_STATUS)
                                .build()
                );
        when(correspondenceStateRepository.saveAndFlush(any(CorrespondenceStateEntity.class)))
                .thenReturn(buildCorrespondenceStateEntity(REP_ID, CorrespondenceStatus.NONE));

        CorrespondenceStatus status =
                correspondenceStateService.updateCorrespondenceState(REP_ID, CorrespondenceStatus.NONE);

        assertThat(status)
                .isEqualTo(CorrespondenceStatus.NONE);
    }

    @Test
    void givenInvalidRepId_whenUpdateCorrespondenceStatusIsInvoked_thenExceptionIsThrown() {
        when(correspondenceStateRepository.findByRepId(INVALID_REP_ID))
                .thenReturn(null);

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
                .isThrownBy(() -> correspondenceStateService.updateCorrespondenceState(
                        INVALID_REP_ID, CorrespondenceStatus.APPEAL_CC
                ))
                .withMessageContaining("No correspondence state found for repId= 1235");
    }
}

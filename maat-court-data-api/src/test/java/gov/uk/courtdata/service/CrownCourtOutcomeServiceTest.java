package gov.uk.courtdata.service;

import gov.uk.courtdata.common.repository.CrownCourtProcessingRepository;
import gov.uk.courtdata.common.repository.CrownCourtStoredProcedureRepository;
import gov.uk.courtdata.crowncourt.model.UpdateCCOutcome;
import gov.uk.courtdata.crowncourt.model.UpdateSentenceOrder;
import gov.uk.courtdata.crowncourt.service.CrownCourtOutcomeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class CrownCourtOutcomeServiceTest {

    @Mock
    CrownCourtStoredProcedureRepository crownCourtStoredProcedureRepository;
    @Mock
    CrownCourtProcessingRepository crownCourtProcessingRepository;
    @InjectMocks
    private CrownCourtOutcomeService crownCourtOutcomeService;

    @Test
    void givenUpdateCCOutcomeObject_whenUpdateCCOutcomeIsInvoked_thenCCOutcomeIsUpdated() {
        crownCourtOutcomeService.updateCCOutcome(new UpdateCCOutcome());
        verify(crownCourtStoredProcedureRepository).updateCrownCourtOutcome(any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void givenUpdateSentenceOrderObject_whenUpdateCCSentenceOrderDateIsInvoked_thenCCSentenceOrderDateIsUpdated() {
        crownCourtOutcomeService.updateCCSentenceOrderDate(new UpdateSentenceOrder());
        verify(crownCourtProcessingRepository).invokeUpdateSentenceOrderDate(any(), any(), any());
    }

    @Test
    void givenUpdateSentenceOrderObject_whenUpdateAppealCCSentenceOrderDateIsInvoked_thenAppealSentenceOrderDateIsUpdated() {
        crownCourtOutcomeService.updateAppealCCSentenceOrderDate(new UpdateSentenceOrder());
        verify(crownCourtProcessingRepository).invokeUpdateAppealSentenceOrderDate(any(), any(), any(), any());
    }
}
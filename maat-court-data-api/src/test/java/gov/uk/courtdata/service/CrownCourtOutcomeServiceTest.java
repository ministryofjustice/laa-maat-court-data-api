package gov.uk.courtdata.service;

import gov.uk.courtdata.model.UpdateCCOutcome;
import gov.uk.courtdata.repository.CrownCourtStoredProcedureRepository;
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
    @InjectMocks
    private CrownCourtOutcomeService crownCourtOutcomeService;

    @Test
    void givenDateCompletedObject_whenUpdateAppDateCompletedIsInvoked_thenAppDateIsUpdated() {
        crownCourtOutcomeService.update(new UpdateCCOutcome());
        verify(crownCourtStoredProcedureRepository).updateCrownCourtOutcome(any(), any(), any(), any(), any(), any(), any());
    }
}
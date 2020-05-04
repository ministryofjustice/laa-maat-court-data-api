package gov.uk.courtdata.hearing.crowncourt;

import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CrownCourtProcessingImplTest {

    @InjectMocks
    private CrownCourtProcessingImpl crownCourtProcessingImpl;
    @Mock
    private CrownCourtProcessingRepository crownCourtProcessingRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenCCMessageIsReceived_whenCrownCourtProcessingImplIsInvoked_thenCrownCourtProcessingRepositoryIsCalled() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .ccooOutcome("OutCome")
                .build();
        //when
        crownCourtProcessingImpl.execute(hearingDetails);

        //then
        verify(crownCourtProcessingRepository, times(1))
                .invokeCrownCourtOutcomeProcess(hearingDetails.getMaatId(),
                        hearingDetails.getCcooOutcome(),
                        hearingDetails.getBenchWarrantIssuedYn(),
                        hearingDetails.getAppealType(),
                        hearingDetails.getCcImprisioned(),
                        hearingDetails.getCcImprisioned(),
                        hearingDetails.getCrownCourtCode());

    }
}

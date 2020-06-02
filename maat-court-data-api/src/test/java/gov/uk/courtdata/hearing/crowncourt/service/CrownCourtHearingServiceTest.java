package gov.uk.courtdata.hearing.crowncourt.service;

import gov.uk.courtdata.hearing.crowncourt.impl.CrownCourtHearingResultedImpl;
import gov.uk.courtdata.hearing.crowncourt.impl.CrownCourtProcessingImpl;
import gov.uk.courtdata.hearing.crowncourt.validator.CrownCourtValidationProcessor;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
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
public class CrownCourtHearingServiceTest {

    @Mock
    private CrownCourtValidationProcessor crownCourtValidationProcessor;
    @Mock
    private CrownCourtProcessingImpl crownCourtProcessingImpl;
    @Mock
    private CrownCourtHearingResultedImpl crownCourtHearingResultedImpl;
    @InjectMocks
    private CrownCourtHearingService crownCourtHearingService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenHearingIsReceived_whenCCOutcomeIsAvailable_thenCrownCourtOutComeIsProcessed() {

        //given
        CCOutComeData ccOutComeData = CCOutComeData.builder().ccooOutcome("CONVICTED").build();
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .ccOutComeData(ccOutComeData)
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);

        //then
        verify(crownCourtValidationProcessor, times(1)).validate(hearingDetails);
        verify(crownCourtProcessingImpl, times(1)).execute(hearingDetails);
        verify(crownCourtHearingResultedImpl, times(1)).execute(hearingDetails);

    }

    @Test
    public void givenHearingIsReceived_whenCCOutcomeIsNOTAvailable_thenWorkQueueProcessingIsCompleted() {

        //given
        CCOutComeData ccOutComeData = CCOutComeData.builder().build();
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .ccOutComeData(ccOutComeData)
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);

        //then
        verify(crownCourtValidationProcessor, times(0)).validate(hearingDetails);
        verify(crownCourtProcessingImpl, times(0)).execute(hearingDetails);
        verify(crownCourtHearingResultedImpl, times(1)).execute(hearingDetails);

    }

}

package gov.uk.courtdata.hearing.crowncourt.service;

import gov.uk.courtdata.hearing.crowncourt.impl.CrownCourtProcessingImpl;
import gov.uk.courtdata.hearing.crowncourt.impl.OffenceHelper;
import gov.uk.courtdata.hearing.crowncourt.validator.CrownCourtValidationProcessor;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CrownCourtHearingServiceTest {

    @Mock
    private CrownCourtValidationProcessor crownCourtValidationProcessor;
    @Mock
    private CrownCourtProcessingImpl crownCourtProcessingImpl;

    @Mock
    private HearingResultedImpl hearingResultedImpl;

    @InjectMocks
    private CrownCourtHearingService crownCourtHearingService;

    @Mock
    private OffenceHelper offenceHelper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenHearingIsReceived_whenCCOutcomeIsNull_thenWorkQueueProcessingIsCompleted() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);

        //then
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
        verify(crownCourtProcessingImpl, atLeastOnce()).execute(hearingDetails);
    }
}

package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.hearing.crowncourt.CrownCourtProcessingImpl;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.validator.CrownCourtValidationProcessor;
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
public class HearingResultedServiceTest {

    @InjectMocks
    private HearingResultedService hearingResultedService;

    @Mock
    private CrownCourtValidationProcessor hearingValidationProcessor;
    @Mock
    private CrownCourtProcessingImpl hearingResultedImpl;


    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenACaseDetail_whenHearingServiceIsInvoked_thenMessageIsPublished() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder().jurisdictionType(JurisdictionType.CROWN).build();
        //when
        hearingResultedService.process(hearingDetails);
        //then
        verify(hearingResultedImpl, times(1)).execute(hearingDetails);

    }
}

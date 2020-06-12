package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.hearing.crowncourt.service.CrownCourtHearingService;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
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
    private HearingValidationProcessor hearingValidationProcessor;

    @Mock
    private HearingResultedImpl hearingResultedImpl;
    @Mock
    private CrownCourtHearingService crownCourtHearingService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenACaseDetail_whenCrownCourtHearingServiceIsReceived_thenCCImplIsInvoked() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder().jurisdictionType(JurisdictionType.CROWN).build();
        //when
        hearingResultedService.execute(hearingDetails);
        //then
        verify(crownCourtHearingService, times(1)).execute(hearingDetails);



    }

    @Test
    public void givenACaseDetail_whenMAGGCourtHearingServiceIsReceived_thenMagsCourtProcessingInvoked() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder().jurisdictionType(JurisdictionType.MAGISTRATES).build();
        //when
        hearingResultedService.execute(hearingDetails);
        //then
        verify(hearingValidationProcessor, times(1)).validate(hearingDetails);
        verify(hearingResultedImpl, times(1)).execute(hearingDetails);

    }



}

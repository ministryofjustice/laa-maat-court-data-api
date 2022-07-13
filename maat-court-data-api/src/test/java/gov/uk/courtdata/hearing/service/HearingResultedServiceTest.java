package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.enums.FunctionType;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.processor.CourtApplicationsPreProcessor;
import gov.uk.courtdata.hearing.processor.WQHearingProcessor;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HearingResultedServiceTest {

    @InjectMocks
    private HearingResultedService hearingResultedService;

    @Mock
    private HearingValidationProcessor hearingValidationProcessor;

    @Mock
    private HearingResultedImpl hearingResultedImpl;

    @Mock
    private CourtApplicationsPreProcessor courtApplicationsPreProcessor;

    @Mock
    private WQHearingProcessor wqHearingProcessor;

    @Test
    public void givenAMagCourtNotification_whenMaatNotLocked_thenMagsCourtProcessingInvoked() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder().maatId(34).jurisdictionType(JurisdictionType.MAGISTRATES).build();

        //when
        doNothing().when(hearingResultedImpl).execute(hearingDetails);

        hearingResultedService.execute(hearingDetails);
        //then
        verify(hearingValidationProcessor).validate(hearingDetails);
        verify(hearingResultedImpl).execute(hearingDetails);
        verify(wqHearingProcessor).process(hearingDetails);
    }

    @Test
    public void givenApplicationNotification_whenApplicationType_thenApplicationPreProcessingInvoked() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(34)
                .functionType(FunctionType.APPLICATION)
                .jurisdictionType(JurisdictionType.MAGISTRATES)
                .build();

        //when
        doNothing().when(courtApplicationsPreProcessor).process(hearingDetails);

        hearingResultedService.execute(hearingDetails);
        //then
        verify(courtApplicationsPreProcessor).process(hearingDetails);
        verify(hearingValidationProcessor).validate(hearingDetails);
        verify(hearingResultedImpl).execute(hearingDetails);
        verify(wqHearingProcessor).process(hearingDetails);
    }
}

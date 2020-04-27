package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class HearingResultedServiceTest {

    @InjectMocks
    private HearingResultedService hearingResultedService;

    @Mock
    private HearingValidationProcessor hearingValidationProcessor;
    @Mock
    private HearingResultedImpl hearingResultedImpl;


    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenACaseDetail_whenHearingServiceIsInvoked_thenMessageIsPublished() {

        //given
        CaseDetails caseDetails = CaseDetails.builder().build();
        CourtDataDTO courtDataDTO = CourtDataDTO.builder().build();

        //when
        when(hearingValidationProcessor.validate(caseDetails)).thenReturn(courtDataDTO);
        hearingResultedService.process(caseDetails);

        //then
    //    verify(hearingResultedImpl, times(1)).execute(courtDataDTO);

    }
}

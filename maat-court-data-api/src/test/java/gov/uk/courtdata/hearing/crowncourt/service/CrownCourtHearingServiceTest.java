package gov.uk.courtdata.hearing.crowncourt.service;

import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.hearing.crowncourt.impl.CrownCourtProcessHelper;
import gov.uk.courtdata.hearing.crowncourt.impl.CrownCourtProcessingImpl;
import gov.uk.courtdata.hearing.crowncourt.validator.CrownCourtValidationProcessor;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    private CrownCourtProcessHelper crownCourtProcessHelper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenHearingIsReceived_whenCCOutcomeIsAvailable_thenCrownCourtOutComeIsProcessed() {

        //given
        HearingResulted hearingDetails = getHearingResulted();

        //when
        crownCourtHearingService.execute(hearingDetails);

        //then
        verify(crownCourtValidationProcessor, times(0)).validate(hearingDetails);
        verify(crownCourtProcessingImpl, times(0)).execute(hearingDetails);
        verify(hearingResultedImpl,atLeastOnce()).execute(hearingDetails);
        verify(crownCourtProcessHelper, atLeastOnce()).isCaseConcluded(hearingDetails);

    }

    @Test
    public void givenHearingIsReceived_whenCaseConcluded_thenCrownCourtOutComeIsProcessed() {

        //given
        HearingResulted hearingDetails = getHearingResulted();

        when(crownCourtProcessHelper.isCaseConcluded(hearingDetails)).thenReturn(true);

        //when
        crownCourtHearingService.execute(hearingDetails);

        //then
        verify(crownCourtValidationProcessor, atLeastOnce()).validate(hearingDetails);
        verify(crownCourtProcessingImpl, atLeastOnce()).execute(hearingDetails);
        verify(hearingResultedImpl,atLeastOnce()).execute(hearingDetails);
        verify(crownCourtProcessHelper, atLeastOnce()).isCaseConcluded(hearingDetails);

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
        verify(crownCourtProcessHelper, times(0)).isCaseConcluded(hearingDetails);
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);

    }

    @Test
    public void givenHearingIsReceived_whenCCOutcomeIsNull_thenWorkQueueProcessingIsCompleted() {

        //given

        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .ccOutComeData(null)
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);

        //then
        verify(crownCourtValidationProcessor, times(0)).validate(hearingDetails);
        verify(crownCourtProcessingImpl, times(0)).execute(hearingDetails);
        verify(crownCourtProcessHelper, times(0)).isCaseConcluded(hearingDetails);
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);

    }

    @Test
    public void givenHearingIsReceived_whenCCOutcomeIsEmptyString_thenWorkQueueProcessingIsCompleted() {

        //given

        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .ccOutComeData(CCOutComeData.builder().ccooOutcome("").build())
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);

        //then
        verify(crownCourtValidationProcessor, times(0)).validate(hearingDetails);
        verify(crownCourtProcessingImpl, times(0)).execute(hearingDetails);
        verify(crownCourtProcessHelper, times(0)).isCaseConcluded(hearingDetails);
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
    }



    private HearingResulted getHearingResulted() {
        CCOutComeData ccOutComeData = CCOutComeData.builder().ccooOutcome("CONVICTED").build();
        List<Result> resultList = Arrays.asList(
                Result.builder().resultCode("3030").build(),
                Result.builder().resultCode("3031").build(),
                Result.builder().resultCode("3040").build()
        );

        return HearingResulted.builder()
                .maatId(12345)
                .ccOutComeData(ccOutComeData)
                .defendant(Defendant.builder()
                        .offences(Collections.singletonList(Offence
                                .builder()
                                .results(resultList).build()))
                        .build()
                ).build();
    }

}

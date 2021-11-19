package gov.uk.courtdata.hearing.crowncourt.service;

import gov.uk.courtdata.hearing.crowncourt.impl.CrownCourtProcessingImpl;
import gov.uk.courtdata.hearing.crowncourt.impl.OffenceHelper;
import gov.uk.courtdata.hearing.crowncourt.validator.CrownCourtValidationProcessor;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Verdict;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    }



    @Test
    public void givenHearingIsReceived_whenAllOffencesHaveVerdict_thenOutcomeProcessingIsTriggered() {



        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(123456)
                .defendant(getDefendant())
                .ccOutComeData(CCOutComeData.builder().ccooOutcome("CONVICTED").build())
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
        verify(crownCourtProcessingImpl, atLeastOnce()).execute(hearingDetails);
        assertThat(hearingDetails.getCcOutComeData().getCcooOutcome()).isEqualTo("CONVICTED");
    }

    @Test
    public void givenHearingIsReceived_whenPartOfOffencesHaveVerdict_thenOutcomeProcessingIsNOTTriggered() {



        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(123456)
                .defendant(getNonConcludedDefendant())
                .ccOutComeData(CCOutComeData.builder().ccooOutcome("CONVICTED").build())
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
        verify(crownCourtProcessingImpl, never()).execute(hearingDetails);

    }




    @Test
    public void givenHearingIsReceived_whenNoOffenceAvailable_thenReturnNull() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .prosecutionConcluded(true)
                .ccOutComeData(CCOutComeData.builder().build())
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);

        //then
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
        assertThat(hearingDetails.getCcOutComeData().getCcooOutcome()).isNull();
    }

    @Test
    public void givenHearingIsReceived_whenProsConIsFalseAndCcoIsNull_thenProcessRequest() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .prosecutionConcluded(false)
                .ccOutComeData(null)
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);

        //then
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
        verify(crownCourtValidationProcessor, atLeast(0)).validate(hearingDetails);
        verify(crownCourtProcessingImpl, atLeast(0)).execute(hearingDetails);

    }



    private Defendant getDefendant() {

        return Defendant
                .builder()
                .offences(
                        Arrays.asList(
                                Offence
                                        .builder()
                                        .offenceId("12121")
                                        .verdict(Verdict.builder().categoryType("GUILTY").build())
                                        .build(),
                                Offence
                                        .builder()
                                        .offenceId("1221")
                                        .verdict(Verdict.builder().categoryType("GUILTY").build())
                                        .build()
                        )
                ).build();
    }


    private Defendant getNonConcludedDefendant() {

        return Defendant
                .builder()
                .offences(
                        Arrays.asList(
                                Offence
                                        .builder()
                                        .offenceId("12121")
                                        .verdict(Verdict.builder().categoryType("GUILTY").build())
                                        .build(),
                                Offence
                                        .builder()
                                        .offenceId("1221")
                                        .build()
                        )
                ).build();
    }
}

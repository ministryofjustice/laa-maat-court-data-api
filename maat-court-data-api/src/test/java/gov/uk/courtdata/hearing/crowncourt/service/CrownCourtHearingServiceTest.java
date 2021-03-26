package gov.uk.courtdata.hearing.crowncourt.service;

import gov.uk.courtdata.enums.CrownCourtTrialOutcome;
import gov.uk.courtdata.hearing.crowncourt.impl.CrownCourtProcessingImpl;
import gov.uk.courtdata.hearing.crowncourt.impl.OffenceHelper;
import gov.uk.courtdata.hearing.crowncourt.validator.CrownCourtValidationProcessor;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Plea;
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
                .ccOutComeData(null)
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);

        //then
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
    }

    @Test(expected = NullPointerException.class)
    public void givenHearingIsReceived_whenProsFlagTrue_thenWorkQueueProcessingCrownOutcome() {

        //given

        HearingResulted hearingDetails = HearingResulted.builder()
                .prosecutionConcluded(true)
                .maatId(123456)
                .defendant(getDefendant())
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
    }

    @Test
    public void givenHearingIsReceived_whenProsFlagTrue_thenProcessingOutcomeAsConvicted() {

        //given
        when(offenceHelper.getOffences(anyInt()))
                .thenReturn(
                        Arrays.asList(
                                Offence.builder().plea(Plea.builder().pleaValue("GUILTY").build()).build(),
                                Offence.builder().plea(Plea.builder().pleaValue("GUILTY_LESSER_OFFENCE_NAMELY").build()).build(),
                                Offence.builder().verdict(Verdict.builder().categoryType("GUILTY_CONVICTED").build()).build()
                        )
                );

        HearingResulted hearingDetails = HearingResulted.builder()
                .prosecutionConcluded(true)
                .maatId(123456)
                .defendant(getDefendant())
                .ccOutComeData(CCOutComeData.builder().build())
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
        assertThat(hearingDetails.getCcOutComeData().getCcOutcome()).isEqualTo("CONVICTED");
    }

    @Test
    public void givenHearingIsReceived_whenProsFlagTrue_thenProcessingOutcomeAsPartConv() {

        //given
        when(offenceHelper.getOffences(anyInt()))
                .thenReturn(
                        Arrays.asList(
                                Offence.builder().plea(Plea.builder().pleaValue("GUILTY").build()).build(),
                                Offence.builder().plea(Plea.builder().pleaValue("NOT_GUILTY").build()).build(),
                                Offence.builder().verdict(Verdict.builder().categoryType("RANDOM").build()).build()
                        )
                );

        HearingResulted hearingDetails = HearingResulted.builder()
                .prosecutionConcluded(true)
                .maatId(123456)
                .defendant(getDefendant())
                .ccOutComeData(CCOutComeData.builder().build())
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
        assertThat(hearingDetails.getCcOutComeData().getCcOutcome()).isEqualTo(CrownCourtTrialOutcome.PART_CONVICTED.getValue());
    }


    @Test
    public void givenHearingIsReceived_whenProsFlagTrue_thenProcessingOutcomeAsAcquitted() {

        when(offenceHelper.getOffences(anyInt()))
                .thenReturn(
                        Arrays.asList(
                                Offence.builder().plea(Plea.builder().pleaValue("NOT_GUILTY").build()).build(),
                                Offence.builder().plea(Plea.builder().pleaValue("NOT_GUILTY").build()).build(),
                                Offence.builder().verdict(Verdict.builder().categoryType("RANDOM").build()).build()
                        )
                );


        HearingResulted hearingDetails = HearingResulted.builder()
                .prosecutionConcluded(true)
                .maatId(123456)
                .defendant(getDefendant())
                .ccOutComeData(CCOutComeData.builder().caseEndDate("2012-12-12").ccOutcome("Convicted").build())
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);

        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
        assertThat(hearingDetails.getCcOutComeData().getCcOutcome()).isEqualTo("AQUITTED");
    }

    /**
     * To maintain the existing func as the flag is false and keeping the outcome as it is.
     */
    @Test
    public void givenHearingIsReceived_whenProsFlagFalse_thenProcessingOutcomeAsAcquitted() {

        HearingResulted hearingDetails = HearingResulted.builder()
                .prosecutionConcluded(false)
                .maatId(123456)
                .defendant(getDefendant())
                .ccOutComeData(CCOutComeData.builder().caseEndDate("2012-12-12").ccOutcome("CONVICTED").build())
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);

        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
        assertThat(hearingDetails.getCcOutComeData().getCcOutcome()).isEqualTo("CONVICTED");
    }


    /**
     * when the flag is false then keep the data as it is for CC outcome.
     */
    @Test
    public void givenHearingIsReceived_whenProsFlagFalse_thenProcessingOutcomeAsIs() {

        HearingResulted hearingDetails = HearingResulted.builder()
                .prosecutionConcluded(false)
                .maatId(123456)
                .defendant(getDefendant())
                .ccOutComeData(CCOutComeData.builder().caseEndDate("2012-12-12")
                        .ccOutcome("CONVICTED")
                        .caseEndDate("2021-01-01")
                        .appealType("TYPE")
                        .build())
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);

        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
        assertThat(hearingDetails.getCcOutComeData().getCcOutcome()).isEqualTo("CONVICTED");
        assertThat(hearingDetails.getCcOutComeData().getCaseEndDate()).isEqualTo("2021-01-01");
        assertThat(hearingDetails.getCcOutComeData().getAppealType()).isEqualTo("TYPE");
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
        assertThat(hearingDetails.getCcOutComeData().getCcOutcome()).isEmpty();
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

    /**
     * When prosecution Concluded flag is true
     * CC Outcome is null and offences are null too
     * This is not a valid use case and there is data issue, NPE.
     */
    @Test(expected = NullPointerException.class)
    public void givenHearingIsReceived_whenProsConIsTrueAndCcoIsNull_thenProcessRequest() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .prosecutionConcluded(true)
                .ccOutComeData(null)
                .build();

        //when
        crownCourtHearingService.execute(hearingDetails);

        //then
        verify(hearingResultedImpl, atLeastOnce()).execute(hearingDetails);
        verify(crownCourtValidationProcessor, atLeast(0)).validate(hearingDetails);
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
                                        .plea(Plea.builder().pleaValue("GUILTY").build())
                                        .build()
                        )
                ).build();
    }

}

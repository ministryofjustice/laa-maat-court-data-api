package gov.uk.courtdata.hearing.crowncourt.impl;

import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Plea;
import gov.uk.courtdata.model.Verdict;
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
public class CalculateCrownCourtOutComeTest {

    @InjectMocks
    private CalculateCrownCourtOutCome calculateCrownCourtOutCome;

    @Mock
    private OffenceHelper offenceHelper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
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
                .build();

        //when
        String response = calculateCrownCourtOutCome.calculate(hearingDetails);

        //then
        verify(offenceHelper, atLeastOnce()).getOffences(123456);
        assertThat(response).isEqualTo("CONVICTED");
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
                .build();

        //when
        String response = calculateCrownCourtOutCome.calculate(hearingDetails);

        assertThat(response).isEqualTo("AQUITTED");
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
                .build();

        //when
        String response = calculateCrownCourtOutCome.calculate(hearingDetails);

        assertThat(response).isEqualTo("PART CONVICTED");
    }



    @Test
    public void givenHearingIsReceived_whenNoOffenceAvailable_thenReturnNull() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .prosecutionConcluded(true)
                .build();

        //when
        String response = calculateCrownCourtOutCome.calculate(hearingDetails);

        //then
        assertThat(response).isEmpty();
    }



    @Test
    public void givenHearingIsReceived_whenProsConIsFalse_thenProcessRequest() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .prosecutionConcluded(false)
                .build();

        //when
        String response = calculateCrownCourtOutCome.calculate(hearingDetails);

        //then
        assertThat(response).isNull();
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
package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.hearing.crowncourt.impl.OffenceHelper;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Plea;
import gov.uk.courtdata.model.Verdict;
import gov.uk.courtdata.model.crowncourt.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.CalculateCrownCourtOutcome;
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
public class CalculateCrownCourtOutcomeTest {

    @InjectMocks
    private CalculateCrownCourtOutcome calculateCrownCourtOutcome;

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

        ProsecutionConcluded prosecutionConcluded = getProsecutionConcluded();

        //when
        String response = calculateCrownCourtOutcome.calculate(prosecutionConcluded);

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


        ProsecutionConcluded prosecutionConcluded = getProsecutionConcluded();

        //when
        String response = calculateCrownCourtOutcome.calculate(prosecutionConcluded);

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

        ProsecutionConcluded prosecutionConcluded = getProsecutionConcluded();


        //when
        String response = calculateCrownCourtOutcome.calculate(prosecutionConcluded);

        assertThat(response).isEqualTo("PART CONVICTED");
    }



    @Test
    public void givenHearingIsReceived_whenNoOffenceAvailable_thenReturnNull() {

        //given
        ProsecutionConcluded prosecutionConcluded = getProsecutionConcluded();

        //when
        String response = calculateCrownCourtOutcome.calculate(prosecutionConcluded);

        //then
        assertThat(response).isEmpty();
    }



    @Test
    public void givenHearingIsReceived_whenProsConIsFalse_thenProcessRequest() {

        //given
        ProsecutionConcluded prosecutionConcluded = getProsecutionConcluded();
        prosecutionConcluded.setConcluded(false);


        //when
        String response = calculateCrownCourtOutcome.calculate(prosecutionConcluded);

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

    private ProsecutionConcluded getProsecutionConcluded() {
        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .concluded(true)
                .maatId(123456)
                .build();
        return prosecutionConcluded;
    }
}
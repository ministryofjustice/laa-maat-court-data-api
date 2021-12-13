package gov.uk.courtdata.prosecutionconcluded.helper;

import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;

import org.mockito.MockitoAnnotations;


import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CalculateOutcomeHelperTest {

    @InjectMocks
    private CalculateOutcomeHelper calculateOutcomeHelper;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void givenMessageIsReceived_whenPleaAndVerdictIsAvailable_thenReturnOutcomeAsPartConvicted() {

        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .concluded(true)
                .maatId(123456)
                .offenceSummaryList(Arrays.asList(
                        OffenceSummary.builder()
                                .offenceCode("1212")
                                .verdict(Verdict.builder().verdictType(VerdictType.builder().categoryType("GUILTY").build()).verdictDate("2021-11-12").build())
                                .plea(Plea.builder().value("NOT_GUILTY").pleaDate("2021-11-12").build())
                                .build()
                ))
                .build();

        String res = calculateOutcomeHelper.calculate(prosecutionConcluded);

        assertThat(res).isEqualTo("CONVICTED");
    }

    @Test
    public void givenMessageIsReceived_whenMultipleOffenceIsAvailable_thenReturnOutcomeAsPartConvicted() {

        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .concluded(true)
                .maatId(123456)
                .offenceSummaryList(
                        Arrays.asList(
                                OffenceSummary.builder()
                                .verdict(Verdict.builder().verdictType(VerdictType.builder().categoryType("GUILTY").build()).verdictDate("2021-11-12").build())
                                .plea(Plea.builder().value("NOT_GUILTY").pleaDate("2021-11-12").build())
                                .build(),
                                OffenceSummary.builder()
                                        .verdict(Verdict.builder().verdictType(VerdictType.builder().categoryType("NOT GUILTY").build()).verdictDate("2021-11-12").build())
                                        .plea(Plea.builder().value("GUILTY").pleaDate("2021-11-12").build())
                                        .build()
                ))
                .build();

        String res = calculateOutcomeHelper.calculate(prosecutionConcluded);

        assertThat(res).isEqualTo("PART CONVICTED");
    }

    @Test
    public void givenMessageIsReceived_whenVerdictIsGuilty_thenReturnOutcomeAsConvicted() {

        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .concluded(true)
                .maatId(123456)
                .offenceSummaryList(Arrays.asList(
                        OffenceSummary.builder()
                                .offenceCode("1212")
                                .verdict(Verdict.builder().verdictType(VerdictType.builder().categoryType("GUILTY").build()).verdictDate("2021-11-12").build())
                                .build()
                ))
                .build();

        String res = calculateOutcomeHelper.calculate(prosecutionConcluded);

        assertThat(res).isEqualTo("CONVICTED");
    }


    @Test
    public void givenMessageIsReceived_whenVerdictIsNotGuilty_thenReturnOutcomeAsAquitted() {

        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .concluded(true)
                .maatId(123456)
                .offenceSummaryList(Arrays.asList(
                        OffenceSummary.builder()
                                .offenceCode("1212")
                                .verdict(Verdict.builder().verdictType(VerdictType.builder().categoryType("NOT_GUILTY").build()).verdictDate("2021-11-12").build())
                                .build()
                ))
                .build();

        String res = calculateOutcomeHelper.calculate(prosecutionConcluded);

        assertThat(res).isEqualTo("AQUITTED");
    }


    @Test
    public void givenMessageIsReceived_whenPleaIsNotGuilty_thenReturnOutcomeAsAquitted() {

        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .concluded(true)
                .maatId(123456)
                .offenceSummaryList(Arrays.asList(
                        OffenceSummary.builder()
                                .offenceCode("1212")
                                .plea(Plea.builder().value("NOT_GUILTY").pleaDate("2021-11-12").build())
                                .build()
                ))
                .build();

        String res = calculateOutcomeHelper.calculate(prosecutionConcluded);

        assertThat(res).isEqualTo("AQUITTED");
    }




    @Test
    public void givenMessageIsReceived_whenPleaIsGuilty_thenReturnOutcomeAsConvicted() {

        ProsecutionConcluded prosecutionConcluded = getProsecutionConcluded();

        String res = calculateOutcomeHelper.calculate(prosecutionConcluded);

        assertThat(res).isEqualTo("CONVICTED");

    }

    @Test (expected = NullPointerException.class)
    public void givenMessageIsReceived_whenOffenceSummeryIsNull_thenThrowException() {

        calculateOutcomeHelper.calculate(ProsecutionConcluded.builder()
                .concluded(true)
                .build()
        );
    }

    private ProsecutionConcluded getProsecutionConcluded() {
        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .concluded(true)
                .maatId(123456)
                .offenceSummaryList(Arrays.asList(
                        OffenceSummary.builder()
                                .offenceCode("1212")
                                .plea(Plea.builder().value("GUILTY").pleaDate("2021-12-12").build())
                                .build()
                ))
                .build();
        return prosecutionConcluded;
    }
}
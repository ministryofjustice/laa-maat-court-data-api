package gov.uk.courtdata.prosecutionconcluded.helper;

import gov.uk.courtdata.prosecutionconcluded.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class CalculateOutcomeHelperTest {

    @InjectMocks
    private CalculateOutcomeHelper calculateOutcomeHelper;


    @Test
    public void givenMessageIsReceived_whenPleaAndVerdictIsAvailable_thenReturnOutcomeAsPartConvicted() {

        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .isConcluded(true)
                .maatId(123456)
                .offenceSummary(Arrays.asList(
                        OffenceSummary.builder()
                                .offenceCode("1212")
                                .verdict(Verdict.builder().verdictType(VerdictType.builder().categoryType("GUILTY").build()).verdictDate("2021-11-12").build())
                                .plea(Plea.builder().value("NOT_GUILTY").pleaDate("2021-11-12").build())
                                .build()
                ))
                .build();

        String res = calculateOutcomeHelper.calculate(prosecutionConcluded.getOffenceSummary());

        assertThat(res).isEqualTo("CONVICTED");
    }


    @Test
    public void givenMessageIsReceived_whenMultipleOffenceIsAvailable_thenReturnOutcomeAsPartConvicted() {

        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .isConcluded(true)
                .maatId(123456)
                .offenceSummary(
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

        String res = calculateOutcomeHelper.calculate(prosecutionConcluded.getOffenceSummary());

        assertThat(res).isEqualTo("PART CONVICTED");
    }

    @Test
    public void givenMessageIsReceived_whenVerdictIsGuilty_thenReturnOutcomeAsConvicted() {

        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .isConcluded(true)
                .maatId(123456)
                .offenceSummary(Arrays.asList(
                        OffenceSummary.builder()
                                .offenceCode("1212")
                                .verdict(Verdict.builder().verdictType(VerdictType.builder().categoryType("GUILTY").build()).verdictDate("2021-11-12").build())
                                .build()
                ))
                .build();

        String res = calculateOutcomeHelper.calculate(prosecutionConcluded.getOffenceSummary());

        assertThat(res).isEqualTo("CONVICTED");
    }


    @Test
    public void givenMessageIsReceived_whenVerdictIsNotGuilty_thenReturnOutcomeAsAquitted() {

        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .isConcluded(true)
                .maatId(123456)
                .offenceSummary(Arrays.asList(
                        OffenceSummary.builder()
                                .offenceCode("1212")
                                .verdict(Verdict.builder().verdictType(VerdictType.builder().categoryType("NOT_GUILTY").build()).verdictDate("2021-11-12").build())
                                .build()
                ))
                .build();

        String res = calculateOutcomeHelper.calculate(prosecutionConcluded.getOffenceSummary());

        assertThat(res).isEqualTo("AQUITTED");
    }


    @Test
    public void givenMessageIsReceived_whenPleaIsNotGuilty_thenReturnOutcomeAsAquitted() {

        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .isConcluded(true)
                .maatId(123456)
                .offenceSummary(Arrays.asList(
                        OffenceSummary.builder()
                                .offenceCode("1212")
                                .plea(Plea.builder().value("NOT_GUILTY").pleaDate("2021-11-12").build())
                                .build()
                ))
                .build();

        String res = calculateOutcomeHelper.calculate(prosecutionConcluded.getOffenceSummary());

        assertThat(res).isEqualTo("AQUITTED");
    }




    @Test
    public void givenMessageIsReceived_whenPleaIsGuilty_thenReturnOutcomeAsConvicted() {

        ProsecutionConcluded prosecutionConcluded = getProsecutionConcluded();

        String res = calculateOutcomeHelper.calculate(prosecutionConcluded.getOffenceSummary());

        assertThat(res).isEqualTo("CONVICTED");

    }



    private ProsecutionConcluded getProsecutionConcluded() {
        ProsecutionConcluded prosecutionConcluded = ProsecutionConcluded.builder()
                .isConcluded(true)
                .maatId(123456)
                .offenceSummary(Arrays.asList(
                        OffenceSummary.builder()
                                .offenceCode("1212")
                                .plea(Plea.builder().value("GUILTY").pleaDate("2021-12-12").build())
                                .build()
                ))
                .build();
        return prosecutionConcluded;
    }
}
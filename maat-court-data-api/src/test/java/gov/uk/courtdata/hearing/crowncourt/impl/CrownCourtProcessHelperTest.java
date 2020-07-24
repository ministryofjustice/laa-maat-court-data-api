package gov.uk.courtdata.hearing.crowncourt.impl;


import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.enums.CCTrialOutcome;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.XLATResultRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrownCourtProcessHelperTest {

    @InjectMocks
    private CrownCourtProcessHelper crownCourtProcessHelper;
    @Mock
    private XLATResultRepository xlatResultRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenCCOutcomeIsNotConvicted_ImprisonmentFlagIsNotSet() {

        HearingResulted hearing = hearingResultedWith();

        String isImp = crownCourtProcessHelper.isImprisoned(hearing, "ACQUITTED");

        assertAll("Imprisoned",
                () -> assertNull(isImp));

    }

    @Test
    public void whenCCOutcomeIsConvicted_HearingResulted_WithNonImprisonmentResult_FlagIsNo() {

        when(xlatResultRepository.fetchResultCodesForCCImprisonment())
                .thenReturn(buildCCImprisonmentResultEntities());

        HearingResulted hearing = hearingResultedWith("3030");

        String imprisoned = crownCourtProcessHelper.isImprisoned(hearing,
                CCTrialOutcome.CONVICTED.getValue());

        assertAll("Imprisoned",
                () -> assertNotNull(imprisoned),
                () -> assertEquals("N", imprisoned));

    }

    @Test
    public void whenCCOutcomeIsPartConvicted_HearingResulted_WithImprisonmentResult_FlagIsYes() {


        when(xlatResultRepository.fetchResultCodesForCCImprisonment())
                .thenReturn(buildCCImprisonmentResultEntities());

        HearingResulted hearing = hearingResultedWith("1002");

        String imprisoned = crownCourtProcessHelper.isImprisoned(hearing,
                CCTrialOutcome.PART_CONVICTED.getValue());

        assertAll("Imprisoned",
                () -> assertNotNull(imprisoned),
                () -> assertEquals("Y", imprisoned));

    }

    @Test
    public void whenCCOutcomeIsConvicted_HearingResulted_WithImprisonmentResult_FlagIsYes() {


        when(xlatResultRepository.fetchResultCodesForCCImprisonment())
                .thenReturn(buildCCImprisonmentResultEntities());

        HearingResulted hearing = hearingResultedWith("1002");

        String imprisoned = crownCourtProcessHelper.isImprisoned(hearing,
                CCTrialOutcome.CONVICTED.getValue());

        assertAll("Imprisoned",
                () -> assertNotNull(imprisoned),
                () -> assertEquals("Y", imprisoned));

    }

    @Test
    public void whenCCOutcomeIsConvicted_AnyOneOffence_WithImprisonmentResult_FlagIsYes() {


        when(xlatResultRepository.fetchResultCodesForCCImprisonment())
                .thenReturn(buildCCImprisonmentResultEntities());

        HearingResulted hearing = hearingResultedWith("1002", "1070", "1111");

        String imprisoned = crownCourtProcessHelper.isImprisoned(hearing,
                CCTrialOutcome.CONVICTED.getValue());

        assertAll("Imprisoned",
                () -> assertNotNull(imprisoned),
                () -> assertEquals("Y", imprisoned));

    }

    @Test
    public void whenCCOutcomeIsConvicted_NoOffence_WithImprisonmentResult_FlagIsNo() {


        when(xlatResultRepository.fetchResultCodesForCCImprisonment())
                .thenReturn(buildCCImprisonmentResultEntities());

        HearingResulted hearing = hearingResultedWith("1111", "2222", "3333");

        String imprisoned = crownCourtProcessHelper.isImprisoned(hearing,
                CCTrialOutcome.CONVICTED.getValue());

        assertAll("Imprisoned",
                () -> assertNotNull(imprisoned),
                () -> assertEquals("N", imprisoned));

    }

    private HearingResulted hearingResultedWith(String... resultCodes) {

        List<Offence> offenceList = new ArrayList<>();

        Arrays.asList(resultCodes).forEach(result -> {

            offenceList.add(Offence.builder().offenceCode("CJ01502").results(
                    Arrays.asList(Result.builder().resultCode(result).build())
            ).build());
        });

        return HearingResulted.builder().defendant(
                Defendant.builder().offences(offenceList).build()
        ).build();
    }


    private List<String> imprisonmentResultCodes() {
        return Arrays.asList("1002", "1024", "1003", "1007", "1016", "1002", "1081");
    }

    private List<XLATResultEntity> buildCCImprisonmentResultEntities() {

        List<XLATResultEntity> resultList = new ArrayList<>();

        imprisonmentResultCodes().stream().forEach(res -> {
            resultList.add(XLATResultEntity.builder().cjsResultCode(1002).build());
        });

        return resultList;
    }

}

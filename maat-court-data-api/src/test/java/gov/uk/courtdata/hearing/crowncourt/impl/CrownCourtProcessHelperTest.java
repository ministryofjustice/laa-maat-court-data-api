package gov.uk.courtdata.hearing.crowncourt.impl;


import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.enums.CrownCourtTrialOutcome;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.XLATResultRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static gov.uk.courtdata.enums.CrownCourtAppealOutcome.UNSUCCESSFUL;
import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.CONVICTED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CrownCourtProcessHelperTest {

    @Rule
    public ExpectedException thrown = none();
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
                CONVICTED.getValue());

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
                CrownCourtTrialOutcome.PART_CONVICTED.getValue());

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
                CONVICTED.getValue());

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
                CONVICTED.getValue());

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
                CONVICTED.getValue());

        assertAll("Imprisoned",
                () -> assertNotNull(imprisoned),
                () -> assertEquals("N", imprisoned));

    }

    @Test
    public void givenHearingResulted_whenCrownCourtProcessorIsInvoked_thenProcessThenSetFlagToY() {

        //given
        List<XLATResultEntity> resultEntityList = Arrays.asList(
                XLATResultEntity.builder().ccBenchWarrant("Y").cjsResultCode(3030).build(),
                XLATResultEntity.builder().ccBenchWarrant("Y").cjsResultCode(3032).build(),
                XLATResultEntity.builder().ccBenchWarrant("Y").cjsResultCode(3033).build()
        );

        //when
        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(resultEntityList);

        String status = crownCourtProcessHelper.isBenchWarrantIssued(getHearingResulted(CONVICTED.getValue()));

        //then
        verify(xlatResultRepository).findByCjsResultCodeIn();
        assertThat(status).isEqualTo("Y");
    }

    @Test
    public void givenHearingResulted_whenResultCodeIsNull_thenSetFlagToN() {
        //when
        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(null);
        thrown.expect(NullPointerException.class);
        String status = crownCourtProcessHelper.isBenchWarrantIssued(getHearingResulted(CONVICTED.getValue()));

        //then
        verify(xlatResultRepository).findByCjsResultCodeIn();

    }

    @Test
    public void givenHearingResulted_whenNotTrial_thenSetFlagToNull() {

        String status = crownCourtProcessHelper.isBenchWarrantIssued(getHearingResulted(UNSUCCESSFUL.getValue()));

        assertNull(status);

    }

    @Test
    public void givenHearingResulted_whenResultCodeIsEmpty_thenSetFlagToN() {
        //when
        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(
                Collections.singletonList(XLATResultEntity.builder().ccBenchWarrant("Y").cjsResultCode(3033).build()));

        String status = crownCourtProcessHelper.isBenchWarrantIssued(getHearingResulted(CONVICTED.getValue()));

        //then
        verify(xlatResultRepository).findByCjsResultCodeIn();
        assertNull(status);

    }

    @Test
    public void givenHearingResulted_whenConcludedResultCodeAvailable_thenReturnTrue() {
        //when
        when(xlatResultRepository.findByWqType(anyInt())).thenReturn(
                Arrays.asList(XLATResultEntity.builder().cjsResultCode(3030).build(),
                        XLATResultEntity.builder().cjsResultCode(3033).build()
                ));

        boolean status = crownCourtProcessHelper.isCaseConcluded(getHearingResulted(CONVICTED.getValue()));

        //then
        verify(xlatResultRepository).findByWqType(anyInt());
        assertTrue(status);
    }

    @Test
    public void givenHearingResulted_whenConcludedResultCodeAvailable_thenReturnFalse() {
        //when
        when(xlatResultRepository.findByWqType(anyInt())).thenReturn(
                Arrays.asList(XLATResultEntity.builder().cjsResultCode(1111).build(),
                        XLATResultEntity.builder().cjsResultCode(7778).build()
                ));

        boolean status = crownCourtProcessHelper.isCaseConcluded(getHearingResulted(CONVICTED.getValue()));

        //then
        verify(xlatResultRepository).findByWqType(anyInt());
        assertFalse(status);
    }

    private HearingResulted getHearingResulted(String outcome) {

        CCOutComeData ccOutComeData = CCOutComeData.builder()
                .ccooOutcome(outcome).build();
        Session session = Session.builder().courtLocation("OU").build();
        Offence offence = Offence.builder()
                .results(Collections.singletonList(Result.builder().resultCode("3030").build()))
                .build();
        Defendant defendant = Defendant.builder()
                .offences(Collections.singletonList(offence))
                .build();

        return HearingResulted.builder()
                .session(session)
                .ccOutComeData(ccOutComeData)
                .defendant(defendant)
                .build();
    }


    private HearingResulted hearingResultedWith(String... resultCodes) {

        List<Offence> offenceList = new ArrayList<>();

        Arrays.asList(resultCodes).forEach(result -> {

            offenceList.add(Offence.builder().offenceCode("CJ01502").results(
                    Collections.singletonList(Result.builder().resultCode(result).build())
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

        imprisonmentResultCodes().forEach(res -> {
            resultList.add(XLATResultEntity.builder().cjsResultCode(Integer.valueOf(res)).build());
        });

        return resultList;
    }

}

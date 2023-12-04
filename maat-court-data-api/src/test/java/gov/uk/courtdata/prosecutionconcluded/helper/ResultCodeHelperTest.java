package gov.uk.courtdata.prosecutionconcluded.helper;


import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.repository.XLATResultRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResultCodeHelperTest {

    @InjectMocks
    private ResultCodeHelper resultCodeHelper;

    @Mock
    private XLATResultRepository xlatResultRepository;

    @Test
    public void testWhenCCOutcomeIsConvictedAndResultCodeWithNonImp_thenReturnN() {

        when(xlatResultRepository.fetchResultCodesForCCImprisonment()).thenReturn(buildCCResultEntities());

        String isImp = resultCodeHelper.isImprisoned(CONVICTED.getValue(), List.of("0000"));

        verify(xlatResultRepository).fetchResultCodesForCCImprisonment();


        assertAll("Imprisoned", () -> assertNotNull(isImp), () -> assertEquals("N", isImp));
    }


    @Test
    public void testWhenCCOutcomeIsPartConvictedAndResultCodeWithNonImp_thenReturnN() {

        when(xlatResultRepository.fetchResultCodesForCCImprisonment()).thenReturn(buildCCResultEntities());

        String isImp = resultCodeHelper.isImprisoned(PART_CONVICTED.getValue(), Arrays.asList("0000"));

        assertAll("Imprisoned", () -> assertNotNull(isImp), () -> assertEquals("N", isImp));
    }

    @Test
    public void testWhenCCOutcomeIsAquittedAndResultCodeWithNonImp_thenReturnN() {

        //when(xlatResultRepository.fetchResultCodesForCCImprisonment()).thenReturn(buildCCResultEntities());

        String isImp = resultCodeHelper.isImprisoned(AQUITTED.getValue(), Arrays.asList("0000"));

        assertAll("Imprisoned",
                () -> assertNull(isImp));
    }

    @Test
    public void whenCCOutcomeIsPartConvictedAndResultCodeWithImp_thenFlagIsY() {

        when(xlatResultRepository.fetchResultCodesForCCImprisonment()).thenReturn(buildCCResultEntities());

        String imprisoned = resultCodeHelper.isImprisoned(PART_CONVICTED.getValue(), Arrays.asList("1002"));

        assertAll("Imprisoned", () -> assertNotNull(imprisoned), () -> assertEquals("Y", imprisoned));

    }

    @Test
    public void whenCCOutcomeIsConvictedAndResultCodeWithImp_thenFlagIsY() {


        when(xlatResultRepository.fetchResultCodesForCCImprisonment()).thenReturn(buildCCResultEntities());

        String imprisoned = resultCodeHelper.isImprisoned(CONVICTED.getValue(), Arrays.asList("1016"));

        assertAll("Imprisoned", () -> assertNotNull(imprisoned), () -> assertEquals("Y", imprisoned));

    }

    @Test
    public void whenCCOutcomeIsAquittedAndResultCodeWithImp_thenFlagIsNull() {

        String imprisoned = resultCodeHelper.isImprisoned(AQUITTED.getValue(), Arrays.asList("1002"));

        assertAll("Imprisoned",
                () -> assertNull(imprisoned));
    }

    @Test
    public void whenCCOutcomeIsConvAndResultCodeWithImp_thenBWarFlagIsY() {
        //when
        when(xlatResultRepository.fetchResultCodesForCCImprisonment()).thenReturn(null);
        Assertions.assertThrows(NullPointerException.class,()->{
            resultCodeHelper.isImprisoned(CONVICTED.getValue(), List.of("1002"));
            //then
            verify(xlatResultRepository).fetchResultCodesForCCImprisonment();
        });

    }

    @Test
    public void whenCCOutcomeIsAquittedAndResultCodeWithBW_thenBWarFlagIsY() {
        //when
        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(null);

        Assertions.assertThrows(NullPointerException.class,()->{
            //   thrown.expect(NullPointerException.class);
            resultCodeHelper.isBenchWarrantIssued(AQUITTED.getValue(), List.of("1002"));
            //then
            verify(xlatResultRepository).findByCjsResultCodeIn();
        });

    }

    @Test
    public void whenCCOutcomeIsConvictedAndResultCodeWithBW_thenReturnY() {

        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(buildCCResultEntities());

        String status = resultCodeHelper.isBenchWarrantIssued(CONVICTED.getValue(), Arrays.asList("1002"));

        //then
        verify(xlatResultRepository).findByCjsResultCodeIn();
        assertAll("Bench Warrant", () -> assertNotNull(status), () -> assertEquals("Y", status));
    }


    @Test
    public void whenCCOutcomeIsConvictedAndResultCodeWithBW_thenReturnNull() {

        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(buildCCResultEntities());

        String status = resultCodeHelper.isBenchWarrantIssued(CONVICTED.getValue(), Arrays.asList("1111"));

        verify(xlatResultRepository).findByCjsResultCodeIn();
        assertAll("Bench Warrant", () -> assertNull(status));
    }

    @Test
    public void whenCCOutcomeIsAquittedAndResultCodeWithBW_thenReturnY() {

        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(buildCCResultEntities());

        String status = resultCodeHelper.isBenchWarrantIssued(AQUITTED.getValue(), Arrays.asList("1002"));

        verify(xlatResultRepository).findByCjsResultCodeIn();
        assertAll("Bench Warrant", () -> assertNotNull(status), () -> assertEquals("Y", status));
    }

    @Test
    public void whenCCOutcomeIsAquittedAndResultCodeWithBW_thenBWarFlagIsNull() {

        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(buildCCResultEntities());

        String status = resultCodeHelper.isBenchWarrantIssued(AQUITTED.getValue(), Arrays.asList("2222"));

        verify(xlatResultRepository).findByCjsResultCodeIn();
        assertAll("Bench Warrant", () -> assertNull(status));
    }

    @Test
    public void whenCCOutcomeIsPartCAndResultCodeWithBW_thenReturnY() {

        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(buildCCResultEntities());

        String status = resultCodeHelper.isBenchWarrantIssued(PART_CONVICTED.getValue(), Arrays.asList("1016"));

        verify(xlatResultRepository).findByCjsResultCodeIn();
        assertAll("Bench Warrant", () -> assertNotNull(status), () -> assertEquals("Y", status));
    }

    @Test
    public void whenCCOutcomeIsPartCAndResultCodeWithBW_thenBWarFlagIsNull() {

        when(xlatResultRepository.findByCjsResultCodeIn()).thenReturn(buildCCResultEntities());

        String status = resultCodeHelper.isBenchWarrantIssued(PART_CONVICTED.getValue(), Arrays.asList("4545"));

        verify(xlatResultRepository).findByCjsResultCodeIn();
        assertAll("Bench Warrant", () -> assertNull(status));
    }

    private List<String> imprisonmentResultCodes() {
        return Arrays.asList("1002", "1024", "1003", "1007", "1016", "1002", "1081");
    }

    private List<XLATResultEntity> buildCCResultEntities() {

        List<XLATResultEntity> resultList = new ArrayList<>();

        imprisonmentResultCodes().forEach(res -> {
            resultList.add(XLATResultEntity.builder().cjsResultCode(Integer.valueOf(res)).build());
        });

        return resultList;
    }
}
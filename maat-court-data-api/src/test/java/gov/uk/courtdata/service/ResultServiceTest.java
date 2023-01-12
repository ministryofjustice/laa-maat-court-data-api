package gov.uk.courtdata.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.repository.ResultRepository;
import gov.uk.courtdata.repository.WQResultRepository;
import gov.uk.courtdata.repository.XLATResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResultServiceTest {

    @Mock
    WQResultRepository wqResultRepository;
    @Mock
    ResultRepository resultRepository;
    @Mock
    XLATResultRepository xlatResultRepository;
    @InjectMocks
    private ResultsService resultsService;

    @Test
    public void givenAValidCaseIdAndAsnSeq_whenFindResultCodesByCaseIdAndAsnSeqIsInvoked_shouldReturnIntegerList() {
        when(resultRepository.findResultCodeByCaseIdAndAsnSeq(TestEntityDataBuilder.TEST_CASE_ID, TestEntityDataBuilder.TEST_ASN_SEQ))
                .thenReturn(List.of(TestModelDataBuilder.TEST_RESULT_CODE));
        resultsService.findResultCodesByCaseIdAndAsnSeq(TestEntityDataBuilder.TEST_CASE_ID, TestEntityDataBuilder.TEST_ASN_SEQ);
        verify(resultRepository, atLeastOnce()).findResultCodeByCaseIdAndAsnSeq(anyInt(), anyString());
    }

    @Test
    public void givenAValidCaseIdAndAsnSeq_whenFindWQResultCodesByCaseIdAndAsnSeqIsInvoked_shouldReturnIntegerList() {
        when(wqResultRepository.findResultCodeByCaseIdAndAsnSeq(TestEntityDataBuilder.TEST_CASE_ID, TestEntityDataBuilder.TEST_ASN_SEQ))
                .thenReturn(List.of(TestModelDataBuilder.TEST_RESULT_CODE));
        resultsService.findWQResultCodesByCaseIdAndAsnSeq(TestEntityDataBuilder.TEST_CASE_ID, TestEntityDataBuilder.TEST_ASN_SEQ);
        verify(wqResultRepository, atLeastOnce()).findResultCodeByCaseIdAndAsnSeq(anyInt(), anyString());
    }

    @Test
    public void givenAValidResultCodeAvailable_whenFindXLATResultCodesForCCImprisonmentIsInvoked_shouldReturnXLATResultEntityList() {
        when(xlatResultRepository.fetchResultCodesForCCImprisonment())
                .thenReturn(List.of(XLATResultEntity.builder().build()));
        resultsService.findXLATResultCodesForCCImprisonment();
        verify(xlatResultRepository, atLeastOnce()).fetchResultCodesForCCImprisonment();
    }

    @Test
    public void givenAValidResultCodeAvailable_whenFindXLATResultCodesForCCBenchWarrantIsInvoked_shouldReturnXLATResultEntityList() {
        when(xlatResultRepository.findByCjsResultCodeIn())
                .thenReturn(List.of(XLATResultEntity.builder().build()));
        resultsService.findXLATResultCodesForCCBenchWarrant();
        verify(xlatResultRepository, atLeastOnce()).findByCjsResultCodeIn();
    }

    @Test
    public void givenValidWQTypeAndSubType_whenFindXLATResultCodesByWQTypeAndSubTypeCodeIsInvoked_shouldReturnIntegerList() {
        Integer wqType = 1234;
        Integer subType = 2345;
        when(xlatResultRepository.findResultsByWQType(wqType, subType))
                .thenReturn(List.of(TestModelDataBuilder.TEST_RESULT_CODE));
        resultsService.findXLATResultCodesByWQTypeAndSubTypeCode(wqType, subType);
        verify(xlatResultRepository, atLeastOnce()).findResultsByWQType(anyInt(), anyInt());
    }

}
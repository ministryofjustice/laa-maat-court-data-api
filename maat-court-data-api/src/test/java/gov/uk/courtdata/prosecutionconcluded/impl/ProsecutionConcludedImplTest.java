package gov.uk.courtdata.prosecutionconcluded.impl;

import gov.uk.courtdata.common.repository.CrownCourtStoredProcedureRepository;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.enums.CrownCourtCaseType;
import gov.uk.courtdata.enums.CrownCourtTrialOutcome;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.CrownCourtCodeHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.ResultCodeHelper;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.repository.RepOrderRepository;
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
import java.util.Optional;

import static gov.uk.courtdata.enums.CrownCourtAppealOutcome.isAppeal;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProsecutionConcludedImplTest {

    @InjectMocks
    private ProsecutionConcludedImpl prosecutionConcludedImpl;

    @Mock
    private RepOrderRepository repOrderRepository;

    @Mock
    private CrownCourtStoredProcedureRepository crownCourtStoredProcedureRepository;

    @Mock
    private CrownCourtCodeHelper crownCourtCodeHelper;

    @Mock
    private ProcessSentencingImpl processSentencingHelper;

    @Mock
    private ResultCodeHelper resultCodeHelper;

    @Mock
    private XLATResultRepository xlatResultRepository;


    @Test
    public void testWhenCaseConcludedAndImpAndWarrantisY_thenProcessDataAsExpected() {

        //given
        List<String> hearingResultCodes = List.of("1212","3343");
        ConcludedDTO concludedDTO = getConcludedDTO();

        //when
        RepOrderEntity repOrderEntity = RepOrderEntity.builder().catyCaseType(CrownCourtCaseType.INDICTABLE.name()).appealTypeCode("ACV").id(123).build();
        when(repOrderRepository.findById(anyInt())).thenReturn(Optional.of(repOrderEntity));

        String courtCode = "1212";
        when(crownCourtCodeHelper.getCode(anyString())).thenReturn(courtCode);

        when(resultCodeHelper.isImprisoned("CONVICTED",hearingResultCodes)).thenReturn("Y");
        when(resultCodeHelper.isBenchWarrantIssued("CONVICTED",hearingResultCodes)).thenReturn("Y");

//        when(xlatResultRepository.fetchResultCodesForCCImprisonment())
//                .thenReturn(buildCCImprisonmentResultEntities());

        prosecutionConcludedImpl.execute(concludedDTO);

        //then
        verify(crownCourtStoredProcedureRepository, times(1))
                .updateCrownCourtOutcome(concludedDTO.getProsecutionConcluded().getMaatId(),
                        CrownCourtTrialOutcome.CONVICTED.name(),
                        "Y",
                        "ACV",
                        "Y",
                        "caaseURN12",
                        courtCode);


        verify(processSentencingHelper)
                .processSentencingDate(concludedDTO.getCaseEndDate(),
                        concludedDTO.getProsecutionConcluded().getMaatId(),
                        repOrderEntity.getCatyCaseType());


    }


    @Test
    public void testWhenRepOrderIsMissing_thenDoNothing() {

        ConcludedDTO concludedDTO = getConcludedDTO();

        when(repOrderRepository.findById(anyInt())).thenReturn(Optional.empty());

        prosecutionConcludedImpl.execute(concludedDTO);

        verify(crownCourtStoredProcedureRepository, never())
                .updateCrownCourtOutcome(concludedDTO.getProsecutionConcluded().getMaatId(),
                        CrownCourtTrialOutcome.CONVICTED.name(),
                        "Y",
                        "ACV",
                        "Y",
                        "caaseURN12",
                        "");

        verify(processSentencingHelper,never())
                .processSentencingDate(concludedDTO.getCaseEndDate(),
                        concludedDTO.getProsecutionConcluded().getMaatId(),
                        "");


    }


    @Test
    public void givenMessageIsReceived_whenProsecutionConcludedImplTestIsInvoked_thenCrownCourtProcessingRepositoryIsCalled() {
        //given
        List<String> hearingResultCodes = List.of("1212","3343");
        ConcludedDTO concludedDTO = getConcludedDTO();

        //when
        RepOrderEntity repOrderEntity = RepOrderEntity.builder().catyCaseType(CrownCourtCaseType.INDICTABLE.name()).appealTypeCode("ACV").id(123).build();
        when(repOrderRepository.findById(anyInt())).thenReturn(Optional.of(repOrderEntity));

        String courtCode = "1212";
        when(crownCourtCodeHelper.getCode(anyString())).thenReturn(courtCode);

        when(resultCodeHelper.isImprisoned("CONVICTED",hearingResultCodes)).thenReturn("N");
        when(resultCodeHelper.isBenchWarrantIssued("CONVICTED",hearingResultCodes)).thenReturn("N");

        prosecutionConcludedImpl.execute(concludedDTO);

        //then
        verify(crownCourtStoredProcedureRepository)
                .updateCrownCourtOutcome(concludedDTO.getProsecutionConcluded().getMaatId(),
                        CrownCourtTrialOutcome.CONVICTED.name(),
                        "N",
                        "ACV",
                        "N",
                        "caaseURN12",
                        courtCode);


        verify(processSentencingHelper)
                .processSentencingDate(concludedDTO.getCaseEndDate(),
                        concludedDTO.getProsecutionConcluded().getMaatId(),
                        repOrderEntity.getCatyCaseType());

    }

    @Test
    public void testWhenResultCodesAreNotValid_thenProcessWithNullExpected() {
        //given
        ConcludedDTO concludedDTO = getConcludedDTO();

        //when
        RepOrderEntity repOrderEntity = RepOrderEntity.builder().catyCaseType(CrownCourtCaseType.INDICTABLE.name()).appealTypeCode("ACV").id(123).build();
        when(repOrderRepository.findById(anyInt())).thenReturn(Optional.of(repOrderEntity));

        String courtCode = "1212";
        when(crownCourtCodeHelper.getCode(anyString())).thenReturn(courtCode);

        prosecutionConcludedImpl.execute(concludedDTO);

        //then
        verify(crownCourtStoredProcedureRepository)
                .updateCrownCourtOutcome(concludedDTO.getProsecutionConcluded().getMaatId(),
                        CrownCourtTrialOutcome.CONVICTED.name(),
                        null,
                        "ACV",
                        null,
                        "caaseURN12",
                        courtCode);


        verify(processSentencingHelper)
                .processSentencingDate(concludedDTO.getCaseEndDate(),
                        concludedDTO.getProsecutionConcluded().getMaatId(),
                        repOrderEntity.getCatyCaseType());

    }

    @Test
    public void givenOutcomeIsEmpty_whenProsecutionConcludedImplCalled_ThenExceptionThrown() {

        ConcludedDTO concludedDTO = ConcludedDTO.builder()
                .prosecutionConcluded(ProsecutionConcluded.builder().maatId(121111).build())
                .build();

        prosecutionConcludedImpl.execute(concludedDTO);

        Assertions.assertThrows(ValidationException.class, () -> isAppeal(null));
    }

    private List<String> imprisonmentResultCodes() {
        return Arrays.asList("1002", "1024", "1003", "1007", "1016", "1002", "1081");
    }

    private List<XLATResultEntity> buildCCImprisonmentResultEntities() {

        List<XLATResultEntity> resultList = new ArrayList<>();

        imprisonmentResultCodes().forEach(res -> resultList.add(XLATResultEntity.builder().cjsResultCode(Integer.valueOf(res)).build()));

        return resultList;
    }

    private ConcludedDTO getConcludedDTO() {
        List<String> hearingResultCodes = List.of("1212","3343");
        return ConcludedDTO.builder()
                .calculatedOutcome(CrownCourtTrialOutcome.CONVICTED.name())
                .hearingResultCodeList(hearingResultCodes)
                .caseUrn("caaseURN12")
                .ouCourtLocation("121")
                .prosecutionConcluded(ProsecutionConcluded.builder()
                        .maatId(121111)
                        .isConcluded(true)
                        .build())
                .build();
    }
}
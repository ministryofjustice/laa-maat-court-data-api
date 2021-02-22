package gov.uk.courtdata.hearing.impl;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.hearing.dto.ResultDTO;
import gov.uk.courtdata.hearing.mapper.HearingDTOMapper;
import gov.uk.courtdata.hearing.processor.HearingWQProcessor;
import gov.uk.courtdata.hearing.processor.WQCoreProcessor;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.processor.OffenceCodeRefDataProcessor;
import gov.uk.courtdata.processor.ResultCodeRefDataProcessor;
import gov.uk.courtdata.repository.IdentifierRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HearingResultedImplTest {

    @InjectMocks
    private HearingResultedImpl hearingResultedImpl;

    @Mock
    private HearingDTOMapper hearingDTOMapper;

    @Mock
    private IdentifierRepository identifierRepository;

    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Mock
    private  HearingWQProcessor hearingWQProcessor;

    @Mock
    private  ResultCodeRefDataProcessor resultCodeRefDataProcessor;

    @Mock
    private  OffenceCodeRefDataProcessor offenceCodeRefDataProcessor;

    @Mock
    private  WQCoreProcessor wqCoreProcessor;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void givenACaseDetail_whenHearingResultedImplIsInvoked_thenProcessingImplIsInvoked() {

        //given
        HearingResulted laaHearingDetails = HearingResulted.builder().maatId(12345)
                .jurisdictionType(JurisdictionType.CROWN)
                .defendant(getDefendant())
                .build();

        //when
        List<WqLinkRegisterEntity> wqLinkRegisterEntities = Collections.singletonList(WqLinkRegisterEntity.builder()
                .caseId(565)
                .proceedingId(12121).maatId(12345).build());
        Mockito.when(wqLinkRegisterRepository.findBymaatId(12345)).thenReturn(wqLinkRegisterEntities);
        Mockito.when(identifierRepository.getTxnID()).thenReturn(123);

        HearingDTO hearingDTO = HearingDTO.builder().
                result(ResultDTO.builder().resultCode(3026).build()).build();

        Mockito.when(hearingDTOMapper.toHearingDTO(any(),any(),any(),any(),any(),any())).thenReturn(hearingDTO);

        hearingResultedImpl.execute(laaHearingDetails);

        //then
        verify(resultCodeRefDataProcessor).processResultCode(3026);
        verify(wqLinkRegisterRepository).findBymaatId(12345);
        verify(offenceCodeRefDataProcessor).processOffenceCode("23224");
        verify(hearingWQProcessor).process(any());
        verify(wqCoreProcessor).findWQType(any());
    }

    @Test
    public void givenACaseDetail_whenHearingResultedImplForMag_thenProcessingImplIsInvoked() {

        //given
        HearingResulted laaHearingDetails = HearingResulted.builder().maatId(12345)
                .jurisdictionType(JurisdictionType.MAGISTRATES)
                .defendant(getDefendant())
                .build();

        //when
        List<WqLinkRegisterEntity> wqLinkRegisterEntities = Collections.singletonList(WqLinkRegisterEntity.builder()
                .caseId(565)
                .proceedingId(12121).maatId(12345).build());
        Mockito.when(wqLinkRegisterRepository.findBymaatId(12345)).thenReturn(wqLinkRegisterEntities);
        Mockito.when(identifierRepository.getTxnID()).thenReturn(-1);

        HearingDTO hearingDTO = HearingDTO.builder().
                result(ResultDTO.builder().resultCode(3026).build()).build();

        Mockito.when(hearingDTOMapper.toHearingDTO(any(),any(),any(),any(),any(),any())).thenReturn(hearingDTO);

        hearingResultedImpl.execute(laaHearingDetails);

        //then
        verify(resultCodeRefDataProcessor).processResultCode(3026);
        verify(wqLinkRegisterRepository).findBymaatId(12345);
        verify(offenceCodeRefDataProcessor).processOffenceCode("23224");
        verify(hearingWQProcessor).process(any());
    }

    private Defendant getDefendant() {

        return Defendant.builder()
                .offences(Collections.singletonList(
                        Offence.builder().legalAidStatus("AP").asnSeq("0")
                                .offenceCode("23224")
                                .asnSeq("1").legalAidReason("some aid reason")
                                .results(Collections.singletonList(Result.builder().resultCode("3026").build()))
                                .build()))
                .build();
    }
}

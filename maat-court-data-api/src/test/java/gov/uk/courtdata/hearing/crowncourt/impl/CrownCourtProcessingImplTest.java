package gov.uk.courtdata.hearing.crowncourt.impl;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.util.DateUtil;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CrownCourtProcessingImplTest {

    @InjectMocks
    private CrownCourtProcessingImpl crownCourtProcessingImpl;
    @Mock
    private CrownCourtProcessingRepository crownCourtProcessingRepository;
    @Mock
    private RepOrderRepository repOrderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenCCMessageIsReceived_whenCrownCourtProcessingImplIsInvoked_thenCrownCourtProcessingRepositoryIsCalled() {

        //given
        CCOutComeData ccOutComeData = CCOutComeData.builder().build();
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .ccOutComeData(ccOutComeData)
                .build();
        RepOrderEntity repOrderEntity = RepOrderEntity.builder().catyCaseType("ABC").aptyCode("ACV").id(123).build();

        //when
        when(repOrderRepository.findById(anyInt())).thenReturn(Optional.of(repOrderEntity));
        crownCourtProcessingImpl.execute(hearingDetails);

        //then
        verify(crownCourtProcessingRepository, times(1))
                .invokeCrownCourtOutcomeProcess(hearingDetails.getMaatId(),
                        ccOutComeData.getCcooOutcome(),
                        ccOutComeData.getBenchWarrantIssuedYn(),
                        "ACV",
                        ccOutComeData.getCcImprisioned(),
                        hearingDetails.getCaseUrn(),
                        ccOutComeData.getCrownCourtCode());

    }

    @Test
    public void givenCCMessageIsReceived_whenAppealToCCScenario_thenInvokeUpdateAppealSentenceOrderDateIsCalled() {

        //given
        CCOutComeData ccOutComeData = CCOutComeData.builder().caseEndDate("2020-02-02").build();
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .ccOutComeData(ccOutComeData)
                .build();
        RepOrderEntity repOrderEntity = RepOrderEntity.builder().catyCaseType("APPEAL CC").aptyCode("ACV").id(123).build();

        //when
        when(repOrderRepository.findById(anyInt())).thenReturn(Optional.of(repOrderEntity));
        crownCourtProcessingImpl.execute(hearingDetails);

        //then
        verify(crownCourtProcessingRepository, times(1))
                .invokeUpdateAppealSentenceOrderDate(hearingDetails.getMaatId(),
                        null,
                        DateUtil.parse(ccOutComeData.getCaseEndDate()),
                        LocalDate.now());
    }

    @Test
    public void givenCCMessageIsReceived_whenNONAppealToCCScenario_thenInvokeUpdateSentenceOrderDateIsCalled() {

        //given
        CCOutComeData ccOutComeData = CCOutComeData.builder().caseEndDate("2020-02-02").build();
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(12345)
                .ccOutComeData(ccOutComeData)
                .build();
        RepOrderEntity repOrderEntity = RepOrderEntity.builder().catyCaseType("NON APPEAL").aptyCode("ACV").id(123).build();

        //when
        when(repOrderRepository.findById(anyInt())).thenReturn(Optional.of(repOrderEntity));
        crownCourtProcessingImpl.execute(hearingDetails);

        //then
        verify(crownCourtProcessingRepository, times(1))
                .invokeUpdateSentenceOrderDate(hearingDetails.getMaatId(),
                        null,
                        DateUtil.parse(ccOutComeData.getCaseEndDate()));
    }
}




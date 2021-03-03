package gov.uk.courtdata.hearing.processor;

import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.entity.XLATOffenceEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import gov.uk.courtdata.repository.XLATOffenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CourtApplicationsPreProcessorTest {

    @InjectMocks
    private CourtApplicationsPreProcessor courtApplicationsPreProcessor;

    @Mock
    private XLATOffenceRepository xlatOffenceRepository;
    @Mock
    private OffenceRepository offenceRepository;
    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenApplicationDetail_whenCourtApplicationsPreProcessorIsInvoked_thenASNSeqIsSet() {

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
        Mockito.when(offenceRepository.findApplicationByOffenceCode(any(), any(), any())).thenReturn(Optional.empty());
        Mockito.when(xlatOffenceRepository.findById(any())).thenReturn(Optional.of(XLATOffenceEntity.builder().applicationFlag(1).build()));

        courtApplicationsPreProcessor.process(laaHearingDetails);

        //then
        assertThat(laaHearingDetails.getDefendant().getOffences().get(0).getAsnSeq()).isEqualTo("1");
        assertThat(laaHearingDetails.getDefendant().getOffences().get(0).getOffenceClassification()).isEqualTo("1");
    }


    @Test
    public void givenMultipleApplicationDetail_whenCourtApplicationsPreProcessorIsInvoked_thenASNSeqIsSetForExistingApplication() {

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
        Mockito.when(offenceRepository.findApplicationByOffenceCode(any(), any(), any())).thenReturn(Optional.of(OffenceEntity.builder().asnSeq("1").build()));
        Mockito.when(xlatOffenceRepository.findById(any())).thenReturn(Optional.of(XLATOffenceEntity.builder().applicationFlag(1).build()));

        courtApplicationsPreProcessor.process(laaHearingDetails);

        //then
        assertThat(laaHearingDetails.getDefendant().getOffences().get(0).getAsnSeq()).isEqualTo("1");
        assertThat(laaHearingDetails.getDefendant().getOffences().get(0).getOffenceClassification()).isEqualTo("1");
    }


    @Test
    public void givenNoRecordAvailable_whenCourtApplicationsPreProcessorIsInvoked_thenASNSeqIsSetToNULL() {

        //given
        HearingResulted laaHearingDetails = HearingResulted.builder().maatId(12345)
                .jurisdictionType(JurisdictionType.CROWN)
                .defendant(getDefendant())
                .build();

        //when

        Mockito.when(wqLinkRegisterRepository.findBymaatId(any())).thenReturn(new ArrayList<>());
        Mockito.when(xlatOffenceRepository.findById(any())).thenReturn(Optional.of(XLATOffenceEntity.builder().applicationFlag(1).build()));

        courtApplicationsPreProcessor.process(laaHearingDetails);

        //then
        assertThat(laaHearingDetails.getDefendant().getOffences().get(0).getAsnSeq()).isNull();

    }

    @Test
    public void givenApplicationDetail_whenNullOffenceCodeIsInvoked_thenOffenceClassificationIsSetToNull() {

        //given
        HearingResulted laaHearingDetails = HearingResulted.builder().maatId(12345)
                .jurisdictionType(JurisdictionType.CROWN)
                .defendant(getDefendant())
                .build();
        laaHearingDetails.getDefendant().getOffences().get(0).setOffenceCode(null);

        //when

        Mockito.when(wqLinkRegisterRepository.findBymaatId(any())).thenReturn(new ArrayList<>());

        courtApplicationsPreProcessor.process(laaHearingDetails);

        //then
        assertThat(laaHearingDetails.getDefendant().getOffences().size()).isZero();


    }

    private Defendant getDefendant() {

        return Defendant.builder()
                .offences(Collections.singletonList(
                        Offence.builder().legalAidStatus("AP")
                                .offenceCode("23224")
                                .offenceId("Offence ID")
                                .legalAidReason("some aid reason")
                                .results(Collections.singletonList(Result.builder().resultCode("3026").build()))
                                .build()))
                .build();
    }

}

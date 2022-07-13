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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CourtApplicationsPreProcessorTest {

    @InjectMocks
    private CourtApplicationsPreProcessor courtApplicationsPreProcessor;

    @Mock
    private XLATOffenceRepository xlatOffenceRepository;

    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Mock
    private OffenceRepository offenceRepository;


    @Test
    public void testApplicationFlagAndASNSeqForNewApplication() {

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

        Mockito.when(xlatOffenceRepository.findById(any())).thenReturn(Optional.of(XLATOffenceEntity.builder().applicationFlag(1).build()));

        courtApplicationsPreProcessor.process(laaHearingDetails);

        //then
        assertThat(laaHearingDetails.getDefendant().getOffences().get(0).getAsnSeq()).isEqualTo("1000");
        assertThat(laaHearingDetails.getDefendant().getOffences().get(0).getApplicationFlag()).isEqualTo(1);
    }


    @Test
    public void testApplicationFlagAndASNSeqForExistingApplication() {

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

        Mockito.when(xlatOffenceRepository.findById(any())).thenReturn(Optional.of(XLATOffenceEntity.builder().applicationFlag(1).build()));

        Mockito.when(offenceRepository.findApplicationByOffenceCode(any(), any(), any())).thenReturn(Optional.of(OffenceEntity.builder().applicationFlag(1).asnSeq("1006").build()));

        courtApplicationsPreProcessor.process(laaHearingDetails);

        //then
        assertThat(laaHearingDetails.getDefendant().getOffences().get(0).getAsnSeq()).isEqualTo("1006");
        assertThat(laaHearingDetails.getDefendant().getOffences().get(0).getApplicationFlag()).isEqualTo(1);
    }


    @Test
    public void testApplicationFuncWhenNoMAATId() {

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
    public void testAppFuncWhenNoOffenceCode() {

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
        assertThat(laaHearingDetails.getDefendant().getOffences().get(0).getApplicationFlag()).isNull();


    }


    @Test
    public void testApplicationFlagAndASNSeqForENewAndExistingApplication() {

        Offence off = Offence.builder().legalAidStatus("AP")
                .offenceCode("123")
                .offenceId("Offence ID")
                .legalAidReason("some aid reason")
                .build();

        //given
        HearingResulted laaHearingDetails = HearingResulted.builder().maatId(12345)
                .jurisdictionType(JurisdictionType.CROWN)
                .defendant(getDefendant())
                .build();
        List<Offence> offenceList = Arrays.asList(off, getOffence());


        laaHearingDetails.getDefendant().setOffences(offenceList);

        //when
        List<WqLinkRegisterEntity> wqLinkRegisterEntities = Collections.singletonList(WqLinkRegisterEntity.builder()
                .caseId(565)
                .proceedingId(12121).maatId(12345).build());
        Mockito.when(wqLinkRegisterRepository.findBymaatId(12345)).thenReturn(wqLinkRegisterEntities);

        Mockito.when(xlatOffenceRepository.findById(any())).thenReturn(Optional.of(XLATOffenceEntity.builder().applicationFlag(1).build()));

      //  Mockito.when(offenceRepository.findApplicationByOffenceCode(565, "23224", 1)).thenReturn(Optional.of(OffenceEntity.builder().applicationFlag(1).asnSeq("1006").build()));

        courtApplicationsPreProcessor.process(laaHearingDetails);

        //then
        assertThat(laaHearingDetails.getDefendant().getOffences().get(0).getAsnSeq()).isNotNull();
        assertThat(laaHearingDetails.getDefendant().getOffences().get(1).getAsnSeq()).isNotNull();
        assertThat(laaHearingDetails.getDefendant().getOffences().get(0).getApplicationFlag()).isEqualTo(1);
    }


    private Defendant getDefendant() {

        return Defendant.builder()
                .offences(Collections.singletonList(
                        getOffence()))
                .build();
    }

    private Offence getOffence() {
        return Offence.builder().legalAidStatus("AP")
                .offenceCode("23224")
                .offenceId("Offence ID")
                .legalAidReason("some aid reason")
                .results(Collections.singletonList(Result.builder().resultCode("3026").build()))
                .build();
    }

}

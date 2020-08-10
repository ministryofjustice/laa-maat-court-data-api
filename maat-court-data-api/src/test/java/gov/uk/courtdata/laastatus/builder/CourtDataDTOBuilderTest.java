package gov.uk.courtdata.laastatus.builder;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.repository.DefendantMAATDataRepository;
import gov.uk.courtdata.repository.OffenceRepository;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CourtDataDTOBuilderTest {

    @InjectMocks
    private CourtDataDTOBuilder courtDataDTOBuilder;

    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;

    @Mock
    private SolicitorMAATDataRepository solicitorMAATDataRepository;

    @Mock
    private DefendantMAATDataRepository defendantMAATDataRepository;

    @Mock
    private OffenceRepository offenceRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenCaseDetailsIsReceived_whenCourtDataDTOBuilderIsInvoked_thenReturnedCaseDetailsDTO() {


        CaseDetails caseDetails = CaseDetails.builder().maatId(12)
                .defendant(Defendant.builder().surname("Smith")
                        .offences(Collections.singletonList(Offence.builder().asnSeq("67")
                                .offenceCode("A603060").build()))
                        .build())
                .build();

        List<WqLinkRegisterEntity> wqLinkRegisterEntityList =
                Collections.singletonList(WqLinkRegisterEntity.builder()
                        .libraId("4506454")
                        .proceedingId(789)
                        .caseId(12).build());

        Optional<SolicitorMAATDataEntity> optionalSolicitorMAATDataEntity =
                Optional.of(SolicitorMAATDataEntity.builder().maatId(12).build());

        Optional<DefendantMAATDataEntity> optionalDefendantMAATDataEntity =
                Optional.of(DefendantMAATDataEntity.builder().maatId(12).build());

        Optional<OffenceEntity> offenceEntity = Optional.of(OffenceEntity.builder()
                .caseId(88999)
                .offenceId("90")
                .offenceCode("A603060")
                .build());

        //when
        when(wqLinkRegisterRepository.findBymaatId(anyInt())).thenReturn(wqLinkRegisterEntityList);
        when(solicitorMAATDataRepository.findBymaatId(anyInt())).thenReturn(optionalSolicitorMAATDataEntity);
        when(defendantMAATDataRepository.findBymaatId(anyInt())).thenReturn(optionalDefendantMAATDataEntity);
        when(offenceRepository.findByMaxTxId(anyInt(), anyString())).thenReturn(offenceEntity);

        CourtDataDTO courtDataDTO = courtDataDTOBuilder.build(caseDetails);

        //then
        verify(wqLinkRegisterRepository).findBymaatId(anyInt());
        verify(solicitorMAATDataRepository).findBymaatId(anyInt());
        verify(defendantMAATDataRepository).findBymaatId(anyInt());
        verify(offenceRepository).findByMaxTxId(anyInt(), anyString());

        assertThat(courtDataDTO.getCaseId()).isEqualTo(12);
        assertThat(courtDataDTO.getLibraId()).isEqualTo("06454");
        assertThat(courtDataDTO.getProceedingId()).isEqualTo(789);
        assertThat(courtDataDTO.getDefendantMAATDataEntity()).isEqualTo(optionalDefendantMAATDataEntity.get());
        assertThat(courtDataDTO.getSolicitorMAATDataEntity()).isEqualTo(optionalSolicitorMAATDataEntity.get());

    }

    @Test (expected = NoSuchElementException.class)
    public void givenCaseDetailsIsReceived_whenCourtDataDTOBuilderIsInvoked_thenThrowException() {

        CaseDetails caseDetails = CaseDetails.builder().build();
        courtDataDTOBuilder.build(caseDetails);
    }
}

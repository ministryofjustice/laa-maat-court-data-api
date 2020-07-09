package gov.uk.courtdata.laastatus.builder;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepOrderUpdateMessageBuilderTest {

    @Mock
    private RepOrderCPDataRepository repOrderCPDataRepository;

    @Mock
    private SolicitorMAATDataRepository solicitorMAATDataRepository;

    @InjectMocks
    private RepOrderUpdateMessageBuilder repOrderUpdateMessageBuilder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenCaseDetailsIsReceived_whenIsRepOrderUpdateMessageBuilderInvoked_thenReturnedLaaStatusUpdate() {

        CaseDetails caseDetails = CaseDetails.builder().maatId(1234567)
                .defendant(Defendant.builder().surname("Smith")
                        .offences(Collections.singletonList(Offence.builder().offenceCode("67")
                                .offenceId("987").legalAidStatus("OK").legalAidStatusDate("2010-10-10").build()))
                        .build())
                .build();

        Optional<RepOrderCPDataEntity> repOrderCPData = Optional.of(RepOrderCPDataEntity.builder().defendantId("555666").build());
        when(repOrderCPDataRepository.findByrepOrderId(anyInt())).thenReturn(repOrderCPData);

        LaaStatusUpdate laaStatusUpdate = repOrderUpdateMessageBuilder.build(caseDetails);

        assertThat(laaStatusUpdate.getData().getAttributes().getMaatReference()).isEqualTo(1234567);

        assertThat(laaStatusUpdate.getData().getAttributes().getOffences().get(0).getOffenceId()).isEqualTo("987");
        assertThat(laaStatusUpdate.getData().getAttributes().getOffences().get(0).getStatusCode()).isEqualTo("OK");
        assertThat(laaStatusUpdate.getData().getAttributes().getOffences().get(0).getEffectiveEndDate()).isEqualTo("2010-10-10");

        assertThat(laaStatusUpdate.getData().getRelationships().getDefendant().getData().getId()).isEqualTo("555666");
        assertThat(laaStatusUpdate.getData().getRelationships().getDefendant().getData().getType()).isEqualTo("defendants");

        assertThat(laaStatusUpdate.getData().getRelationships().getDefendant().getData().getType()).isEqualTo("defendants");

        verify(repOrderCPDataRepository).findByrepOrderId(anyInt());
        verify(solicitorMAATDataRepository).findBymaatId(anyInt());
    }

    @Test
    public void testSolicitor_whenIsRepOrderUpdateMessageBuilderInvoked_thenReturnedLaaStatusUpdate() {

        CaseDetails caseDetails = CaseDetails.builder().maatId(1234567)
                .defendant(Defendant.builder().surname("Smith")
                        .offences(Collections.singletonList(Offence.builder().offenceCode("67")
                                .offenceId("987").legalAidStatus("OK").legalAidStatusDate("2010-10-10").build()))
                        .build())
                .build();

        Optional<SolicitorMAATDataEntity> optSolicitor = Optional.of(SolicitorMAATDataEntity.builder()
                .accountCode("456").accountName("Solicitor Name Ltd").build());
        when(solicitorMAATDataRepository.findBymaatId(anyInt())).thenReturn(optSolicitor);

        LaaStatusUpdate laaStatusUpdate = repOrderUpdateMessageBuilder.build(caseDetails);

        assertThat(laaStatusUpdate.getData().getAttributes().getDefenceOrganisation().getOrganisation().getName()).isEqualTo("Solicitor Name Ltd");
        assertThat(laaStatusUpdate.getData().getAttributes().getDefenceOrganisation().getLaaContractNumber()).isEqualTo("456");
    }
}

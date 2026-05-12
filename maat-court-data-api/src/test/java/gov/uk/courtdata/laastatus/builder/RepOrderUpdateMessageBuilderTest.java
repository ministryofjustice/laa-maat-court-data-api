package gov.uk.courtdata.laastatus.builder;

import static gov.uk.courtdata.constants.CourtDataConstants.CDA_TRANSACTION_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.laastatus.Address;
import gov.uk.courtdata.model.laastatus.Contact;
import gov.uk.courtdata.model.laastatus.DefenceOrganisation;
import gov.uk.courtdata.model.laastatus.DefendantData;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RepOrderUpdateMessageBuilderTest {

    @Mock
    private RepOrderCPDataRepository repOrderCPDataRepository;

    @Mock
    private SolicitorMAATDataRepository solicitorMAATDataRepository;

    @InjectMocks
    private RepOrderUpdateMessageBuilder repOrderUpdateMessageBuilder;

    @Test
    void givenCaseDetailsIsReceived_whenIsRepOrderUpdateMessageBuilderInvoked_thenReturnedLaaStatusUpdate() {

        CaseDetails caseDetails = CaseDetails.builder()
                .maatId(1234567)
                .defendant(Defendant.builder()
                        .surname("Smith")
                        .offences(Collections.singletonList(Offence.builder()
                                .offenceCode("67")
                                .offenceId("987")
                                .legalAidStatus("OK")
                                .legalAidStatusDate("2010-10-10")
                                .build()))
                        .build())
                .build();

        Optional<RepOrderCPDataEntity> repOrderCPData =
                Optional.of(RepOrderCPDataEntity.builder().defendantId("555666").build());
        when(repOrderCPDataRepository.findByrepOrderId(anyInt())).thenReturn(repOrderCPData);

        LaaStatusUpdate laaStatusUpdate = repOrderUpdateMessageBuilder.build(caseDetails);

        assertThat(laaStatusUpdate.getData().getAttributes().getMaatReference()).isEqualTo(1234567);

        gov.uk.courtdata.model.laastatus.Offence offence =
                laaStatusUpdate.getData().getAttributes().getOffences().getFirst();

        SoftAssertions.assertSoftly(s -> {
            assertThat(offence.getOffenceId()).isNotNull().isEqualTo("987");
            assertThat(offence.getStatusCode()).isNotNull().isEqualTo("OK");
            assertThat(offence.getEffectiveEndDate()).isNotNull().isEqualTo("2010-10-10");
        });

        DefendantData defData =
                laaStatusUpdate.getData().getRelationships().getDefendant().getData();

        SoftAssertions.assertSoftly(s -> {
            assertThat(defData.getId()).isNotNull().isEqualTo("555666");
            assertThat(defData.getType()).isNotNull().isEqualTo("defendants");
        });

        verify(repOrderCPDataRepository).findByrepOrderId(anyInt());
        verify(solicitorMAATDataRepository).findBymaatId(anyInt());
    }

    @Test
    void testSolicitor_whenIsRepOrderUpdateMessageBuilderInvoked_thenReturnedLaaStatusUpdate() {

        CaseDetails caseDetails = CaseDetails.builder()
                .maatId(1234567)
                .defendant(Defendant.builder()
                        .surname("Smith")
                        .offences(Collections.singletonList(Offence.builder()
                                .offenceCode("67")
                                .offenceId("987")
                                .legalAidStatus("OK")
                                .legalAidStatusDate("2010-10-10")
                                .build()))
                        .build())
                .build();

        Optional<SolicitorMAATDataEntity> optSolicitor = Optional.of(SolicitorMAATDataEntity.builder()
                .accountCode("456")
                .accountName("Solicitor Name Ltd")
                .build());
        when(solicitorMAATDataRepository.findBymaatId(anyInt())).thenReturn(optSolicitor);

        LaaStatusUpdate laaStatusUpdate = repOrderUpdateMessageBuilder.build(caseDetails);

        DefenceOrganisation defOrg = laaStatusUpdate.getData().getAttributes().getDefenceOrganisation();

        SoftAssertions.assertSoftly(s -> {
            assertThat(defOrg.getLaaContractNumber()).isNotNull().isEqualTo("456");
            assertThat(defOrg.getOrganisation().getName()).isNotNull().isEqualTo("Solicitor Name Ltd");
        });
    }

    @Test
    void testSolicitorAddress_IsAvailableOnLaaStatusRequest() {

        CaseDetails caseDetails = buildSampleCase();

        Optional<SolicitorMAATDataEntity> optSolicitor = buildCompleteSolicitorDetails();

        when(solicitorMAATDataRepository.findBymaatId(anyInt())).thenReturn(optSolicitor);

        LaaStatusUpdate laaStatusUpdate = repOrderUpdateMessageBuilder.build(caseDetails);

        Address address = laaStatusUpdate
                .getData()
                .getAttributes()
                .getDefenceOrganisation()
                .getOrganisation()
                .getAddress();

        SoftAssertions.assertSoftly(s -> {
            assertThat(address).isNotNull();
            assertThat(address.getAddress1())
                    .isNotNull()
                    .isEqualTo(optSolicitor.get().getLine1());
            assertThat(address.getAddress2())
                    .isNotNull()
                    .isEqualTo(optSolicitor.get().getLine2());
            assertThat(address.getAddress3())
                    .isNotNull()
                    .isEqualTo(optSolicitor.get().getLine3());
            assertThat(address.getAddress4())
                    .isNotNull()
                    .isEqualTo(optSolicitor.get().getCity());
            assertThat(address.getPostcode())
                    .isNotNull()
                    .isEqualTo(optSolicitor.get().getPostcode());
        });
    }

    @Test
    void testSolicitorContact_isAvailableOnLaaStatusUpdate_Request() {

        CaseDetails caseDetails = buildSampleCase();

        Optional<SolicitorMAATDataEntity> optSolicitor = buildCompleteSolicitorDetails();

        when(solicitorMAATDataRepository.findBymaatId(anyInt())).thenReturn(optSolicitor);

        LaaStatusUpdate laaStatusUpdate = repOrderUpdateMessageBuilder.build(caseDetails);

        Contact contact = laaStatusUpdate
                .getData()
                .getAttributes()
                .getDefenceOrganisation()
                .getOrganisation()
                .getContact();

        SoftAssertions.assertSoftly(s -> {
            assertThat(contact).isNotNull();
            assertThat(contact.getWork())
                    .isNotNull()
                    .isEqualTo(optSolicitor.get().getPhone());
            assertThat(contact.getPrimaryEmail())
                    .isNotNull()
                    .isEqualTo(optSolicitor.get().getAdminEmail());
            assertThat(contact.getSecondaryEmail())
                    .isNotNull()
                    .isEqualTo(optSolicitor.get().getEmail());
        });
    }

    @Test
    void givenCaseDetailsIsReceived_whenIsRepOrderUpdateMessageBuilderInvoked_thenHeaderDetailsAreReturned() {

        // given
        CaseDetails caseDetails = CaseDetails.builder()
                .laaTransactionId(UUID.fromString("6f5b34ea-e038-4f1c-bfe5-d6bf622444f0"))
                .build();
        CourtDataDTO courtDataDTO =
                CourtDataDTO.builder().caseDetails(caseDetails).txId(123456).build();

        // when
        Map<String, String> headers = repOrderUpdateMessageBuilder.buildHeaders(courtDataDTO);

        SoftAssertions.assertSoftly(s -> {
            assertThat(headers)
                    .containsEntry(CDA_TRANSACTION_ID_HEADER, "6f5b34ea-e038-4f1c-bfe5-d6bf622444f0")
                    .containsEntry("Laa-Status-Transaction-Id", "123456");
        });
    }

    @Test
    void
            givenCaseDetailsIsReceived_whenIsRepOrderUpdateMessageBuilderInvoked_thenHeaderDetailsAreReturnedWithNullTxnID() {

        // given
        CaseDetails caseDetails = CaseDetails.builder().build();
        CourtDataDTO courtDataDTO =
                CourtDataDTO.builder().caseDetails(caseDetails).txId(123456).build();

        // when
        Map<String, String> headers = repOrderUpdateMessageBuilder.buildHeaders(courtDataDTO);

        // then
        SoftAssertions.assertSoftly(s -> assertThat(headers)
                .containsEntry(CDA_TRANSACTION_ID_HEADER, null)
                .containsEntry("Laa-Status-Transaction-Id", "123456"));
    }

    private Optional<SolicitorMAATDataEntity> buildCompleteSolicitorDetails() {

        return Optional.of(SolicitorMAATDataEntity.builder()
                .accountCode("456")
                .accountName("BRAY & BRAY")
                .line1("SPA PLACE")
                .line2("36-42")
                .line3("HUMBERSTONE ROAD")
                .city("LEICESTER")
                .postcode("LE5 0AE")
                .phone("0116 254 8871")
                .adminEmail("test@test.com")
                .email("test@test.com")
                .build());
    }

    private CaseDetails buildSampleCase() {

        return CaseDetails.builder()
                .maatId(864325)
                .defendant(Defendant.builder()
                        .surname("Gregory")
                        .offences(Collections.singletonList(Offence.builder()
                                .offenceCode("CJ01501")
                                .offenceId("987")
                                .legalAidStatus("GR")
                                .legalAidStatusDate("2010-10-10")
                                .build()))
                        .build())
                .build();
    }
}

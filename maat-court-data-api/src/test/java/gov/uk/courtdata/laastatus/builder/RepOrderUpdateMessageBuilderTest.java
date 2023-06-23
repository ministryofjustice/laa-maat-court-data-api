package gov.uk.courtdata.laastatus.builder;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.laastatus.*;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static gov.uk.courtdata.constants.CourtDataConstants.CDA_TRANSACTION_ID_HEADER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class
RepOrderUpdateMessageBuilderTest {

    @Mock
    private RepOrderCPDataRepository repOrderCPDataRepository;

    @Mock
    private SolicitorMAATDataRepository solicitorMAATDataRepository;

    @InjectMocks
    private RepOrderUpdateMessageBuilder repOrderUpdateMessageBuilder;


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

        gov.uk.courtdata.model.laastatus.Offence offence =
                laaStatusUpdate.getData().getAttributes().getOffences().iterator().next();

        assertAll("VerifyOffence",
                () -> assertNotNull(offence.getOffenceId()),
                () -> assertNotNull(offence.getStatusCode()),
                () -> assertNotNull(offence.getEffectiveEndDate()),
                () -> assertEquals("987", offence.getOffenceId()),
                () -> assertEquals("OK", offence.getStatusCode()),
                () -> assertEquals("2010-10-10", offence.getEffectiveEndDate())
        );

        DefendantData defData = laaStatusUpdate.getData().getRelationships().getDefendant().getData();

        assertAll("VerifyDefendant",
                () -> assertNotNull(defData.getId()),
                () -> assertNotNull(defData.getType()),
                () -> assertEquals("555666", defData.getId()),
                () -> assertEquals("defendants", defData.getType())
        );


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

        DefenceOrganisation defOrg = laaStatusUpdate.getData().getAttributes().getDefenceOrganisation();

        assertAll("VerifyDefenceOrganisation",
                () -> assertNotNull(defOrg.getLaaContractNumber()),
                () -> assertNotNull(defOrg.getOrganisation().getName()),
                () -> assertEquals("Solicitor Name Ltd", defOrg.getOrganisation().getName()),
                () -> assertEquals("456", defOrg.getLaaContractNumber())
        );

    }

    @Test
    public void testSolicitorAddress_IsAvailableOnLaaStatusRequest() {

        CaseDetails caseDetails = buildSampleCase();

        Optional<SolicitorMAATDataEntity> optSolicitor = buildCompleteSolicitorDetails();

        when(solicitorMAATDataRepository.findBymaatId(anyInt())).thenReturn(optSolicitor);

        LaaStatusUpdate laaStatusUpdate = repOrderUpdateMessageBuilder.build(caseDetails);

        Address address =
                laaStatusUpdate.getData().getAttributes()
                        .getDefenceOrganisation().getOrganisation().getAddress();

        assertAll("VerifyAddress",
                () -> assertNotNull(address),
                () -> assertNotNull(address.getAddress1()),
                () -> assertNotNull(address.getAddress2()),
                () -> assertNotNull(address.getAddress3()),
                () -> assertNotNull(address.getAddress4()),
                () -> assertNotNull(address.getPostcode()),
                () -> assertEquals(address.getAddress1(), optSolicitor.get().getLine1()),
                () -> assertEquals(address.getAddress2(), optSolicitor.get().getLine2()),
                () -> assertEquals(address.getAddress3(), optSolicitor.get().getLine3()),
                () -> assertEquals(address.getAddress4(), optSolicitor.get().getCity()),
                () -> assertEquals(address.getPostcode(), optSolicitor.get().getPostcode())
        );

    }

    @Test
    public void testSolicitorContact_isAvailableOnLaaStatusUpdate_Request() {


        CaseDetails caseDetails = buildSampleCase();

        Optional<SolicitorMAATDataEntity> optSolicitor = buildCompleteSolicitorDetails();

        when(solicitorMAATDataRepository.findBymaatId(anyInt())).thenReturn(optSolicitor);

        LaaStatusUpdate laaStatusUpdate = repOrderUpdateMessageBuilder.build(caseDetails);

        Contact contact =
                laaStatusUpdate.getData().getAttributes()
                        .getDefenceOrganisation().getOrganisation().getContact();

        assertAll("VerifyContact",
                () -> assertNotNull(contact),
                () -> assertNotNull(contact.getWork()),
                () -> assertNotNull(contact.getPrimaryEmail()),
                () -> assertNotNull(contact.getSecondaryEmail()),
                () -> assertEquals(contact.getWork(), optSolicitor.get().getPhone()),
                () -> assertEquals(contact.getPrimaryEmail(), optSolicitor.get().getAdminEmail()),
                () -> assertEquals(contact.getSecondaryEmail(), optSolicitor.get().getEmail())
        );

    }


    @Test
    public void givenCaseDetailsIsReceived_whenIsRepOrderUpdateMessageBuilderInvoked_thenHeaderDetailsAreReturned() {

        //given
        CaseDetails caseDetails = CaseDetails.builder().laaTransactionId(UUID.fromString("6f5b34ea-e038-4f1c-bfe5-d6bf622444f0")).build();
        CourtDataDTO courtDataDTO = CourtDataDTO.builder().caseDetails(caseDetails).txId(123456).build();

        //when
        Map<String, String> headers = repOrderUpdateMessageBuilder.buildHeaders(courtDataDTO);

        //then
        assertAll("VerifyHeaders",
                () -> assertEquals("6f5b34ea-e038-4f1c-bfe5-d6bf622444f0", headers.get(CDA_TRANSACTION_ID_HEADER)),
                () -> assertEquals("123456", headers.get("Laa-Status-Transaction-Id")));
    }


    @Test
    public void givenCaseDetailsIsReceived_whenIsRepOrderUpdateMessageBuilderInvoked_thenHeaderDetailsAreReturnedWithMullTxnID() {

        //given
        CaseDetails caseDetails = CaseDetails.builder().build();
        CourtDataDTO courtDataDTO = CourtDataDTO.builder().caseDetails(caseDetails).txId(123456).build();

        //when
        Map<String, String> headers = repOrderUpdateMessageBuilder.buildHeaders(courtDataDTO);

        //then
        assertAll("VerifyHeaders",
                () -> assertNull(headers.get(CDA_TRANSACTION_ID_HEADER)),
                () -> assertEquals("123456", headers.get("Laa-Status-Transaction-Id")));
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
                .defendant(
                        Defendant.builder()
                                .surname("Gregory")
                                .offences(Collections.singletonList(
                                        Offence.builder()
                                                .offenceCode("CJ01501")
                                                .offenceId("987")
                                                .legalAidStatus("GR")
                                                .legalAidStatusDate("2010-10-10")
                                                .build()))
                                .build())
                .build();
    }
}

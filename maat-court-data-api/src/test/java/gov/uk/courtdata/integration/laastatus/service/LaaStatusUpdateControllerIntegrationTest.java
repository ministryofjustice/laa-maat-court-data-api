package gov.uk.courtdata.integration.laastatus.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getAllServeEvents;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static gov.uk.courtdata.constants.CourtDataConstants.WQ_UPDATE_CASE_EVENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.CaseEntity;
import gov.uk.courtdata.entity.DefendantEntity;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.SessionEntity;
import gov.uk.courtdata.entity.SolicitorEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.WQStatus;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.laastatus.controller.LaaStatusUpdateController;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.MessageCollection;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.model.laastatus.Address;
import gov.uk.courtdata.model.laastatus.Attributes;
import gov.uk.courtdata.model.laastatus.Contact;
import gov.uk.courtdata.model.laastatus.DefenceOrganisation;
import gov.uk.courtdata.model.laastatus.DefendantData;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import gov.uk.courtdata.model.laastatus.Organisation;
import gov.uk.courtdata.model.laastatus.Relationships;
import gov.uk.courtdata.model.laastatus.RepOrderData;
import gov.uk.courtdata.util.QueueMessageLogTestHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class LaaStatusUpdateControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String LAA_TRANSACTION_ID = "b27b97e4-0514-42c4-8e09-fcc2c693e11f";
    private static final Integer TEST_CASE_ID = 42;
    private static final String TEST_ASN_SEQ = "001";
    private Integer testMaatId = 1234;

    @Autowired
    private LaaStatusUpdateController laaStatusUpdateController;

    private QueueMessageLogTestHelper queueMessageLogTestHelper;

    @BeforeEach
    void setUp() {
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        setupCdaWebServer();
        queueMessageLogTestHelper = new QueueMessageLogTestHelper(repos.queueMessageLog);
    }

    @Test
    void givenNullMaatIdInCaseDetails_whenUpdateLAAStatusIsInvoked_theCorrectErrorIsReturned() throws Exception {
        String testPayload = objectMapper.writeValueAsString(CaseDetails.builder()
                .laaTransactionId(UUID.fromString(LAA_TRANSACTION_ID))
                .build());
        assertThat(runServerErrorScenario(
                "MAAT API Call failed - MAAT/REP ID is required, found [null]", getPostRequest(testPayload)))
                .isTrue();
    }

    @Test
    void givenAMissingMaatIdInCaseDetails_whenUpdateLAAStatusIsInvoked_theCorrectErrorIsReturned()
            throws Exception {
        String payloadMissingMaatId = String.format("{\"laaTransactionId\":\"%s\"}", LAA_TRANSACTION_ID);
        assertThat(runServerErrorScenario(
                "MAAT API Call failed - MAAT/REP ID is required, found [null]", getPostRequest(payloadMissingMaatId)))
                .isTrue();
    }

    @Test
    void givenAnInvalidMaatIdInCaseDetails_whenUpdateLAAStatusIsInvoked_theCorrectErrorIsReturned()
            throws Exception {
        runValidationFailureScenario(String.format("MAAT API Call failed - MAAT/REP ID [%d] is invalid", testMaatId));
        assertThat(wireMock().getAllServeEvents()).isEmpty();
        verify(exactly(0), postRequestedFor(urlEqualTo("/oauth2/token")));
        verify(exactly(0), postRequestedFor(urlEqualTo("/api/internal/v1/representation_orders")));
    }

    @Test
    void givenAMaatIdThatIsNotLinked_whenUpdateLAAStatusIsInvoked_theCorrectErrorIsReturned() throws Exception {
        createTestRepoOrder();
        runValidationFailureScenario(String.format("MAAT API Call failed - MAAT Id : %s not linked.", testMaatId));
        assertThat(wireMock().getAllServeEvents()).isEmpty();
    }

    @Test
    void givenAMaatIdWithMultipleLinks_whenUpdateLAAStatusIsInvoked_theCorrectErrorIsReturned()
            throws Exception {
        createTestRepoOrder();
        createTestLinkData(2);
        runValidationFailureScenario(
                String.format("MAAT API Call failed - Multiple Links found for  MAAT Id : %s", testMaatId));
        assertThat(wireMock().getAllServeEvents()).isEmpty();
    }

    @Test
    void givenAMaatIdNoLinkedSolicitor_whenUpdateLAAStatusIsInvoked_theCorrectErrorIsReturned()
            throws Exception {
        createTestRepoOrder();
        createTestLinkData(1);
        runValidationFailureScenario(
                String.format("MAAT API Call failed - Solicitor not found for maatId %s", testMaatId));
        assertThat(wireMock().getAllServeEvents()).isEmpty();
    }

    @Test
    void
            givenAMaatIdWhereTheLinkedSolicitorHasNoAccountCode_whenUpdateLAAStatusIsInvoked_theCorrectErrorIsReturned()
                    throws Exception {
        createTestRepoOrder();
        createTestLinkData(1);
        createSolicitorData("");
        createDefendantData();
        runValidationFailureScenario(String.format(
                "MAAT API Call failed - Solicitor account code not available for maatId %s.", testMaatId));
        assertThat(wireMock().getAllServeEvents()).isEmpty();
    }

    @Test
    void givenAMaatIdWithNoLinkedDefendant_whenUpdateLAAStatusIsInvoked_theCorrectErrorIsReturned()
            throws Exception {
        createTestRepoOrder();
        createTestLinkData(1);
        createSolicitorData("test-account-code");
        runValidationFailureScenario("MAAT API Call failed - MAAT Defendant details not found.");
        assertThat(wireMock().getAllServeEvents()).isEmpty();
    }

    @Test
    void
            givenCaseDetailsWithOffencesContainingInvalidLaaStatuses_whenUpdateLAAStatusIsInvoked_theCorrectErrorIsReturned()
                    throws Exception {
        createTestRepoOrder();
        createTestLinkData(1);
        createSolicitorData("test-account-code");
        createDefendantData();

        List<String> expectedErrorMessages = new ArrayList<>();

        Offence offenceOne = Offence.builder()
                .asnSeq(TEST_ASN_SEQ)
                .iojDecision(0)
                .legalAidStatus("GR")
                .build();
        expectedErrorMessages.add(String.format(
                "Cannot Grant Legal Aid on a Failed or Pending IOJ - See offence %s", offenceOne.getAsnSeq()));

        Offence offenceTwo = Offence.builder()
                .asnSeq(TEST_ASN_SEQ)
                .iojDecision(1)
                .legalAidStatus("FJ")
                .build();
        expectedErrorMessages.add(
                String.format("Cannot Pass IOJ and Fail Legal Aid on IOJ - See offence %s", offenceTwo.getAsnSeq()));

        Offence offenceThree = Offence.builder()
                .asnSeq(TEST_ASN_SEQ)
                .iojDecision(3)
                .legalAidStatus("GR")
                .build();
        expectedErrorMessages.add(
                String.format("Cannot Grant Legal Aid on a n/a IOJ - See offence %s", offenceThree.getAsnSeq()));

        Defendant defendantWithInvalidLaaStatuses = Defendant.builder()
                .offences(List.of(offenceOne, offenceTwo, offenceThree))
                .build();
        String testPayload = generateCaseDetailsJsonPayload(CaseDetails.builder()
                .laaTransactionId(UUID.fromString(LAA_TRANSACTION_ID))
                .maatId(testMaatId)
                .defendant(defendantWithInvalidLaaStatuses)
                .build());

        runSuccessScenario(
                MessageCollection.builder().messages(expectedErrorMessages).build(), getPostRequest(testPayload));

        queueMessageLogTestHelper.assertQueueMessageLogged(testPayload, 1, LAA_TRANSACTION_ID, testMaatId);
    }

    @Test
    void givenValidCaseDetails_whenUpdateLAAStatusIsInvoked_theUpdateIsPerformedCorrectly() throws Exception {
        runValidCaseDetailsScenario(false);
    }

    @Test
    void givenValidCaseDetailsWithCdaOnlySet_whenUpdateLAAStatusIsInvoked_onlyCdaUpdatesArePerformed()
            throws Exception {
        runValidCaseDetailsScenario(true);
    }

    private void runValidCaseDetailsScenario(Boolean cdaOnly) throws Exception {
        createTestRepoOrder();
        List<WqLinkRegisterEntity> linkEntities = createTestLinkData(1);
        SolicitorMAATDataEntity solicitorMAATDataEntity = createSolicitorData("test-account-code");
        DefendantMAATDataEntity defendantMAATDataEntity = createDefendantData();
        RepOrderCPDataEntity repOrderCPDataEntity = createRepOrderCPData();

        Offence validOffence = Offence.builder()
                .asnSeq(TEST_ASN_SEQ)
                .iojDecision(1)
                .legalAidStatus("AP")
                .offenceCode("TH68001")
                .build();
        Defendant validDefendant = Defendant.builder()
                .offences(List.of(validOffence))
                .forename("first-name")
                .surname("last-name")
                .build();

        List<Session> sessions = List.of(
                Session.builder()
                        .courtLocation("Location 1")
                        .dateOfHearing(LocalDate.of(1, 1, 1).toString())
                        .postHearingCustody("A")
                        .build(),
                Session.builder().courtLocation("Location 2").build());

        OffenceEntity offenceEntity = createOffenceData(
                linkEntities.getFirst().getCaseId(), validOffence.getOffenceCode(), validOffence.getLegalAidStatus());

        CaseDetails inputCaseDetails = CaseDetails.builder()
                .laaTransactionId(UUID.fromString(LAA_TRANSACTION_ID))
                .maatId(testMaatId)
                .isActive(true)
                .cjsAreaCode("1")
                .createdUser("test-user")
                .defendant(validDefendant)
                .docLanguage("ENG")
                .category(3)
                .caseCreationDate(LocalDate.of(1, 1, 1).toString())
                .sessions(sessions)
                .onlyForCDAService(cdaOnly)
                .build();

        String testPayload = generateCaseDetailsJsonPayload(inputCaseDetails);

        runSuccessScenario(
                MessageCollection.builder().messages(new ArrayList<>()).build(), getPostRequest(testPayload));

        queueMessageLogTestHelper.assertQueueMessageLogged(testPayload, 2, LAA_TRANSACTION_ID, testMaatId);
        assertCdaCalledCorrectly(inputCaseDetails, offenceEntity, solicitorMAATDataEntity, repOrderCPDataEntity);

        if (cdaOnly) assertMlaUpdatesNotPerformed(linkEntities.getFirst());
        else
            assertMlaUpdatesPerformedCorrectly(
                    inputCaseDetails, linkEntities.getFirst(), solicitorMAATDataEntity, defendantMAATDataEntity);
    }

    private void runValidationFailureScenario(String expectedErrorMessage) throws Exception {
        String testPayload = generateCaseDetailsJsonPayload(CaseDetails.builder()
                .laaTransactionId(UUID.fromString(LAA_TRANSACTION_ID))
                .maatId(testMaatId)
                .build());
        assertThat(runServerErrorScenario(expectedErrorMessage, getPostRequest(testPayload))).isTrue();
        queueMessageLogTestHelper.assertQueueMessageLogged(testPayload, 1, LAA_TRANSACTION_ID, testMaatId);
    }

    private void assertMlaUpdatesPerformedCorrectly(
            CaseDetails inputCaseDetails,
            WqLinkRegisterEntity wqLinkRegisterEntity,
            SolicitorMAATDataEntity solicitorMAATDataEntity,
            DefendantMAATDataEntity defendantMAATDataEntity) {
        int expectedTxId = repos.identifier.getTxnID() - 1;

        List<CaseEntity> createdCaseEntities = repos.caseRepository.findAll();
        List<WqCoreEntity> createdWqCoreEntities = repos.wqCore.findAll();
        List<WqLinkRegisterEntity> wqLinkRegisterEntities =
                repos.wqLinkRegister.findBymaatId(inputCaseDetails.getMaatId());
        List<SolicitorEntity> createdSolicitorEntities = repos.solicitor.findAll();
        List<DefendantEntity> createdDefendantEntities = repos.defendant.findAll();
        List<SessionEntity> createdSessionEntities = repos.session.findAll();
        Offence inputOffence = inputCaseDetails.getDefendant().getOffences().getFirst();
        OffenceEntity offenceEntity = repos.offence.findAll().stream()
                .filter(item -> item.getTxId() == expectedTxId)
                .toList()
                .getFirst();

        SoftAssertions.assertSoftly(softly -> {
            // Check case data.
            assertThat(createdCaseEntities).hasSize(1);
            CaseEntity caseEntity = createdCaseEntities.getFirst();
            assertThat(caseEntity.getTxId()).isEqualTo(expectedTxId);
            assertThat(caseEntity.getCaseId()).isEqualTo(TEST_CASE_ID.intValue());
            assertThat(caseEntity.getCjsAreaCode()).isEqualTo(String.format("0%s", inputCaseDetails.getCjsAreaCode()));
            assertThat(caseEntity.getInactive()).isEqualTo(inputCaseDetails.isActive() ? "N" : "Y");
            assertThat(caseEntity.getLibraCreationDate()).isNotNull();
            assertThat(caseEntity.getDocLanguage()).isEqualTo(inputCaseDetails.getDocLanguage());
            assertThat(caseEntity.getProceedingId())
                    .isEqualTo(wqLinkRegisterEntity.getProceedingId().intValue());
            // Check work queue core data.
            assertThat(createdWqCoreEntities).hasSize(1);
            WqCoreEntity wqCoreEntity = createdWqCoreEntities.getFirst();
            assertThat(wqCoreEntity.getTxId().intValue()).isEqualTo(expectedTxId);
            assertThat(wqCoreEntity.getCaseId()).isEqualTo(TEST_CASE_ID.intValue());
            assertThat(wqCoreEntity.getCreatedTime()).isNotNull();
            assertThat(wqCoreEntity.getProcessedTime()).isNotNull();
            assertThat(wqCoreEntity.getCreatedUserId()).isEqualTo(inputCaseDetails.getCreatedUser());
            assertThat(wqCoreEntity.getWqType()).isEqualTo(WQ_UPDATE_CASE_EVENT);
            assertThat(wqCoreEntity.getWqStatus().intValue()).isEqualTo(WQStatus.WAITING.value());
            // Check work queue link register data.
            assertThat(wqLinkRegisterEntities).hasSize(1);
            WqLinkRegisterEntity updatedWqLinkRegisterEntity = wqLinkRegisterEntities.getFirst();
            assertThat(updatedWqLinkRegisterEntity.getMlrCat()).isEqualTo(inputCaseDetails.getCategory());
            // Check solicitor data.
            assertThat(createdSolicitorEntities).hasSize(1);
            SolicitorEntity solicitorEntity = createdSolicitorEntities.getFirst();
            assertThat(solicitorEntity.getTxId().intValue()).isEqualTo(expectedTxId);
            assertThat(solicitorEntity.getCaseId()).isEqualTo(TEST_CASE_ID);
            assertThat(solicitorEntity.getFirmName()).isEqualTo(solicitorMAATDataEntity.getAccountName());
            assertThat(solicitorEntity.getContactName()).isEqualTo(solicitorMAATDataEntity.getSolicitorName());
            assertThat(solicitorEntity.getLaaOfficeAccount()).isEqualTo(solicitorMAATDataEntity.getAccountCode());
            // Check defendant data.
            assertThat(createdDefendantEntities).hasSize(1);
            DefendantEntity defendantEntity = createdDefendantEntities.getFirst();
            assertThat(defendantEntity.getTxId().intValue()).isEqualTo(expectedTxId);
            assertThat(defendantEntity.getCaseId()).isEqualTo(TEST_CASE_ID);
            assertThat(defendantEntity.getForename())
                    .isEqualTo(inputCaseDetails.getDefendant().getForename());
            assertThat(defendantEntity.getSurname())
                    .isEqualTo(inputCaseDetails.getDefendant().getSurname());
            assertThat(defendantEntity.getUseSol()).isEqualTo(defendantMAATDataEntity.getUseSol());
            // Check session data
            assertThat(createdSessionEntities).hasSize(1);
            SessionEntity sessionEntity = createdSessionEntities.getFirst();
            assertThat(sessionEntity.getTxId().intValue()).isEqualTo(expectedTxId);
            assertThat(sessionEntity.getCaseId()).isEqualTo(TEST_CASE_ID);
            assertThat(sessionEntity.getDateOfHearing()).isNotNull();
            assertThat(sessionEntity.getCourtLocation())
                    .isEqualTo(inputCaseDetails.getSessions().getFirst().getCourtLocation());
            // Check offence data
            assertThat(offenceEntity.getTxId().intValue()).isEqualTo(expectedTxId);
            assertThat(offenceEntity.getCaseId()).isEqualTo(TEST_CASE_ID);
            assertThat(offenceEntity.getAsnSeq()).isEqualTo(TEST_ASN_SEQ);
            assertThat(offenceEntity.getOffenceCode()).isEqualTo(inputOffence.getOffenceCode());
            assertThat(offenceEntity.getLegalAidStatus()).isEqualTo(inputOffence.getLegalAidStatus());
        });
    }

    private void assertMlaUpdatesNotPerformed(WqLinkRegisterEntity initialWqLinkRegisterEntity) {
        SoftAssertions.assertSoftly(softly -> {
            assertThat(repos.caseRepository.findAll()).isEmpty();
            assertThat(repos.wqCore.findAll()).isEmpty();
            assertThat(repos.solicitor.findAll()).isEmpty();
            assertThat(repos.defendant.findAll()).isEmpty();
            assertThat(repos.session.findAll()).isEmpty();
            assertThat(repos.offence.findAll()).hasSize(1);
            assertThat(repos.wqLinkRegister
                            .findBymaatId(initialWqLinkRegisterEntity.getMaatId())
                            .getFirst()
                            .getMlrCat())
                    .isEqualTo(initialWqLinkRegisterEntity.getMlrCat());
        });
    }

    private void assertCdaCalledCorrectly(
            CaseDetails inputCaseDetails,
            OffenceEntity offence,
            SolicitorMAATDataEntity solicitor,
            RepOrderCPDataEntity repOrderCPDataEntity)
            throws JsonProcessingException {

        verify(exactly(1), postRequestedFor(urlEqualTo("/oauth2/token")));
        verify(exactly(1), postRequestedFor(urlEqualTo("/api/internal/v1/representation_orders")));

        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        String expectedCdaPostBody = objectMapper.writeValueAsString(
                generateExpectedLaaStatusObject(inputCaseDetails, offence, solicitor, repOrderCPDataEntity));
        List<ServeEvent> allServeEvents = getAllServeEvents();
        assertThat(allServeEvents.getFirst().getRequest().getBodyAsString()).isEqualTo(expectedCdaPostBody);
    }

    private String generateCaseDetailsJsonPayload(CaseDetails inputCaseDetails) throws JsonProcessingException {
        String testPayload = objectMapper.writeValueAsString(inputCaseDetails);
        return testPayload.replace("\"active\"", "\"isActive\"");
    }

    private DefendantMAATDataEntity createDefendantData() {
        DefendantMAATDataEntity defendantMAATDataEntity = DefendantMAATDataEntity.builder()
                .maatId(testMaatId)
                .useSol("use-sol")
                .build();
        repos.defendantMAATData.save(defendantMAATDataEntity);
        return defendantMAATDataEntity;
    }

    private RepOrderCPDataEntity createRepOrderCPData() {
        RepOrderCPDataEntity repOrderCPDataEntity = RepOrderCPDataEntity.builder()
                .repOrderId(testMaatId)
                .defendantId("defendant-id")
                .build();
        repos.repOrderCPData.save(repOrderCPDataEntity);
        return repOrderCPDataEntity;
    }

    private OffenceEntity createOffenceData(Integer caseId, String offenceCode, String laaStatus) {
        OffenceEntity offenceEntity = OffenceEntity.builder()
                .txId(1)
                .caseId(caseId)
                .offenceId("offence-id")
                .offenceCode(offenceCode)
                .asnSeq(TEST_ASN_SEQ)
                .legalAidStatus(laaStatus)
                .build();
        repos.offence.save(offenceEntity);
        return offenceEntity;
    }

    private void createTestRepoOrder() {
        RepOrderEntity repOrderEntity = TestEntityDataBuilder.getPopulatedRepOrder();
        repOrderEntity.setCaseId(TEST_CASE_ID.toString());
        RepOrderEntity repOrder = repos.repOrder.save(repOrderEntity);
        testMaatId = repOrder.getId();
    }

    private SolicitorMAATDataEntity createSolicitorData(String accountCode) {
        SolicitorMAATDataEntity solicitor = SolicitorMAATDataEntity.builder()
                .maatId(testMaatId)
                .solicitorName("test-solicitor")
                .accountCode(accountCode)
                .accountName("test-account")
                .build();
        repos.solicitorMAATData.save(solicitor);
        return solicitor;
    }

    private List<WqLinkRegisterEntity> createTestLinkData(Integer numberOfLinks) {
        List<WqLinkRegisterEntity> linkItems = new ArrayList<>();
        for (int i = 0; i < numberOfLinks; i++)
            linkItems.add(WqLinkRegisterEntity.builder()
                    .createdTxId(i)
                    .maatId(testMaatId)
                    .libraId(String.format("CP%d", i))
                    .proceedingId(i)
                    .caseId(TEST_CASE_ID)
                    .mlrCat(3)
                    .build());
        repos.wqLinkRegister.saveAll(linkItems);
        return linkItems;
    }

    private void setupCdaWebServer() {
        wireMock()
                .stubFor(WireMock.post(urlEqualTo("/api/internal/v1/representation_orders"))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON))));
    }

    private LaaStatusUpdate generateExpectedLaaStatusObject(
            CaseDetails inputCaseDetails,
            OffenceEntity offence,
            SolicitorMAATDataEntity solicitor,
            RepOrderCPDataEntity repOrderCPDataEntity) {

        gov.uk.courtdata.model.laastatus.Offence mappedOffence = gov.uk.courtdata.model.laastatus.Offence.builder()
                .offenceId(offence.getOffenceId())
                .statusCode(offence.getLegalAidStatus())
                .build();

        DefenceOrganisation defenceOrganisation = DefenceOrganisation.builder()
                .laaContractNumber(solicitor.getAccountCode())
                .organisation(Organisation.builder()
                        .address(Address.builder().build())
                        .contact(Contact.builder().build())
                        .name(solicitor.getAccountName())
                        .build())
                .build();

        Organisation.builder()
                .address(Address.builder().build())
                .contact(Contact.builder().build())
                .name(solicitor.getAccountName())
                .build();

        Relationships relationships = Relationships.builder()
                .defendant(gov.uk.courtdata.model.laastatus.Defendant.builder()
                        .data(DefendantData.builder()
                                .id(repOrderCPDataEntity.getDefendantId())
                                .type("defendants")
                                .build())
                        .build())
                .build();

        return LaaStatusUpdate.builder()
                .data(RepOrderData.builder()
                        .type("representation_order")
                        .attributes(Attributes.builder()
                                .maatReference(inputCaseDetails.getMaatId())
                                .defenceOrganisation(defenceOrganisation)
                                .offences(List.of(mappedOffence))
                                .build())
                        .relationships(relationships)
                        .build())
                .build();
    }

    private MockHttpServletRequestBuilder getPostRequest(String payload) {
        return post("/maatApi/laaStatus")
                .contentType(MediaType.APPLICATION_JSON)
                .header("laa-transaction-id", LAA_TRANSACTION_ID)
                .content(payload);
    }
}

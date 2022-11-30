package gov.uk.courtdata.integration.hearing;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.enums.*;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.hearing.service.HearingResultedListener;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.QueueMessageLogTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static gov.uk.courtdata.constants.CourtDataConstants.*;
import static gov.uk.courtdata.enums.WQStatus.WAITING;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class HearingResultedListenerIntegrationTest {

    private final String LAA_TRANSACTION_ID = "b27b97e4-0514-42c4-8e09-fcc2c693e11f";
    private final Integer TEST_MAAT_ID = 1234;
    private final Integer TEST_CASE_ID = 1;
    private final String TEST_USER = "test-user";
    private final LocalDate TEST_CREATED_DATE = LocalDate.of(2022, 1, 1);
    private final Integer TEST_APPLICATION_FLAG = 1;

    @Autowired
    private IdentifierRepository identifierRepository;
    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Autowired
    private XLATResultRepository xlatResultRepository;
    @Autowired
    private XLATOffenceRepository xlatOffenceRepository;
    @Autowired
    private WqCoreRepository wqCoreRepository;
    @Autowired
    private OffenceRepository offenceRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private WQResultRepository wqResultRepository;
    @Autowired
    private WQHearingRepository wqHearingRepository;
    @Autowired
    private QueueMessageLogRepository queueMessageLogRepository;
    @Autowired
    private RepOrderRepository repOrderRepository;
    @Autowired
    private WQCaseRepository wqCaseRepository;
    @Autowired
    private WQSessionRepository wqSessionRepository;
    @Autowired
    private WQDefendantRepository wqDefendantRepository;
    @Autowired
    private WQOffenceRepository wqOffenceRepository;
    @Autowired
    private HearingResultedListener hearingResultedListener;
    @Autowired
    protected ObjectMapper objectMapper;

    private QueueMessageLogTestHelper queueMessageLogTestHelper;

    @Before
    public void setUp() {
        identifierRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();
        xlatResultRepository.deleteAll();
        xlatOffenceRepository.deleteAll();
        wqCoreRepository.deleteAll();
        offenceRepository.deleteAll();
        resultRepository.deleteAll();
        wqResultRepository.deleteAll();
        wqHearingRepository.deleteAll();
        queueMessageLogRepository.deleteAll();
        wqCaseRepository.deleteAll();
        wqSessionRepository.deleteAll();
        wqDefendantRepository.deleteAll();
        wqOffenceRepository.deleteAll();

        setupTestData();
        queueMessageLogTestHelper = new QueueMessageLogTestHelper(queueMessageLogRepository);
    }

    @Test
    public void givenAHearingMessageWithNoMaatId_whenMessageIsReceived_thenErrorIsReturned() {
        String payloadMissingMaatId = String.format("{\"laaTransactionId\":\"%s\"}", LAA_TRANSACTION_ID);
        ValidationException error = Assert.assertThrows(
                ValidationException.class, () -> hearingResultedListener.receive(payloadMissingMaatId));

        Assert.assertEquals("MAAT ID is required.", error.getMessage());
        queueMessageLogTestHelper.assertQueueMessageLogged(
                payloadMissingMaatId, 1, LAA_TRANSACTION_ID, -1);}

    @Test
    public void givenAHearingMessageWithAZeroMaatId_whenMessageIsReceived_thenErrorIsReturned() throws JsonProcessingException {
        HearingResulted testData = HearingResulted.builder()
                .maatId(0)
                .laaTransactionId(UUID.fromString(LAA_TRANSACTION_ID))
                .jurisdictionType(JurisdictionType.CROWN).build();

        runValidationErrorScenario(testData, "MAAT ID is required.");
    }

    @Test
    public void givenAHearingMessageWithAMaatIdThatDoesNotExist_whenMessageIsReceived_thenErrorIsReturned() throws JsonProcessingException {
        Integer invalidMaatId = 999;
        HearingResulted testData = HearingResulted.builder()
                .maatId(invalidMaatId)
                .laaTransactionId(UUID.fromString(LAA_TRANSACTION_ID))
                .jurisdictionType(JurisdictionType.CROWN).build();

        runValidationErrorScenario(testData, String.format("MAAT/REP ID: %d is invalid.", invalidMaatId));
    }

    @Test
    public void givenAValidHearingMessageWhereTheMaatIdIsNotLinked_whenMessageIsReceived_thenErrorIsReturned() throws JsonProcessingException {
        HearingResulted testData = HearingResulted.builder()
                .maatId(TEST_MAAT_ID)
                .laaTransactionId(UUID.fromString(LAA_TRANSACTION_ID))
                .jurisdictionType(JurisdictionType.CROWN).build();

        runMaatErrorScenario(testData, String.format("MAAT Id : %s not linked.", TEST_MAAT_ID));
    }

    @Test
    public void givenAHearingMessageWhereTheMaatIdHasMultipleLinks_whenMessageIsReceived_thenErrorIsReturned() throws JsonProcessingException {
        wqLinkRegisterRepository.saveAll(List.of(
                WqLinkRegisterEntity.builder().createdTxId(1).maatId(TEST_MAAT_ID).caseId(1).build(),
                WqLinkRegisterEntity.builder().createdTxId(2).maatId(TEST_MAAT_ID).caseId(2).build()
        ));

        HearingResulted testData = HearingResulted.builder()
                .maatId(TEST_MAAT_ID)
                .laaTransactionId(UUID.fromString(LAA_TRANSACTION_ID))
                .jurisdictionType(JurisdictionType.CROWN).build();

        runMaatErrorScenario(testData, String.format("Multiple Links found for  MAAT Id : %s", TEST_MAAT_ID));
    }

    @Test
    public void givenAHearingMessageWhereTheOffenceCodeIsNull_whenMessageIsReceived_thenErrorIsReturned() throws JsonProcessingException {
        wqLinkRegisterRepository.save(WqLinkRegisterEntity.builder().createdTxId(1).maatId(TEST_MAAT_ID).caseId(1).build());

        List<Offence> testOffences = getTestOffences();
        testOffences.get(0).setOffenceCode(null);

        HearingResulted testData = getTemplateResultedHearingData();
        testData.getDefendant().setOffences(testOffences);

        runMaatErrorScenario(testData, "A Null Offence Code is passed in");
    }

    @Test
    public void givenAHearingMessageContainingANullResultCode_whenMessageIsReceived_thenTheCorrectDataIsPersisted() throws JsonProcessingException {
        List<Offence> testOffences = getTestOffences();
        testOffences.get(0).getResults().get(0).setResultCode(null);

        HearingResulted testData = getTemplateResultedHearingData();
        testData.getDefendant().setOffences(testOffences);

        runSuccessScenario(testData, true, true, true);
    }

    @Test
    public void givenAHearingMessageWithExistingResultAndOffenceCodes_whenMessageIsReceived_thenTheCorrectDataIsPersisted() throws JsonProcessingException {

        HearingResulted testData = getTemplateResultedHearingData();

        testData.getDefendant().getOffences().forEach(offence -> {
            createXlatOffenceData(offence);
            createXlatResultData(offence.getResults());
        });

        runSuccessScenario(testData, false, false, true);
    }

    @Test
    public void givenAValidHearingMessageOfTypeOffenceWithNoWqProcessing_whenMessageIsReceived_thenTheCorrectDataIsPersisted() throws JsonProcessingException {

        HearingResulted testData = getTemplateResultedHearingData();
        testData.setJurisdictionType(JurisdictionType.CROWN);

        testData.getDefendant().getOffences().forEach(offence -> {
            String asnSeq = String.format(LEADING_ZERO_3, Integer.parseInt(offence.getAsnSeq()));
            offenceRepository.save(OffenceEntity.builder()
                    .txId(identifierRepository.getTxnID())
                    .caseId(TEST_CASE_ID)
                    .asnSeq(asnSeq).build());
            createXlatResultData(offence.getResults());
        });

        runSuccessScenario(testData, true, false, false);
    }

    @Test
    public void givenAValidHearingRequiringCourtPreProcessing_whenMessageIsReceived_thenTheCorrectDataIsPersisted() throws JsonProcessingException {

        HearingResulted testData = getTemplateResultedHearingData();
        testData.setFunctionType(FunctionType.APPLICATION);

        testData.getDefendant().getOffences().forEach(offence -> {
            createXlatOffenceData(offence);
            offenceRepository.save(OffenceEntity.builder()
                    .txId(identifierRepository.getTxnID())
                    .caseId(TEST_CASE_ID)
                    .applicationFlag(TEST_APPLICATION_FLAG)
                    .asnSeq(generateTestAsnSeq(offence))
                    .offenceId(offence.getOffenceId()).build());
        });

        runSuccessScenario(testData, false, true, true);
    }

    @Test
    public void givenAValidHearingRequiringCourtPreProcessingWithNoExistingApplication_whenMessageIsReceived_thenTheCorrectDataIsPersisted() throws JsonProcessingException {

        HearingResulted testData = getTemplateResultedHearingData();
        testData.setFunctionType(FunctionType.APPLICATION);

        testData.getDefendant().getOffences().forEach(offence -> {
            offence.setAsnSeq(null);
            createXlatOffenceData(offence);
        });

        runSuccessScenario(testData, false, true, true);
    }

    private String generateTestAsnSeq(Offence offence) {
        return String.format("123%s", offence.getOffenceId());
    }

    private HearingResulted getTemplateResultedHearingData() {
        return HearingResulted.builder()
                .maatId(TEST_MAAT_ID)
                .laaTransactionId(UUID.fromString(LAA_TRANSACTION_ID))
                .hearingId(UUID.randomUUID())
                .functionType(FunctionType.OFFENCE)
                .jurisdictionType(JurisdictionType.MAGISTRATES)
                .session(Session.builder()
                        .courtLocation("test-court")
                        .dateOfHearing(LocalDate.now().toString())
                        .sessionValidateDate(LocalDate.now().toString())
                        .build())
                .defendant(Defendant.builder()
                        .dateOfBirth(LocalDate.now().toString())
                        .offences(getTestOffences()).build())
                .build();
    }

    private void runSuccessScenario(HearingResulted testData, Boolean offencesCreated, Boolean resultsCreated, Boolean wqProcessingExpected) throws JsonProcessingException {
        WqLinkRegisterEntity linkRegisterEntity =
                wqLinkRegisterRepository.save(
                        WqLinkRegisterEntity.builder().createdTxId(1).maatId(TEST_MAAT_ID).caseId(TEST_CASE_ID).proceedingId(1).build());

        String messageBlob = objectMapper.writeValueAsString(testData);
        hearingResultedListener.receive(messageBlob);

        if (testData.getFunctionType() == FunctionType.APPLICATION) {
            modifyTestDataForCourtPreProcessing(testData);
        }

        queueMessageLogTestHelper.assertQueueMessageLogged(
                messageBlob, 1, testData.getLaaTransactionId().toString(), testData.getMaatId());

        assertWqHearingRepositoryUpdated(testData);

        assertXlatOffencesCreated(testData.getDefendant().getOffences(), offencesCreated);
        assertXlatResultsCorrect(testData.getDefendant().getOffences(), resultsCreated);

        if (wqProcessingExpected) {
            assertWqProcessingCorrect(testData, linkRegisterEntity);
        } else {
            assertNoWqProcessingChanges();
        }
    }

    private void assertNoWqProcessingChanges() {
        assertThat(wqCaseRepository.findAll().size()).isEqualTo(0);
        assertThat(wqSessionRepository.findAll().size()).isEqualTo(0);
        assertThat(wqDefendantRepository.findAll().size()).isEqualTo(0);
        assertThat(wqResultRepository.findAll().size()).isEqualTo(0);
        assertThat(wqOffenceRepository.findAll().size()).isEqualTo(0);
        assertThat(wqCoreRepository.findAll().size()).isEqualTo(0);
    }

    private void assertWqProcessingCorrect(HearingResulted hearingResultedData, WqLinkRegisterEntity linkRegisterEntity) {
        Integer currentTxId = identifierRepository.getTxnID();
        Integer resultsCount = (int) hearingResultedData.getDefendant().getOffences().stream()
                .mapToLong(offence -> offence.getResults().size()).sum();
        AtomicReference<Integer> expectedTxId = new AtomicReference<>(currentTxId - resultsCount);

        hearingResultedData.getDefendant().getOffences().forEach(offence -> offence.getResults().forEach(result -> {
            assertWqCaseProcessingCorrect(hearingResultedData, linkRegisterEntity, expectedTxId.get());
            assertWqSessionProcessingCorrect(hearingResultedData.getSession(), linkRegisterEntity, expectedTxId.get());
            assertWqDefendantProcessingCorrect(hearingResultedData.getDefendant(), linkRegisterEntity, expectedTxId.get());

            boolean extendedProcessing = offenceRepository.getOffenceCountForAsnSeq(
                    linkRegisterEntity.getCaseId(),
                    String.format(LEADING_ZERO_3, Integer.parseInt(offence.getAsnSeq()))) == 0;

            Boolean newOffence = hearingResultedData.getJurisdictionType() == JurisdictionType.CROWN && extendedProcessing;
            assertWqOffenceProcessingCorrect(offence, linkRegisterEntity, expectedTxId.get(), newOffence);
            assertWqResultProcessingCorrect(hearingResultedData, offence, result, linkRegisterEntity, expectedTxId.get());
            assertWqCoreProcessingCorrect(
                    hearingResultedData, linkRegisterEntity, Integer.parseInt(result.getResultCode()), expectedTxId.get(), extendedProcessing);
            expectedTxId.getAndSet(expectedTxId.get() + 1);
        }));

    }

    private void modifyTestDataForCourtPreProcessing(HearingResulted hearingResultedData) {
        hearingResultedData.setInActive(NO);
        AtomicReference<Integer> offenceCount = new AtomicReference<>(0);

        hearingResultedData.getDefendant().getOffences().forEach(offence -> {
            int systemGeneratedAsnSeq = APPLICATION_ASN_SEQ_INITIAL_VALUE + offenceCount.get();
            offence.setApplicationFlag(TEST_APPLICATION_FLAG);
            offence.setOffenceClassification(ApplicationClassification.getDescriptionByCode(offence.getOffenceClassification()));
            offence.setModeOfTrial(ModeOfTrial.NO_MODE_OF_TRAIL.value());
            offence.setAsnSeq(offence.getAsnSeq() == null ? Integer.toString(systemGeneratedAsnSeq) : generateTestAsnSeq(offence));
            offenceCount.getAndSet(offenceCount.get() + 1);
        });
    }

    private void assertWqResultProcessingCorrect(HearingResulted hearingResultedData, Offence offence, Result result, WqLinkRegisterEntity linkRegisterEntity, Integer expectedTxId) {

        WQResultEntity resultEntity = wqResultRepository.getById(expectedTxId);
        assertThat(resultEntity).isNotNull();
        assertThat(resultEntity.getCaseId()).isEqualTo(linkRegisterEntity.getCaseId());
        assertThat(resultEntity.getAsn()).isEqualTo(hearingResultedData.getAsn());
        assertThat(resultEntity.getAsnSeq()).isEqualTo(offence.getAsnSeq());
        assertThat(resultEntity.getResultCode().toString()).isEqualTo(result.getResultCode());
        assertThat(resultEntity.getResultShortTitle()).isEqualTo(result.getResultShortTitle());
        assertThat(resultEntity.getResultText()).isEqualTo(result.getResultText());
        assertThat(resultEntity.getResultCodeQualifiers()).isEqualTo(result.getResultCodeQualifiers());
        assertThat(resultEntity.getNextHearingDate().toString()).isEqualTo(result.getNextHearingDate());
        assertThat(resultEntity.getNextHearingLocation()).isEqualTo(result.getNextHearingLocation());
        assertThat(resultEntity.getFirmName()).isEqualTo(result.getFirstName());
        assertThat(resultEntity.getContactName()).isEqualTo(result.getContactName());
        assertThat(resultEntity.getLaaOfficeAccount()).isEqualTo(result.getLaaOfficeAccount());
        assertThat(resultEntity.getFirmName()).isEqualTo(result.getFirstName());
        assertThat(resultEntity.getLegalAidWithdrawalDate().toString()).isEqualTo(result.getLegalAidWithdrawalDate());
        assertThat(resultEntity.getDateOfHearing().toString()).isEqualTo(result.getDateOfHearing());
        assertThat(resultEntity.getCourtLocation()).isEqualTo(hearingResultedData.getSession().getCourtLocation());
        assertThat(resultEntity.getSessionValidateDate().toString()).isEqualTo(hearingResultedData.getSession().getSessionValidateDate());
        assertThat(resultEntity.getJurisdictionType()).isEqualTo(hearingResultedData.getJurisdictionType().name());
    }

    private void assertWqCoreProcessingCorrect(
            HearingResulted hearingResultedData, WqLinkRegisterEntity linkRegisterEntity, Integer resultCode, Integer expectedTxId, Boolean extendedProcessing) {

        WqCoreEntity coreEntity = wqCoreRepository.getById(expectedTxId);
        Optional<XLATResultEntity> resultEntity = xlatResultRepository.findById(resultCode);
        Integer wqType = resultEntity.map(XLATResultEntity::getWqType).orElse(null);
        assertThat(coreEntity).isNotNull();
        assertThat(coreEntity.getCaseId()).isEqualTo(linkRegisterEntity.getCaseId());
        assertThat(coreEntity.getCreatedUserId()).isEqualTo(MAGS_PROCESSING_SYSTEM_USER);
        assertThat(coreEntity.getWqStatus()).isEqualTo(WAITING.value());
        assertThat(coreEntity.getMaatUpdateStatus()).isEqualTo(2);
        assertThat(coreEntity.getExtendedProcessing()).isEqualTo(extendedProcessing ? 0 : 99);
        assertThat(coreEntity.getWqType()).isEqualTo(wqType);
        assertThat(coreEntity.getJurisdictionType()).isEqualTo(hearingResultedData.getJurisdictionType().name());
    }

    private void assertWqOffenceProcessingCorrect(Offence offence, WqLinkRegisterEntity linkRegisterEntity, Integer expectedTxId, Boolean newOffence) {

        WQOffenceEntity offenceEntity = wqOffenceRepository.getById(expectedTxId);

        assertThat(offenceEntity).isNotNull();
        assertThat(offenceEntity.getCaseId()).isEqualTo(linkRegisterEntity.getCaseId());
        assertThat(offenceEntity.getAsnSeq()).isEqualTo(String.format(LEADING_ZERO_3, Integer.parseInt(offence.getAsnSeq())));
        assertThat(offenceEntity.getOffenceClassification()).isEqualTo(offence.getOffenceClassification());
        assertThat(offenceEntity.getLegalAidStatus()).isEqualTo(offence.getLegalAidStatus());
        assertThat(offenceEntity.getLegalAidStatusDate().toString()).isEqualTo(offence.getLegalAidStatusDate());
        assertThat(offenceEntity.getLegalaidReason()).isEqualTo(offence.getLegalAidReason());
        assertThat(offenceEntity.getOffenceDate().toString()).isEqualTo(offence.getOffenceDate());
        assertThat(offenceEntity.getOffenceShortTitle()).isEqualTo(offence.getOffenceShortTitle());
        assertThat(offenceEntity.getModeOfTrial()).isEqualTo(offence.getModeOfTrial());
        assertThat(offenceEntity.getWqOffence()).isNull();
        assertThat(offenceEntity.getOffenceCode()).isEqualTo(offence.getOffenceCode());
        assertThat(offenceEntity.getApplicationFlag()).isEqualTo(offence.getApplicationFlag() != null ? offence.getApplicationFlag() : G_NO);
        assertThat(offenceEntity.getOffenceId()).isEqualTo(offence.getOffenceId());
        assertThat(offenceEntity.getIsCCNewOffence()).isEqualTo(newOffence ? YES : NO);
    }

    private void assertWqDefendantProcessingCorrect(Defendant defendant, WqLinkRegisterEntity linkRegisterEntity, Integer expectedTxId) {
        WQDefendant defendantEntity = wqDefendantRepository.getById(expectedTxId);
        assertThat(defendantEntity).isNotNull();
        assertThat(defendantEntity.getCaseId()).isEqualTo(linkRegisterEntity.getCaseId());
        assertThat(defendantEntity.getForename()).isEqualTo(defendant.getForename());
        assertThat(defendantEntity.getSurname()).isEqualTo(defendant.getSurname());
        assertThat(defendantEntity.getDateOfBirth().toString()).isEqualTo(defendant.getDateOfBirth());
        assertThat(defendantEntity.getAddressLine1()).isEqualTo(defendant.getAddressLine1());
        assertThat(defendantEntity.getAddressLine2()).isEqualTo(defendant.getAddressLine2());
        assertThat(defendantEntity.getAddressLine3()).isEqualTo(defendant.getAddressLine3());
        assertThat(defendantEntity.getAddressLine4()).isEqualTo(defendant.getAddressLine4());
        assertThat(defendantEntity.getAddressLine5()).isEqualTo(defendant.getAddressLine5());
        assertThat(defendantEntity.getPostCode()).isEqualTo(defendant.getPostcode());
        assertThat(defendantEntity.getNino()).isEqualTo(defendant.getNino());
        assertThat(defendantEntity.getTelephoneHome()).isEqualTo(defendant.getTelephoneHome());
        assertThat(defendantEntity.getTelephoneWork()).isEqualTo(defendant.getTelephoneWork());
        assertThat(defendantEntity.getTelephoneMobile()).isEqualTo(defendant.getTelephoneMobile());
        assertThat(defendantEntity.getEmail1()).isEqualTo(defendant.getEmail1());
        assertThat(defendantEntity.getEmail2()).isEqualTo(defendant.getEmail2());
    }

    private void assertWqSessionProcessingCorrect(Session session, WqLinkRegisterEntity linkRegisterEntity, Integer expectedTxId) {
        WQSessionEntity sessionEntity = wqSessionRepository.getById(expectedTxId);
        assertThat(sessionEntity).isNotNull();
        assertThat(sessionEntity.getCaseId()).isEqualTo(linkRegisterEntity.getCaseId());
        assertThat(sessionEntity.getDateOfHearing().toString()).isEqualTo(session.getDateOfHearing());
        assertThat(sessionEntity.getCourtLocation()).isEqualTo(session.getCourtLocation());
        assertThat(sessionEntity.getPostHearingCustody()).isEqualTo(session.getPostHearingCustody());
        assertThat(sessionEntity.getSessionvalidatedate().toString()).isEqualTo(session.getSessionValidateDate());
    }

    private void assertWqCaseProcessingCorrect(HearingResulted hearingResultedData, WqLinkRegisterEntity linkRegisterEntity, Integer expectedTxId) {

        WQCaseEntity wqCaseEntity =
                wqCaseRepository.findAll().stream()
                    .filter(item -> item.getCaseId() == linkRegisterEntity.getCaseId() && item.getTxId() == expectedTxId)
                    .collect(Collectors.toList()).get(0);

        assertThat(wqCaseEntity).isNotNull();
        assertThat(wqCaseEntity.getAsn()).isEqualTo(hearingResultedData.getAsn());
        assertThat(wqCaseEntity.getAsn()).isEqualTo(hearingResultedData.getAsn());
        assertThat(wqCaseEntity.getDocLanguage()).isEqualTo(hearingResultedData.getDocLanguage());
        assertThat(wqCaseEntity.getInactive()).isEqualTo(hearingResultedData.getInActive());
        assertThat(wqCaseEntity.getCjsAreaCode())
                .isEqualTo(hearingResultedData.getCjsAreaCode() != null ? String.format(
                        LEADING_ZERO_2, Integer.parseInt(hearingResultedData.getCjsAreaCode())) : null);
        assertThat(wqCaseEntity.getProceedingId()).isEqualTo(linkRegisterEntity.getProceedingId());
    }

    private List<Offence> getTestOffences() {
        String TEST_OFFENCE_CLASSIFICATION = "CC";
        return List.of(
                Offence.builder()
                        .offenceId("1")
                        .offenceCode("TOC1")
                        .legalAidStatus("AP")
                        .offenceClassification(TEST_OFFENCE_CLASSIFICATION)
                        .legalAidStatusDate(LocalDate.now().toString())
                        .offenceDate(LocalDate.now().toString())
                        .asnSeq("1")
                        .results(List.of(Result.builder()
                                .legalAidWithdrawalDate(LocalDate.now().toString())
                                .dateOfHearing(LocalDate.now().toString())
                                .nextHearingDate(LocalDate.now().toString())
                                .resultCode("123").build()))
                        .build(),
                Offence.builder()
                        .offenceId("2")
                        .offenceCode("TOC2")
                        .offenceClassification(TEST_OFFENCE_CLASSIFICATION)
                        .offenceDate(LocalDate.now().toString())
                        .legalAidStatus("AP")
                        .legalAidStatusDate(LocalDate.now().toString())
                        .asnSeq("2")
                        .applicationFlag(1)
                        .results(List.of(Result.builder()
                                .legalAidWithdrawalDate(LocalDate.now().toString())
                                .dateOfHearing(LocalDate.now().toString())
                                .nextHearingDate(LocalDate.now().toString())
                                .resultCode("456").build()))
                        .build());
    }

    private void runValidationErrorScenario(HearingResulted testPayload, String expectedErrorMessage) throws JsonProcessingException {
        runErrorScenario(ValidationException.class, testPayload, expectedErrorMessage);
    }

    private void runMaatErrorScenario(HearingResulted testPayload, String expectedErrorMessage) throws JsonProcessingException {
        runErrorScenario(MAATCourtDataException.class, testPayload, expectedErrorMessage);
    }

    private void assertWqHearingRepositoryUpdated(HearingResulted testPayload) {
        List<WQHearingEntity> matchingWqHearingEntities =
                wqHearingRepository.findByMaatIdAndHearingUUID(testPayload.getMaatId(), testPayload.getHearingId().toString());
        WqLinkRegisterEntity linkEntity = wqLinkRegisterRepository.findBymaatId(testPayload.getMaatId()).get(0);

        assertThat(matchingWqHearingEntities.size()).isEqualTo(1);
        WQHearingEntity createWqHearingEntity = matchingWqHearingEntities.get(0);

        assertThat(createWqHearingEntity.getMaatId()).isEqualTo(testPayload.getMaatId());
        assertThat(createWqHearingEntity.getHearingUUID()).isEqualTo(testPayload.getHearingId().toString());
        assertThat(createWqHearingEntity.getWqJurisdictionType()).isEqualTo(testPayload.getJurisdictionType().name());
        assertThat(createWqHearingEntity.getCaseUrn()).isEqualTo(testPayload.getCaseUrn());
        assertThat(createWqHearingEntity.getOuCourtLocation()).isEqualTo(testPayload.getSession().getCourtLocation());
        assertThat(createWqHearingEntity.getCaseId()).isEqualTo(linkEntity.getCaseId());

        String expectedResultCodes = Optional.ofNullable(testPayload.getDefendant())
                .map(Defendant::getOffences).orElse(new ArrayList<>()).stream()
                .flatMap(offence -> offence.getResults().stream())
                .map(Result::getResultCode)
                .distinct().collect(Collectors.joining(","));

        assertThat(createWqHearingEntity.getResultCodes()).isEqualTo(expectedResultCodes);
    }

    private void assertXlatOffencesCreated(List<Offence> expectedOffences, Boolean systemGenerated) {
        expectedOffences.forEach(offence -> {
            XLATOffenceEntity offenceEntity = xlatOffenceRepository.findById(offence.getOffenceCode()).orElse(null);
            assertThat(offenceEntity).isNotNull();
            assertThat(offenceEntity.getOffenceCode()).isEqualTo(offence.getOffenceCode());
            if (systemGenerated) {
                assertThat(offenceEntity.getParentCode()).isEqualTo(offence.getOffenceCode());
                assertThat(offenceEntity.getCodeMeaning()).isEqualTo(UNKNOWN_OFFENCE);
                assertThat(offenceEntity.getApplicationFlag()).isEqualTo(G_NO);
                assertThat(offenceEntity.getCreatedUser()).isEqualTo(AUTO_USER);
            } else {
                assertThat(offenceEntity.getCreatedUser()).isEqualTo(TEST_USER);
                assertThat(offenceEntity.getCreatedDate()).isEqualTo(TEST_CREATED_DATE);
            }
        });
    }

    private void assertXlatResultsCorrect(List<Offence> expectedOffences, Boolean resultsCreated) {
        expectedOffences.forEach(offence -> offence.getResults().forEach(result -> {
            Integer resultCode = Integer.parseInt(result.getResultCode());
            XLATResultEntity resultEntity =
                    xlatResultRepository.findById(resultCode).orElse(null);
            assertThat(resultEntity).isNotNull();
             if (resultsCreated) {
                 assertThat(resultEntity.getCjsResultCode()).isEqualTo(resultCode);
                 assertThat(resultEntity.getResultDescription()).isEqualTo(RESULT_CODE_DESCRIPTION);
                 assertThat(resultEntity.getEnglandAndWales()).isEqualTo(YES);
                 assertThat(resultEntity.getNotes()).isEqualTo(
                         String.format(
                                 "New Result code %s  has been received and automatically added to the Intervention queue. Please contact support.'"
                                 , resultCode));
                 assertThat(resultEntity.getWqType()).isEqualTo(WQType.USER_INTERVENTIONS_QUEUE.value());
                 assertThat(resultEntity.getCreatedUser()).isEqualTo(AUTO_USER);
             } else {
                 assertThat(resultEntity.getCreatedUser()).isEqualTo(TEST_USER);
             }

        }));
    }

    private <T extends Exception> void runErrorScenario(Class<T> exceptionClass, HearingResulted testPayload, String expectedErrorMessage) throws JsonProcessingException {
        String messageBlob = objectMapper.writeValueAsString(testPayload);
        T error = Assert.assertThrows(exceptionClass, () -> hearingResultedListener.receive(messageBlob));
        Assert.assertEquals(error.getMessage(), expectedErrorMessage);
        queueMessageLogTestHelper.assertQueueMessageLogged(
                messageBlob, 1, testPayload.getLaaTransactionId().toString(), testPayload.getMaatId());
    }

    private void setupTestData() {
        repOrderRepository.save(RepOrderEntity.builder().id(TEST_MAAT_ID).build());
    }

    private void createXlatResultData(List<Result> results) {
        results.forEach(result -> xlatResultRepository.save(XLATResultEntity.builder()
                .cjsResultCode(Integer.parseInt(result.getResultCode()))
                .createdDate(TEST_CREATED_DATE)
                .createdUser(TEST_USER)
                .wqType(2)
                .build()));
    }

    private void createXlatOffenceData(Offence offence) {
        xlatOffenceRepository.save(XLATOffenceEntity.builder()
                .offenceCode(offence.getOffenceCode())
                .createdUser(TEST_USER)
                .createdDate(TEST_CREATED_DATE)
                .applicationFlag(TEST_APPLICATION_FLAG)
                .build());
    }
}


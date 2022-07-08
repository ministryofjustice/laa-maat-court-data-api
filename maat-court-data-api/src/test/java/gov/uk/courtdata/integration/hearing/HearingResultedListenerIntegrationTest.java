package gov.uk.courtdata.integration.hearing;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.enums.FunctionType;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.hearing.service.HearingResultedListener;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.Defendant;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class HearingResultedListenerIntegrationTest {

    private final String LAA_TRANSACTION_ID = "b27b97e4-0514-42c4-8e09-fcc2c693e11f";
    private final Integer TEST_MAAT_ID = 1234;

    @Autowired
    private TestModelDataBuilder testModelDataBuilder;
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
    private TestEntityDataBuilder testEntityDataBuilder;
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
        setupTestData();
        queueMessageLogTestHelper = new QueueMessageLogTestHelper(queueMessageLogRepository);
    }

    @Test
    public void givenAHearingMessageWithNoMaatId_whenMessageIsReceived_thenErrorIsReturned() {
        String payloadMissingMaatId = String.format("{\"laaTransactionId\":\"%s\"}", LAA_TRANSACTION_ID);
        ValidationException error = Assert.assertThrows(
                ValidationException.class, () -> hearingResultedListener.receive(payloadMissingMaatId));
        Assert.assertEquals(error.getMessage(), "MAAT ID is required.");
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
        runValidationErrorScenario(testData, String.format("MAAT Id : %s not linked.", TEST_MAAT_ID));
    }

    @Test
    public void givenAHearingMessageWhereTheMaatIdHasMultipleLinks_whenMessageIsReceived_thenErrorIsReturned() throws JsonProcessingException {
        Integer maatIdWithMultipleLinks = 111;

        wqLinkRegisterRepository.saveAll(List.of(
                WqLinkRegisterEntity.builder().createdTxId(1).maatId(maatIdWithMultipleLinks).caseId(1).build(),
                WqLinkRegisterEntity.builder().createdTxId(2).maatId(maatIdWithMultipleLinks).caseId(2).build()
        ));

        HearingResulted testData = HearingResulted.builder()
                .maatId(maatIdWithMultipleLinks)
                .laaTransactionId(UUID.fromString(LAA_TRANSACTION_ID))
                .jurisdictionType(JurisdictionType.CROWN).build();
        runValidationErrorScenario(testData, String.format("MAAT/REP ID: %d is invalid.", maatIdWithMultipleLinks));
    }

    @Test
    public void givenAHearingMessageWhereTheOffenceCodeIsNull_whenMessageIsReceived_thenErrorIsReturned() throws JsonProcessingException {
        wqLinkRegisterRepository.save(WqLinkRegisterEntity.builder().createdTxId(1).maatId(TEST_MAAT_ID).caseId(1).build());

        HearingResulted testData = HearingResulted.builder()
                .maatId(TEST_MAAT_ID)
                .laaTransactionId(UUID.fromString(LAA_TRANSACTION_ID))
                .hearingId(UUID.randomUUID())
                .functionType(FunctionType.OFFENCE)
                .jurisdictionType(JurisdictionType.CROWN)
                .session(Session.builder().courtLocation("test-court").build())
                .defendant(Defendant.builder().offences(getTestOffences()).build())
                .build();
        runMaatErrorScenario(testData, "A Null Offence Code is passed in");
    }

    @Test
    public void givenAValidHearingMessage_whenMessageIsReceived_thenTheCorrectDataIsPersisted() {

    }


    private List<Offence> getTestOffences() {
        return List.of(
                Offence.builder().results(List.of(Result.builder().resultCode("result1").build())).build(),
                Offence.builder().results(List.of(Result.builder().resultCode("result2").build())).build());
    }

    private void runValidationErrorScenario(HearingResulted testPayload, String expectedErrorMessage) throws JsonProcessingException {
        runErrorScenario(ValidationException.class, testPayload, expectedErrorMessage);
    }

    private void runMaatErrorScenario(HearingResulted testPayload, String expectedErrorMessage) throws JsonProcessingException {
        runErrorScenario(MAATCourtDataException.class, testPayload, expectedErrorMessage);
        assertWqHearingRepositoryUpdated(testPayload);
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
}


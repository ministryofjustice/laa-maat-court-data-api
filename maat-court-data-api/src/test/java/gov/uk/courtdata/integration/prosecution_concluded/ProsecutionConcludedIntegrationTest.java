package gov.uk.courtdata.integration.prosecution_concluded;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.enums.CaseConclusionStatus;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.integration.prosecution_concluded.procedures.UpdateOutcomesEntity;
import gov.uk.courtdata.integration.prosecution_concluded.procedures.UpdateOutcomesRepository;
import gov.uk.courtdata.model.Metadata;
import gov.uk.courtdata.prosecutionconcluded.impl.ProcessSentencingImpl;
import gov.uk.courtdata.prosecutionconcluded.model.*;
import gov.uk.courtdata.prosecutionconcluded.service.HearingsService;
import gov.uk.courtdata.prosecutionconcluded.service.ProsecutionConcludedListener;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.QueueMessageLogTestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.MessageHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        MAATCourtDataApplication.class,
        MockServicesConfig.class,
        UpdateOutcomesRepository.class,
        })
public class ProsecutionConcludedIntegrationTest {

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected Gson gson;
    @Autowired
    private ProsecutionConcludedListener prosecutionConcludedListener;
    @Autowired
    private WQHearingRepository wqHearingRepository;
    @Autowired
    private OffenceRepository offenceRepository;
    @Autowired
    private QueueMessageLogRepository queueMessageLogRepository;
    @Autowired
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Autowired
    private XLATResultRepository xlatResultRepository;
    @Autowired
    private ResultRepository resultRepository;
    @Autowired
    private RepOrderRepository repOrderRepository;
    @Autowired
    private CrownCourtCodeRepository crownCourtCodeRepository;
    @Autowired
    private ReservationsRepository reservationsRepository;
    @Autowired
    private ProsecutionConcludedRepository prosecutionConcludedRepository;
    @Autowired
    private UpdateOutcomesRepository updateOutcomesRepository;
    @Autowired
    private FinancialAssessmentRepository financialAssessmentRepository;
    @MockBean
    private CrownCourtProcessingRepository crownCourtProcessingRepository;
    @Autowired
    private PassportAssessmentRepository passportAssessmentRepository;
    @Mock
    private ProcessSentencingImpl processSentencingHelper;
    @Mock
    private HearingsService hearingsService;
    private QueueMessageLogTestHelper queueMessageLogTestHelper;

    private final Integer TEST_MAAT_ID = 6039349;
    private final String TEST_DB_USER = "SA";
    private final UUID TEST_HEARING_ID = UUID.fromString("61600a90-89e2-4717-aa9b-a01fc66130c1");
    private final String LAA_TRANSACTION_ID = "61600a90-89e2-4717-aa9b-a01fc66130c1";

    private WQHearingEntity existingWqHearingEntity;
    private RepOrderEntity existingRepOrder;
    private CrownCourtCode existingCrownCourtCode;

    @BeforeEach
    public void setUp() throws Exception {
        financialAssessmentRepository.deleteAll();
        passportAssessmentRepository.deleteAll();
        wqHearingRepository.deleteAll();
        offenceRepository.deleteAll();
        queueMessageLogRepository.deleteAll();
        wqLinkRegisterRepository.deleteAll();
        xlatResultRepository.deleteAll();
        resultRepository.deleteAll();
        repOrderRepository.deleteAll();
        crownCourtCodeRepository.deleteAll();
        updateOutcomesRepository.deleteAll();
        reservationsRepository.deleteAll();
        prosecutionConcludedRepository.deleteAll();
        loadData();
        queueMessageLogTestHelper = new QueueMessageLogTestHelper(queueMessageLogRepository);
        doNothing().when(crownCourtProcessingRepository).invokeUpdateAppealSentenceOrderDate(any(Integer.class), anyString(), any(LocalDate.class), any(LocalDate.class));
        doNothing().when(crownCourtProcessingRepository).invokeUpdateSentenceOrderDate(any(Integer.class), anyString(), any(LocalDate.class));
    }

    private void loadData() {
        existingWqHearingEntity = wqHearingRepository
                .save(WQHearingEntity
                        .builder()
                        .txId(23232)
                        .hearingUUID(TEST_HEARING_ID.toString())
                        .wqJurisdictionType(JurisdictionType.CROWN.name())
                        .maatId(TEST_MAAT_ID)
                        .caseUrn("3CDSRE33")
                        .ouCourtLocation("3434")
                        .build()
                );

        wqLinkRegisterRepository
                .save(WqLinkRegisterEntity
                        .builder()
                        .createdTxId(343431)
                        .maatId(TEST_MAAT_ID)
                        .caseId(12129)
                        .caseUrn("CASEURN233")
                        .build()
                );

        offenceRepository
                .save(OffenceEntity
                        .builder()
                        .txId(3454238)
                        .caseId(12129)
                        .offenceId("ed0e9d59-cc1c-4869-8fcd-464caf770744")
                        .asnSeq("001")
                        .build()
                );

        xlatResultRepository.saveAll(
                List.of(
                    XLATResultEntity.builder()
                            .cjsResultCode(4455)
                            .wqType(1)
                            .subTypeCode(1)
                            .ccBenchWarrant("Y")
                            .ccImprisonment("Y")
                            .build(),
                    XLATResultEntity
                            .builder()
                            .cjsResultCode(4458)
                            .wqType(1)
                            .subTypeCode(2)
                            .ccBenchWarrant("Y")
                            .build()
                ));

        resultRepository.save(
                ResultEntity
                        .builder()
                        .asnSeq("1")
                        .asn("1")
                        .txId(4564523)
                        .caseId(12129)
                        .resultCode("4458")
                        .build()
        );

        existingRepOrder = repOrderRepository.save(
                RepOrderEntity
                        .builder()
                        .id(TEST_MAAT_ID)
                        .caseId("12129")
                        .appealTypeCode("EITHER WAY")
                        .catyCaseType("EITHER WAY")
                        .build()
        );

        existingCrownCourtCode = crownCourtCodeRepository.save(
                CrownCourtCode
                        .builder()
                        .code("2232")
                        .ouCode("3434")
                        .build()
        );
    }

    @Test
    public void givenSqsPayload_whenDataIsValid_thenProcessAsExpected() throws JsonProcessingException {
        ProsecutionConcluded message = getTestProsecutionConcludedObject();
        message.setHearingIdWhereChangeOccurred(UUID.fromString(existingWqHearingEntity.getHearingUUID()));
        String sqsPayload = pullMessageFromSQS(message);

        runValidDataScenario(message, sqsPayload);
        verify(crownCourtProcessingRepository)
                .invokeUpdateSentenceOrderDate(
                        message.getMaatId(), TEST_DB_USER, LocalDate.parse(message.getOffenceSummary().get(0).getProceedingsConcludedChangedDate()));
    }

    @Test
    @Disabled("This test will fail until LASB-1238 and LASB-1251 are fixed.")
    public void givenSqsPayload_whenDataIsValidWithAppealCaseType_thenProcessAsExpected() throws JsonProcessingException {
        ProsecutionConcluded message = getTestProsecutionConcludedObject();
        message.getOffenceSummary().get(0).getPlea().setValue("NOT_GUILTY");
        message.getOffenceSummary().get(0).getVerdict().setVerdictType(
                VerdictType.builder()
                .description("SUCCESSFUL")
                .category("SUCCESSFUL")
                .categoryType("SUCCESSFUL")
                .sequence(4126)
                .verdictTypeId(null)
                .build());
        message.setHearingIdWhereChangeOccurred(UUID.fromString(existingWqHearingEntity.getHearingUUID()));
        String sqsPayload = pullMessageFromSQS(message);

        existingRepOrder.setAppealTypeCode("APPEAL CC");
        existingRepOrder.setCatyCaseType("APPEAL CC");
        repOrderRepository.save(existingRepOrder);

        runValidDataScenario(message, sqsPayload);
        verify(crownCourtProcessingRepository)
                .invokeUpdateSentenceOrderDate(
                        message.getMaatId(), TEST_DB_USER, LocalDate.parse(message.getOffenceSummary().get(0).getProceedingsConcludedChangedDate()));
    }

    @Test
    public void givenSqsPayload_whenCaseTypeIsNotValidForTrial_thenReturnMessage() throws JsonProcessingException {
        ProsecutionConcluded message = getTestProsecutionConcludedObject();
        message.setHearingIdWhereChangeOccurred(UUID.fromString(existingWqHearingEntity.getHearingUUID()));
        String sqsPayload = pullMessageFromSQS(message);

        existingRepOrder.setAppealTypeCode("APPEAL CC");
        existingRepOrder.setCatyCaseType("APPEAL CC");
        repOrderRepository.save(existingRepOrder);
        prosecutionConcludedListener.receive(sqsPayload, new MessageHeaders(new HashMap<>()));
//        verify it
        Mockito.verify(processSentencingHelper, Mockito.times(0)).processSentencingDate(
                any(String.class), any(Integer.class), anyString());
        queueMessageLogTestHelper.assertQueueMessageLogged(sqsPayload, 1, LAA_TRANSACTION_ID, message.getMaatId());
    }

    @Test
    public void givenSqsPayload_whenDataIsValidAndProsecutionConcludedEntityExists_thenProcessAndUpdateAsExpected() throws JsonProcessingException {
        ProsecutionConcluded message = getTestProsecutionConcludedObject();
        message.setHearingIdWhereChangeOccurred(UUID.fromString(existingWqHearingEntity.getHearingUUID()));
        String sqsPayload = pullMessageFromSQS(message);

        prosecutionConcludedRepository.save(
                ProsecutionConcludedEntity.builder()
                        .maatId(message.getMaatId())
                        .hearingId(message.getHearingIdWhereChangeOccurred().toString())
                        .status(CaseConclusionStatus.PENDING.name())
                        .build());

        runValidDataScenario(message, sqsPayload);
        verify(crownCourtProcessingRepository)
                .invokeUpdateSentenceOrderDate(
                        message.getMaatId(), TEST_DB_USER, LocalDate.parse(message.getOffenceSummary().get(0).getProceedingsConcludedChangedDate()));
    }

    @Test
    public void givenSqsPayload_whenDataIsValidAndCaseIsNotConcluded_thenDoNothing() throws JsonProcessingException {
        ProsecutionConcluded message = getTestProsecutionConcludedObject();
        message.setConcluded(false);
        message.setHearingIdWhereChangeOccurred(UUID.fromString(existingWqHearingEntity.getHearingUUID()));
        String sqsPayload = pullMessageFromSQS(message);
        prosecutionConcludedListener.receive(sqsPayload, new MessageHeaders(new HashMap<>()));

        assertProsecutionNoChangesMade(message, sqsPayload);
    }

    @Test
    public void givenSqsPayload_whenDataIsValidAndJurisdictionIsNotCrown_thenDoNothing() throws JsonProcessingException {
        ProsecutionConcluded message = getTestProsecutionConcludedObject();
        existingWqHearingEntity.setWqJurisdictionType("MAGISTRATES");
        wqHearingRepository.save(existingWqHearingEntity);
        message.setHearingIdWhereChangeOccurred(UUID.fromString(existingWqHearingEntity.getHearingUUID()));
        String sqsPayload = pullMessageFromSQS(message);
        prosecutionConcludedListener.receive(sqsPayload, new MessageHeaders(new HashMap<>()));

        assertProsecutionNoChangesMade(message, sqsPayload);
    }

    @Test
    public void givenSqsPayload_whenMaatIdIsLocked_thenScheduleForProcessing() throws JsonProcessingException {
        ProsecutionConcluded message = getTestProsecutionConcludedObject();
        message.setHearingIdWhereChangeOccurred(UUID.fromString(existingWqHearingEntity.getHearingUUID()));
        String sqsPayload = pullMessageFromSQS(message);

        reservationsRepository.save(ReservationsEntity.builder().recordId(message.getMaatId()).build());

        prosecutionConcludedListener.receive(sqsPayload, new MessageHeaders(new HashMap<>()));

        assertDataStoredForLaterProcessing(message, sqsPayload);
    }

    @Test
    @Disabled("This test should be disabled until LASB-1288 is completed.")
    public void givenSqsPayload_whenHearingEntityDoesNotExist_thenScheduleForProcessing() throws JsonProcessingException {
        ProsecutionConcluded message = getTestProsecutionConcludedObject();
        message.setHearingIdWhereChangeOccurred(UUID.randomUUID());
        String sqsPayload = pullMessageFromSQS(message);
        prosecutionConcludedListener.receive(sqsPayload, new MessageHeaders(new HashMap<>()));

        assertDataStoredForLaterProcessing(message, sqsPayload);
    }

    @Test
    public void givenSqsPayload_whenHearingOffenceSummaryIsNull_thenReturnMessage() {
        String sqsPayload = "{" +
                "    \"maatId\": " + TEST_MAAT_ID + ",\n" +
                "    \"hearingIdWhereChangeOccurred\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "    \"metadata\": {\n" +
                "        \"laaTransactionId\": " + LAA_TRANSACTION_ID + "\n" +
                "    }\n" +
                "}";
        prosecutionConcludedListener.receive(sqsPayload, new MessageHeaders(new HashMap<>()));
        Mockito.verify(hearingsService, Mockito.times(0)).retrieveHearingForCaseConclusion(
                any(ProsecutionConcluded.class));

        queueMessageLogTestHelper.assertQueueMessageLogged(sqsPayload, 1,LAA_TRANSACTION_ID, TEST_MAAT_ID);
    }

    @Test

    public void givenSqsPayload_whenHearingOffenceSummaryIsEmpty_thenReturnMessage() {
        String sqsPayload = "{" +
                "    \"maatId\": " + TEST_MAAT_ID + ",\n" +
                "    \"hearingIdWhereChangeOccurred\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "    \"offenceSummary\": [],\n" +
                "    \"metadata\": {\n" +
                "        \"laaTransactionId\": " + LAA_TRANSACTION_ID + "\n" +
                "    }\n" +
                "}";
        prosecutionConcludedListener.receive(sqsPayload, new MessageHeaders(new HashMap<>()));
        Mockito.verify(hearingsService, Mockito.times(0)).retrieveHearingForCaseConclusion(
                any(ProsecutionConcluded.class));

        queueMessageLogTestHelper.assertQueueMessageLogged(sqsPayload, 1,LAA_TRANSACTION_ID, TEST_MAAT_ID);
    }

    @Test

    public void givenSqsPayload_whenMaatIdIsNull_thenReturnMessage() {
        String sqsPayload = "{" +
                "    \"hearingIdWhereChangeOccurred\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "    \"metadata\": {\n" +
                "        \"laaTransactionId\": " + LAA_TRANSACTION_ID + "\n" +
                "    }\n" +
                "}";

        prosecutionConcludedListener.receive(sqsPayload, new MessageHeaders(new HashMap<>()));
        Mockito.verify(hearingsService, Mockito.times(0)).retrieveHearingForCaseConclusion(
                any(ProsecutionConcluded.class));

        queueMessageLogTestHelper.assertQueueMessageLogged(sqsPayload, 1,LAA_TRANSACTION_ID, -1);
    }

    private void assertDataStoredForLaterProcessing(ProsecutionConcluded message, String sqsPayload) {
        queueMessageLogTestHelper.assertQueueMessageLogged(
                sqsPayload, 1,message.getMetadata().getLaaTransactionId(), message.getMaatId());

        assertThat(updateOutcomesRepository.findAll().size()).isEqualTo(0);

        List<ProsecutionConcludedEntity> createdProsecutionConcludedEntities = prosecutionConcludedRepository.findAll();
        assertThat(createdProsecutionConcludedEntities.size()).isEqualTo(1);

        ProsecutionConcludedEntity createdProsecutionConcludedEntity = prosecutionConcludedRepository.findAll().get(0);
        assertThat(createdProsecutionConcludedEntity.getStatus()).isEqualTo(CaseConclusionStatus.PENDING.name());
        assertThat(createdProsecutionConcludedEntity.getMaatId()).isEqualTo(message.getMaatId());
        ProsecutionConcluded savedCaseData =
                gson.fromJson(new String(createdProsecutionConcludedEntity.getCaseData(), StandardCharsets.UTF_8), ProsecutionConcluded.class);
        assertThat(savedCaseData).isEqualTo(message);
        assertThat(createdProsecutionConcludedEntity.getHearingId()).isEqualTo(message.getHearingIdWhereChangeOccurred().toString());
    }

    private void assertProsecutionNoChangesMade(ProsecutionConcluded message, String sqsPayload) {
        queueMessageLogTestHelper.assertQueueMessageLogged(
                sqsPayload, 1,message.getMetadata().getLaaTransactionId(), message.getMaatId());

        assertThat(updateOutcomesRepository.findAll().size()).isEqualTo(0);
        assertThat(prosecutionConcludedRepository.findAll().size()).isEqualTo(0);
    }

    private void assertProsecutionConcludedEntitiesUpdated(ProsecutionConcluded message) {
        List<ProsecutionConcludedEntity> concludedEntities = prosecutionConcludedRepository.getByMaatId(message.getMaatId());
        concludedEntities.forEach(
                concludedEntity -> assertThat(concludedEntity.getStatus()).isEqualTo(CaseConclusionStatus.PROCESSED.name()));
    }

    private ProsecutionConcluded getTestProsecutionConcludedObject() {
        return ProsecutionConcluded.builder()
                .prosecutionCaseId(UUID.fromString("998984a0-ae53-466c-9c13-e0c84c1fd581"))
                .defendantId(UUID.fromString("aa07e234-7e80-4be1-a076-5ab8a8f49df5"))
                .isConcluded(true)
                .hearingIdWhereChangeOccurred(UUID.fromString("998984a0-ae53-466c-9c13-e0c84c1fd581"))
                .offenceSummary(List.of(
                        OffenceSummary.builder()
                                .offenceId(UUID.fromString("ed0e9d59-cc1c-4869-8fcd-464caf770744"))
                                .offenceCode("PT00011")
                                .proceedingsConcluded(true)
                                .proceedingsConcludedChangedDate("2022-02-01")
                                .plea(Plea.builder()
                                        .originatingHearingId(UUID.fromString("61600a90-89e2-4717-aa9b-a01fc66130c1"))
                                        .value("GUILTY")
                                        .pleaDate("2022-02-01").build())
                                .verdict(Verdict.builder()
                                        .verdictDate("2022-02-01")
                                        .originatingHearingId(UUID.fromString("61600a90-89e2-4717-aa9b-a01fc66130c1"))
                                        .verdictType(
                                                VerdictType.builder()
                                                .description("GUILTY")
                                                .category("GUILTY")
                                                .categoryType("GUILTY")
                                                .sequence(4126)
                                                .verdictTypeId(null)
                                                .build())
                                        .build())
                                .build()
                ))
                .maatId(6039349)
                .metadata(
                        Metadata.builder().laaTransactionId("61600a90-89e2-4717-aa9b-a01fc66130c1").build())
                .build();
    }

    private String pullMessageFromSQS(ProsecutionConcluded prosecutionConcludedObject) throws JsonProcessingException {
        String messageString = objectMapper.writeValueAsString(prosecutionConcludedObject);
        return messageString.replace("concluded", "isConcluded");
    }

    private void assertUpdateOutcomesProcedureCalledCorrectly(UpdateOutcomesEntity expectedProcedureInputs) {
        assertThat(updateOutcomesRepository.findAll().size()).isEqualTo(1);
        UpdateOutcomesEntity procedureInputs = updateOutcomesRepository.findAll().get(0);
        assertThat(procedureInputs.getRepId()).isEqualTo(expectedProcedureInputs.getRepId());
        assertThat(procedureInputs.getAppealType()).isEqualTo(expectedProcedureInputs.getAppealType());
        assertThat(procedureInputs.getCcOutcome()).isEqualTo(expectedProcedureInputs.getCcOutcome());
        assertThat(procedureInputs.getBenchWarrantIssued()).isEqualTo(expectedProcedureInputs.getBenchWarrantIssued());
        assertThat(procedureInputs.getImprisoned()).isEqualTo(expectedProcedureInputs.getImprisoned());
        assertThat(procedureInputs.getCaseNumber()).isEqualTo(expectedProcedureInputs.getCaseNumber());
        assertThat(procedureInputs.getCrownCourtCode()).isEqualTo(expectedProcedureInputs.getCrownCourtCode());
    }

    private void runValidDataScenario(ProsecutionConcluded message, String sqsPayload) {
        prosecutionConcludedListener.receive(sqsPayload, new MessageHeaders(new HashMap<>()));
        queueMessageLogTestHelper.assertQueueMessageLogged(
                sqsPayload, 1,message.getMetadata().getLaaTransactionId(), message.getMaatId());

        assertUpdateOutcomesProcedureCalledCorrectly(
                UpdateOutcomesEntity.builder()
                        .repId(message.getMaatId())
                        .ccOutcome("CONVICTED")
                        .benchWarrantIssued("null")
                        .appealType("EITHER WAY")
                        .imprisoned("N")
                        .caseNumber(existingWqHearingEntity.getCaseUrn())
                        .crownCourtCode(existingCrownCourtCode.getCode()).build());
        assertProsecutionConcludedEntitiesUpdated(message);
    }
}

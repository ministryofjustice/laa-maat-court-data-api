package gov.uk.courtdata.integration.assessment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.assessment.service.OutstandingAssessmentService;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.entity.ChildWeightingsEntity;
import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.NewWorkReasonEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.UserEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.model.NewWorkReason;
import gov.uk.courtdata.model.assessment.ChildWeightings;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.model.assessment.FinancialAssessmentIncomeEvidence;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;

import java.util.List;
import java.util.Objects;

import org.assertj.core.api.SoftAssertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class FinancialAssessmentControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String BASE_URL = "/api/internal/v1/assessment/financial-assessments";
    private static final String ASSESSMENT_URL = BASE_URL + "/{financialAssessmentId}";
    private static final String CHECK_OUTSTANDING_URL = BASE_URL + "/check-outstanding/{repId}";
    private static final String MEANS_ASSESSOR_DETAILS_URL =
            BASE_URL + "/{financialAssessmentId}/means-assessor-details";
    private static final String CREATE_ASSESSMENT_HISTORY_URL =
            BASE_URL + "/history/{financialAssessmentId}/fullAvailable/{fullAvailable}";

    private Integer repIdWithOutstandingAssessments = 1111;
    private Integer repIdWithNoOutstandingAssessments = 2222;
    private Integer repIdWithOutstandingPassportAssessments = 3333;

    @Autowired
    private FinancialAssessmentMapper assessmentMapper;

    private List<FinancialAssessmentEntity> existingAssessmentEntities;
    private RepOrderEntity existingRepOrder;

    @BeforeEach
    void setUp() {
        setupTestData();
    }

    private void setupTestData() {
        Integer repIdDefault = 4444;
        existingRepOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(repIdDefault));

        RepOrderEntity outstandingRepOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder());
        RepOrderEntity noOutstandingRepOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder());
        RepOrderEntity outstandingPassportRepOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder());
        repIdWithOutstandingAssessments = outstandingRepOrder.getId();
        repIdWithNoOutstandingAssessments = noOutstandingRepOrder.getId();
        repIdWithOutstandingPassportAssessments = outstandingPassportRepOrder.getId();

        UserEntity userEntity = TestEntityDataBuilder.getUserEntity(TestEntityDataBuilder.TEST_USER);
        repos.user.save(userEntity);

        NewWorkReasonEntity newWorkReasonEntity =
                repos.mockNewWorkReason.save(TestEntityDataBuilder.getFmaNewWorkReasonEntity());

        List<FinancialAssessmentEntity> assessmentsToCreate = List.of(
                TestEntityDataBuilder.getCustomFinancialAssessmentEntity(
                        repIdWithOutstandingAssessments, "IN PROGRESS", newWorkReasonEntity),
                TestEntityDataBuilder.getCustomFinancialAssessmentEntity(
                        repIdWithNoOutstandingAssessments, "COMPLETE", newWorkReasonEntity),
                TestEntityDataBuilder.getCustomFinancialAssessmentEntity(
                        repIdWithOutstandingPassportAssessments, "COMPLETE", newWorkReasonEntity),
                TestEntityDataBuilder.getFinancialAssessmentEntityWithRelationships(
                        existingRepOrder.getId(), newWorkReasonEntity));

        existingAssessmentEntities = repos.financialAssessment.saveAll(assessmentsToCreate);

        repos.passportAssessment.save(PassportAssessmentEntity.builder()
                .repOrder(outstandingPassportRepOrder)
                .pastStatus("IN PROGRESS")
                .build());
    }

    @Test
    void givenAZeroAssessmentId_whenGetAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        assertThat(runBadRequestErrorScenario("Financial Assessment id is required", get(ASSESSMENT_URL, 0)))
                .isTrue();
    }

    @Test
    void givenAnInvalidAssessmentId_whenGetAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        Integer invalidAssessmentId = 999;
        assertThat(runBadRequestErrorScenario(
                        String.format("%d is invalid", invalidAssessmentId), get(ASSESSMENT_URL, invalidAssessmentId)))
                .isTrue();
    }

    @Test
    void givenAValidAssessmentId_whenGetAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        var testAssessment = existingAssessmentEntities.getFirst();
        assertThat(runSuccessScenario(
                        assessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(testAssessment),
                        get(ASSESSMENT_URL, testAssessment.getId())))
                .isTrue();
    }

    @Test
    void givenAValidAssessmentId_whenGetAssessmentIsInvoked_thenCorrectRelationshipsResponse() throws Exception {
        var testAssessment = existingAssessmentEntities.get(3);
        assertThat(runSuccessScenario(
                        assessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(testAssessment),
                        get(ASSESSMENT_URL, testAssessment.getId())))
                .isTrue();
    }

    @Test
    void givenAZeroAssessmentId_whenDeleteAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        assertThat(runBadRequestErrorScenario("Financial Assessment id is required", delete(ASSESSMENT_URL, 0)))
                .isTrue();
    }

    @Test
    void givenAnInvalidAssessmentId_whenDeleteAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        Integer invalidAssessmentId = 999;
        assertThat(runBadRequestErrorScenario(
                        String.format("%d is invalid", invalidAssessmentId),
                        delete(ASSESSMENT_URL, invalidAssessmentId)))
                .isTrue();
    }

    @Test
    void givenAValidAssessmentId_whenDeleteAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        assertThat(runSuccessScenario(delete(
                                ASSESSMENT_URL,
                                existingAssessmentEntities.getFirst().getId()))
                        .getResponse()
                        .getStatus())
                .isEqualTo(200);
    }

    @Test
    void givenNoOutstanding_whenCheckForOutstandingAssessmentsIsInvoked_thenNoMessagesReturned() throws Exception {
        OutstandingAssessmentResultDTO expectedResponse =
                OutstandingAssessmentResultDTO.builder().build();
        assertThat(runSuccessScenario(expectedResponse, get(CHECK_OUTSTANDING_URL, repIdWithNoOutstandingAssessments)))
                .isTrue();
    }

    @Test
    void givenOutstandingMeans_whenCheckForOutstandingAssessmentsIsInvoked_thenShouldReturnMessage() throws Exception {
        OutstandingAssessmentResultDTO expectedResponse = OutstandingAssessmentResultDTO.builder()
                .outstandingAssessments(true)
                .message(OutstandingAssessmentService.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND)
                .build();
        assertThat(runSuccessScenario(expectedResponse, get(CHECK_OUTSTANDING_URL, repIdWithOutstandingAssessments)))
                .isTrue();
    }

    @Test
    void givenOutstandingPassport_whenCheckForOutstandingAssessmentsIsInvoked_thenShouldReturnMessage()
            throws Exception {
        OutstandingAssessmentResultDTO expectedResponse = OutstandingAssessmentResultDTO.builder()
                .outstandingAssessments(true)
                .message(OutstandingAssessmentService.MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND)
                .build();
        assertThat(runSuccessScenario(
                        expectedResponse, get(CHECK_OUTSTANDING_URL, repIdWithOutstandingPassportAssessments)))
                .isTrue();
    }

    @Test
    void givenAnAssessmentWithNoRepId_whenCreateAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        assertThat(runCreateAssessmentErrorScenario(
                        "Rep Order ID is required",
                        CreateFinancialAssessment.builder().build()))
                .isTrue();
    }

    @Test
    void givenAnAssessmentWithNoCriteriaId_whenCreateAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        assertThat(runCreateAssessmentErrorScenario(
                        "Assessment Criteria ID is required",
                        CreateFinancialAssessment.builder().repId(1).build()))
                .isTrue();
    }

    @Test
    void givenAnAssessmentWithNoCmuId_whenCreateAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        assertThat(runCreateAssessmentErrorScenario(
                        "Case management unit ID is required",
                        CreateFinancialAssessment.builder()
                                .repId(1)
                                .initialAscrId(1)
                                .build()))
                .isTrue();
    }

    @Test
    void givenAnAssessmentWithNoNewWorkReason_whenCreateAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        assertThat(runCreateAssessmentErrorScenario(
                        "New work reason code is required",
                        CreateFinancialAssessment.builder()
                                .repId(1)
                                .initialAscrId(1)
                                .cmuId(1)
                                .build()))
                .isTrue();
    }

    @Test
    void givenAnAssessmentWithNoUser_whenCreateAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        assertThat(runCreateAssessmentErrorScenario(
                        "Username is required",
                        CreateFinancialAssessment.builder()
                                .repId(1)
                                .initialAscrId(1)
                                .cmuId(1)
                                .nworCode("FMA")
                                .build()))
                .isTrue();
    }

    @Test
    void givenAValidAssessmentBody_whenCreateAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        CreateFinancialAssessment body = TestModelDataBuilder.getCreateFinancialAssessmentWithRelationships();
        body.setRepId(repIdWithNoOutstandingAssessments);

        FinancialAssessmentDTO expectedResponse = TestModelDataBuilder.getFinancialAssessmentDTO();
        expectedResponse.setRepId(repIdWithNoOutstandingAssessments);
        expectedResponse.setChildWeightings(List.of(TestModelDataBuilder.getChildWeightings()));
        expectedResponse.setAssessmentDetails(List.of(TestModelDataBuilder.getFinancialAssessmentDetails()));
        expectedResponse.setAssessmentType("INIT");
        expectedResponse.setInitialAssessmentDate(body.getInitialAssessmentDate());

        runSuccessScenario(
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));

        List<FinancialAssessmentEntity> matchingAssessments = repos.financialAssessment.findAll().stream()
                .filter(assessment -> assessment.getRepOrder().getId().equals(repIdWithNoOutstandingAssessments))
                .toList();

        FinancialAssessmentEntity createdAssessment = matchingAssessments.stream()
                .filter(assessment -> assessment.getReplaced().equals("N"))
                .toList()
                .getFirst();

        expectedResponse.setId(createdAssessment.getId());
        expectedResponse.setDateCreated(createdAssessment.getDateCreated());
        expectedResponse.setUpdated(createdAssessment.getUpdated());
        expectedResponse
                .getAssessmentDetails()
                .getFirst()
                .setId(createdAssessment.getAssessmentDetails().getFirst().getId());
        expectedResponse
                .getChildWeightings()
                .getFirst()
                .setId(createdAssessment.getChildWeightings().getFirst().getId());
        expectedResponse
                .getAssessmentDetails()
                .getFirst()
                .setDateModified(
                        createdAssessment.getAssessmentDetails().getFirst().getDateModified());
        expectedResponse.setInitialAssessmentDate(createdAssessment.getInitialAssessmentDate());

        SoftAssertions.assertSoftly(softly -> {
            assertThat(matchingAssessments).hasSize(2);
            assertThat((int) matchingAssessments.stream()
                            .filter(assessment -> assessment.getReplaced().equals("Y"))
                            .count())
                    .isEqualTo(1);
            assertChildWeightingsEqual(expectedResponse.getChildWeightings(), createdAssessment.getChildWeightings());
            assertFinancialAssessmentDetailsEqual(
                    expectedResponse.getAssessmentDetails(), createdAssessment.getAssessmentDetails());
            assertFinancialAssessmentEqual(expectedResponse, createdAssessment);
        });
    }

    @Test
    void givenAnAssessmentWithNoRepId_whenUpdateAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        assertThat(runUpdateAssessmentErrorScenario(
                        "Rep Order ID is required",
                        UpdateFinancialAssessment.builder().build()))
                .isTrue();
    }

    @Test
    void givenAnAssessmentWithNoCriteriaId_whenUpdateAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        assertThat(runUpdateAssessmentErrorScenario(
                        "Assessment Criteria ID is required",
                        UpdateFinancialAssessment.builder().repId(1).build()))
                .isTrue();
    }

    @Test
    void givenAnAssessmentWithNoCmuId_whenUpdateAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        assertThat(runUpdateAssessmentErrorScenario(
                        "Case management unit ID is required",
                        UpdateFinancialAssessment.builder()
                                .repId(1)
                                .initialAscrId(1)
                                .build()))
                .isTrue();
    }

    @Test
    void givenAnAssessmentWithAnInvalidId_whenUpdateAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        Integer assessmentId = 0;
        assertThat(runUpdateAssessmentErrorScenario(
                        "Financial Assessment id is required",
                        UpdateFinancialAssessment.builder()
                                .id(assessmentId)
                                .repId(1)
                                .initialAscrId(1)
                                .cmuId(1)
                                .build()))
                .isTrue();
    }

    @Test
    void givenAnAssessmentWithAnIdThatDoesNotExist_whenUpdateAssessmentIsInvoked_thenCorrectResponse()
            throws Exception {
        Integer assessmentId = 999;
        assertThat(runUpdateAssessmentErrorScenario(
                        String.format("%d is invalid", assessmentId),
                        UpdateFinancialAssessment.builder()
                                .id(assessmentId)
                                .repId(1)
                                .initialAscrId(1)
                                .cmuId(1)
                                .build()))
                .isTrue();
    }

    @Test
    void givenAnAssessmentWithNoUserModifiedSet_whenUpdateAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        assertThat(runUpdateAssessmentErrorScenario(
                        "Username is required",
                        UpdateFinancialAssessment.builder()
                                .id(existingAssessmentEntities.getFirst().getId())
                                .repId(1)
                                .initialAscrId(1)
                                .cmuId(1)
                                .build()))
                .isTrue();
    }

    @Test
    void givenAnAssessmentWithIncomeEvidence_whenUpdateAssessmentIsInvoked_thenNewIncomeEvidenceIsReturned()
            throws Exception {
        FinancialAssessmentEntity assessmentToUpdate = existingAssessmentEntities.stream()
                .filter(item -> item.getRepOrder().getId().equals(repIdWithNoOutstandingAssessments))
                .findFirst()
                .orElse(null);

        UpdateFinancialAssessment body = TestModelDataBuilder.getUpdateFinancialAssessment();
        body.setFinAssIncomeEvidences(List.of(TestModelDataBuilder.getFinancialAssessmentIncomeEvidence()));
        body.setId(assessmentToUpdate.getId());
        body.setRepId(repIdWithNoOutstandingAssessments);

        runSuccessScenario(
                put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
        FinancialAssessmentEntity updatedAssessment =
                repos.financialAssessment.findById(assessmentToUpdate.getId()).orElse(null);

        assertThat(updatedAssessment.getFinAssIncomeEvidences()).hasSize(1);
        assertThat(updatedAssessment.getFinAssIncomeEvidences().getFirst().getIncomeEvidence())
                .isEqualTo("WAGE SLIP");
    }

    @Test
    void givenAnAssessmentWithMultipleIncomeEvidence_whenUpdateAssessmentIsInvoked_thenNewIncomeEvidenceIsReturned()
            throws Exception {
        FinancialAssessmentEntity assessmentToUpdate = existingAssessmentEntities.stream()
                .filter(item -> item.getRepOrder().getId().equals(repIdWithNoOutstandingAssessments))
                .findFirst()
                .orElse(null);

        UpdateFinancialAssessment body = TestModelDataBuilder.getUpdateFinancialAssessment();
        List<FinancialAssessmentIncomeEvidence> incomeEvidences = getFinancialAssessmentIncomeEvidences();
        body.setFinAssIncomeEvidences(incomeEvidences);
        body.setId(assessmentToUpdate.getId());
        body.setRepId(repIdWithNoOutstandingAssessments);

        runSuccessScenario(
                put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
        FinancialAssessmentEntity updatedAssessment =
                repos.financialAssessment.findById(assessmentToUpdate.getId()).orElse(null);

        assertThat(updatedAssessment.getFinAssIncomeEvidences()).hasSize(3);
    }

    @NotNull
    private static List<FinancialAssessmentIncomeEvidence> getFinancialAssessmentIncomeEvidences() {
        FinancialAssessmentIncomeEvidence financialAssessmentIncomeEvidence =
                TestModelDataBuilder.getFinancialAssessmentIncomeEvidence();
        financialAssessmentIncomeEvidence.setIncomeEvidence("WAGE SLIP");
        FinancialAssessmentIncomeEvidence financialAssessmentIncomeEvidence1 =
                TestModelDataBuilder.getFinancialAssessmentIncomeEvidence();
        financialAssessmentIncomeEvidence1.setIncomeEvidence("BANK STATEMENT");
        FinancialAssessmentIncomeEvidence financialAssessmentIncomeEvidence2 =
                TestModelDataBuilder.getFinancialAssessmentIncomeEvidence();
        financialAssessmentIncomeEvidence2.setIncomeEvidence("P60");

        return List.of(
                financialAssessmentIncomeEvidence,
                financialAssessmentIncomeEvidence1,
                financialAssessmentIncomeEvidence2);
    }

    @Test
    void givenAssessmentBody_whenUpdateAssessmentIsInvoked_thenCorrectResponse() throws Exception {
        FinancialAssessmentEntity assessmentToUpdate = existingAssessmentEntities.stream()
                .filter(item -> item.getRepOrder().getId().equals(repIdWithNoOutstandingAssessments))
                .findFirst()
                .orElse(null);

        Integer assessmentId = Objects.requireNonNull(assessmentToUpdate).getId();
        UpdateFinancialAssessment body = TestModelDataBuilder.getUpdateFinancialAssessment();
        body.setChildWeightings(List.of(TestModelDataBuilder.getChildWeightings()));
        body.setAssessmentDetails(List.of(TestModelDataBuilder.getFinancialAssessmentDetails()));
        body.setRepId(repIdWithNoOutstandingAssessments);
        body.setId(assessmentId);

        FinancialAssessmentDTO expectedResponse = TestModelDataBuilder.getFinancialAssessmentDTO();
        expectedResponse.setId(assessmentToUpdate.getId());
        expectedResponse.setUserModified(body.getUserModified());
        expectedResponse.setRepId(repIdWithNoOutstandingAssessments);
        expectedResponse.setChildWeightings(List.of(TestModelDataBuilder.getChildWeightings()));
        expectedResponse.setAssessmentDetails(List.of(TestModelDataBuilder.getFinancialAssessmentDetails()));
        expectedResponse.setAssessmentType("INIT");
        expectedResponse.setInitialAssessmentDate(body.getInitialAssessmentDate());
        expectedResponse.setNewWorkReason(NewWorkReason.builder()
                .code(assessmentToUpdate.getNewWorkReason().getCode())
                .build());
        expectedResponse.setUserCreated(assessmentToUpdate.getUserCreated());
        expectedResponse.setDateCreated(assessmentToUpdate.getDateCreated());
        expectedResponse.setCmuId(assessmentToUpdate.getCmuId());
        expectedResponse.setUsn(assessmentToUpdate.getUsn());

        runSuccessScenario(
                put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));

        FinancialAssessmentEntity updatedAssessment =
                repos.financialAssessment.findById(assessmentToUpdate.getId()).orElse(null);
        if (updatedAssessment != null) {
            expectedResponse
                    .getAssessmentDetails()
                    .getFirst()
                    .setId(updatedAssessment.getAssessmentDetails().getFirst().getId());
            expectedResponse
                    .getChildWeightings()
                    .getFirst()
                    .setId(updatedAssessment.getChildWeightings().getFirst().getId());
        }
        assertThat(assessmentToUpdate.getUpdated())
                .isNotEqualTo(Objects.requireNonNull(updatedAssessment).getUpdated());
        expectedResponse.setUpdated(updatedAssessment.getUpdated());
        expectedResponse
                .getAssessmentDetails()
                .getFirst()
                .setDateModified(
                        updatedAssessment.getAssessmentDetails().getFirst().getDateModified());
        expectedResponse.setInitialAssessmentDate(updatedAssessment.getInitialAssessmentDate());

        SoftAssertions.assertSoftly(softly -> {
            assertThat(updatedAssessment).isNotNull();
            assertChildWeightingsEqual(expectedResponse.getChildWeightings(), updatedAssessment.getChildWeightings());
            assertFinancialAssessmentDetailsEqual(
                    expectedResponse.getAssessmentDetails(), updatedAssessment.getAssessmentDetails());
            assertFinancialAssessmentEqual(expectedResponse, updatedAssessment);
        });
    }

    @Test
    void givenAssessmentWithNoFullAssessment_whenCreateAssessmentHistoryIsInvoked_thenCorrectResponse()
            throws Exception {
        boolean fullAvailable = false;
        FinancialAssessmentEntity assessmentEntity = existingAssessmentEntities.stream()
                .filter(item -> item.getRepOrder().getId().equals(existingRepOrder.getId()))
                .findFirst()
                .orElse(null);

        assertThat(runSuccessScenario(post(
                                CREATE_ASSESSMENT_HISTORY_URL,
                                Objects.requireNonNull(assessmentEntity).getId(),
                                fullAvailable))
                        .getResponse()
                        .getStatus())
                .isEqualTo(200);
        assertAssessmentHistoryCreated(assessmentEntity, fullAvailable, existingRepOrder);
    }

    @Test
    void givenAssessmentWithAFullAssessment_whenCreateAssessmentHistoryIsInvoked_thenCorrectResponse()
            throws Exception {
        boolean fullAvailable = true;
        FinancialAssessmentEntity assessmentEntity = existingAssessmentEntities.stream()
                .filter(item -> item.getRepOrder().getId().equals(existingRepOrder.getId()))
                .findFirst()
                .orElse(null);

        assertThat(runSuccessScenario(post(
                                CREATE_ASSESSMENT_HISTORY_URL,
                                Objects.requireNonNull(assessmentEntity).getId(),
                                fullAvailable))
                        .getResponse()
                        .getStatus())
                .isEqualTo(200);
        assertAssessmentHistoryCreated(assessmentEntity, fullAvailable, existingRepOrder);
    }

    @Test
    void givenValidFinancialAssessmentId_whenMeansAssessorDetailsIsInvoked_thenPopulatedAssessorDetailsAreReturned()
            throws Exception {
        Integer financialAssessmentId = existingAssessmentEntities.getFirst().getId();

        AssessorDetails expectedIOJAssessorDetails = AssessorDetails.builder()
                .fullName("First Name Of [test-f] Surname Of [test-f]")
                .userName(TestEntityDataBuilder.TEST_USER)
                .build();
        assertThat(runSuccessScenario(
                        expectedIOJAssessorDetails, get(MEANS_ASSESSOR_DETAILS_URL, financialAssessmentId)))
                .isTrue();
    }

    @Test
    void givenUnknownFinancialAssessmentId_whenMeansAssessorDetailsIsInvoked_thenNotFoundResponseIsReturned()
            throws Exception {
        assertThat(runNotFoundErrorScenario(
                        "No Financial Assessment found for financial assessment Id: [99999]",
                        get(MEANS_ASSESSOR_DETAILS_URL, 99999)))
                .isTrue();
    }

    void assertAssessmentHistoryCreated(
            FinancialAssessmentEntity assessmentEntity, Boolean fullAvailable, RepOrderEntity existingRepOrder) {

        var createdHistoryEntities = repos.financialAssessmentsHistory.findAll();
        assertThat(createdHistoryEntities).hasSize(1);
        var createdHistory = createdHistoryEntities.getFirst();

        assertThat(createdHistory.getFinancialAssessment().getId()).isEqualTo(assessmentEntity.getId());
        assertThat(createdHistory.getFullAvailable()).isEqualTo(fullAvailable ? "Y" : "N");
        assertThat(createdHistory.getRepId())
                .isEqualTo(assessmentEntity.getRepOrder().getId());
        assertThat(createdHistory.getInitialAscrId()).isEqualTo(assessmentEntity.getInitialAscrId());
        assertThat(createdHistory.getAssessmentType()).isEqualTo(assessmentEntity.getAssessmentType());
        assertThat(createdHistory.getNewWorkReason().getCode())
                .isEqualTo(assessmentEntity.getNewWorkReason().getCode());
        assertThat(createdHistory.getUserCreated()).isEqualTo(assessmentEntity.getUserCreated());
        assertThat(createdHistory.getCmuId()).isEqualTo(assessmentEntity.getCmuId());
        assertThat(createdHistory.getFassFullStatus()).isEqualTo(assessmentEntity.getFassFullStatus());
        assertThat(createdHistory.getReplaced()).isEqualTo(assessmentEntity.getReplaced());
        assertThat(createdHistory.getMagsOutcome()).isEqualTo(existingRepOrder.getMagsOutcome());
        assertThat(createdHistory.getMagsOutcomeDate()).isEqualTo(existingRepOrder.getMagsOutcomeDate());
        assertThat(createdHistory.getMagsOutcomeDateSet()).isEqualTo(existingRepOrder.getMagsOutcomeDateSet());
        assertThat(createdHistory.getCommittalDate()).isEqualTo(existingRepOrder.getCommittalDate());
        assertThat(createdHistory.getRderCode()).isEqualTo(existingRepOrder.getDecisionReasonCode());
        assertThat(createdHistory.getCcRepDec()).isEqualTo(existingRepOrder.getCrownRepOrderDecision());
        assertThat(createdHistory.getCcRepType()).isEqualTo(existingRepOrder.getCrownRepOrderType());
        assertThat(createdHistory.getCaseType()).isEqualTo(existingRepOrder.getCatyCaseType());

        // Check child weight history.
        assertThat(createdHistory.getAssessmentDetails()).hasSize(1);
        var childWeightHistory = createdHistory.getChildWeightings().getFirst();
        var expectedChildWeightHistory = TestEntityDataBuilder.getChildWeightHistoryEntity();
        assertThat(childWeightHistory.getNoOfChildren()).isEqualTo(expectedChildWeightHistory.getNoOfChildren());
        assertThat(childWeightHistory.getChildWeightingId())
                .isEqualTo(expectedChildWeightHistory.getChildWeightingId());

        // Check assessment details history.
        assertThat(createdHistory.getAssessmentDetails()).hasSize(1);
        var assessmentDetailsHistory = createdHistory.getAssessmentDetails().getFirst();
        var expectedDetailsHistory = TestEntityDataBuilder.getFinancialAssessmentDetailsHistoryEntity();
        assertThat(assessmentDetailsHistory.getCriteriaDetailId())
                .isEqualTo(expectedDetailsHistory.getCriteriaDetailId());
        assertThat(assessmentDetailsHistory.getApplicantAmount())
                .isEqualByComparingTo(
                        expectedDetailsHistory.getApplicantAmount().toString());
        assertThat(assessmentDetailsHistory.getPartnerAmount())
                .isEqualByComparingTo(expectedDetailsHistory.getPartnerAmount().toString());
        assertThat(assessmentDetailsHistory.getUserCreated()).isEqualTo(expectedDetailsHistory.getUserCreated());
    }

    private boolean runCreateAssessmentErrorScenario(String errorMessage, CreateFinancialAssessment body)
            throws Exception {
        return runBadRequestErrorScenario(
                errorMessage,
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }

    private boolean runUpdateAssessmentErrorScenario(String errorMessage, UpdateFinancialAssessment body)
            throws Exception {
        return runBadRequestErrorScenario(
                errorMessage,
                put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }

    private void assertChildWeightingsEqual(
            List<ChildWeightings> expectedChildWeightingsList, List<ChildWeightingsEntity> createdEntities) {
        assertThat(expectedChildWeightingsList).hasSize(createdEntities.size());
        for (int i = 0; i < expectedChildWeightingsList.size(); i++) {
            var currentWeighting = expectedChildWeightingsList.get(i);
            var currentWeightingEntity = createdEntities.get(i);
            assertThat(currentWeighting.getNoOfChildren()).isEqualTo(currentWeightingEntity.getNoOfChildren());
            assertThat(currentWeighting.getChildWeightingId()).isEqualTo(currentWeightingEntity.getChildWeightingId());
        }
    }

    private void assertFinancialAssessmentDetailsEqual(
            List<FinancialAssessmentDetails> expectedAssessmentDetailList,
            List<FinancialAssessmentDetailEntity> createdEntities) {
        assertThat(expectedAssessmentDetailList).hasSize(createdEntities.size());
        for (int i = 0; i < expectedAssessmentDetailList.size(); i++) {
            var currentDetails = expectedAssessmentDetailList.get(i);
            var currentEntity = createdEntities.get(i);
            assertThat(currentDetails.getCriteriaDetailId()).isEqualTo(currentEntity.getCriteriaDetailId());
            assertThat(currentDetails.getApplicantAmount())
                    .isEqualByComparingTo(currentEntity.getApplicantAmount().toString());
            assertThat(currentDetails.getApplicantFrequency()).isEqualTo(currentEntity.getApplicantFrequency());
            assertThat(currentDetails.getPartnerAmount())
                    .isEqualByComparingTo(currentEntity.getPartnerAmount().toString());
            assertThat(currentDetails.getPartnerFrequency()).isEqualTo(currentEntity.getPartnerFrequency());
        }
    }

    private void assertFinancialAssessmentEqual(
            FinancialAssessmentDTO expectedAssessment, FinancialAssessmentEntity actualAssessment) {
        assertThat(expectedAssessment.getRepId())
                .isEqualTo(actualAssessment.getRepOrder().getId());
        assertThat(expectedAssessment.getInitialAscrId()).isEqualTo(actualAssessment.getInitialAscrId());
        assertThat(expectedAssessment.getNewWorkReason().getCode())
                .isEqualTo(actualAssessment.getNewWorkReason().getCode());
        assertThat(expectedAssessment.getUserCreated()).isEqualTo(actualAssessment.getUserCreated());
        assertThat(expectedAssessment.getFassInitStatus()).isEqualTo(actualAssessment.getFassInitStatus());
        assertThat(expectedAssessment.getInitialAssessmentDate())
                .isEqualTo(actualAssessment.getInitialAssessmentDate());
        assertThat(expectedAssessment.getInitTotAggregatedIncome())
                .isEqualByComparingTo(
                        actualAssessment.getInitTotAggregatedIncome().toString());
        assertThat(expectedAssessment.getInitAdjustedIncomeValue())
                .isEqualByComparingTo(
                        actualAssessment.getInitAdjustedIncomeValue().toString());
        assertThat(expectedAssessment.getCmuId()).isEqualTo(actualAssessment.getCmuId());
        assertThat(expectedAssessment.getAssessmentType()).isEqualTo(actualAssessment.getAssessmentType());
        assertThat(expectedAssessment.getInitResult()).isEqualTo(actualAssessment.getInitResult());
        assertThat(expectedAssessment.getInitApplicationEmploymentStatus())
                .isEqualTo(actualAssessment.getInitApplicationEmploymentStatus());
        assertThat(expectedAssessment.getUpdated()).isEqualTo(actualAssessment.getUpdated());
    }
}

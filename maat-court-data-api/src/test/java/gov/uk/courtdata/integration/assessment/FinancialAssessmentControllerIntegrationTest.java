package gov.uk.courtdata.integration.assessment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
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
import gov.uk.courtdata.model.assessment.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.assertj.core.api.SoftAssertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class FinancialAssessmentControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String BASE_URL = "/api/internal/v1/assessment/financial-assessments";
    private static final String ASSESSMENT_URL = BASE_URL + "/{financialAssessmentId}";
    private static final String CHECK_OUTSTANDING_URL = BASE_URL + "/check-outstanding/{repId}";
    private static final String MEANS_ASSESSOR_DETAILS_URL = BASE_URL + "/{financialAssessmentId}/means-assessor-details";
    private static final String CREATE_ASSESSMENT_HISTORY_URL = BASE_URL + "/history/{financialAssessmentId}/fullAvailable/{fullAvailable}";

    private Integer REP_ID_WITH_OUTSTANDING_ASSESSMENTS = 1111;
    private Integer REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS = 2222;
    private Integer REP_ID_WITH_OUTSTANDING_PASSPORT_ASSESSMENTS = 3333;

    @Autowired
    private FinancialAssessmentMapper assessmentMapper;

    private List<FinancialAssessmentEntity> existingAssessmentEntities;
    private RepOrderEntity existingRepOrder;

    @BeforeEach
    void setUp() {
        setupTestData();
    }

    private void setupTestData() {
        Integer REP_ID_DEFAULT = 4444;
        existingRepOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID_DEFAULT));

        RepOrderEntity outstandingRepOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder());
        RepOrderEntity noOutstandingRepOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder());
        RepOrderEntity  outstandingPassportRepOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder());
        REP_ID_WITH_OUTSTANDING_ASSESSMENTS = outstandingRepOrder.getId();
        REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS = noOutstandingRepOrder.getId();
        REP_ID_WITH_OUTSTANDING_PASSPORT_ASSESSMENTS = outstandingPassportRepOrder.getId();

        UserEntity userEntity = TestEntityDataBuilder.getUserEntity(TestEntityDataBuilder.TEST_USER);
        repos.user.save(userEntity);

        NewWorkReasonEntity newWorkReasonEntity = repos.mockNewWorkReason.save(
                TestEntityDataBuilder.getFmaNewWorkReasonEntity());

        List<FinancialAssessmentEntity> assessmentsToCreate = List.of(
                TestEntityDataBuilder.getCustomFinancialAssessmentEntity(
                        REP_ID_WITH_OUTSTANDING_ASSESSMENTS,
                        "IN PROGRESS",
                        newWorkReasonEntity),
                TestEntityDataBuilder.getCustomFinancialAssessmentEntity(
                        REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS,
                        "COMPLETE",
                        newWorkReasonEntity),
                TestEntityDataBuilder.getCustomFinancialAssessmentEntity(
                        REP_ID_WITH_OUTSTANDING_PASSPORT_ASSESSMENTS,
                        "COMPLETE",
                        newWorkReasonEntity),
                TestEntityDataBuilder.getFinancialAssessmentEntityWithRelationships(existingRepOrder.getId(), newWorkReasonEntity)
        );

        existingAssessmentEntities = repos.financialAssessment.saveAll(assessmentsToCreate);

        repos.passportAssessment.save(
                PassportAssessmentEntity.builder()
                        .repOrder(outstandingPassportRepOrder)
                        .pastStatus("IN PROGRESS")
                        .build());
    }

    @Test
    void givenAZeroAssessmentId_whenGetAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario("Financial Assessment id is required", get(ASSESSMENT_URL, 0)));
    }

    @Test
    void givenAnInvalidAssessmentId_whenGetAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer invalidAssessmentId = 999;
        assertTrue(runBadRequestErrorScenario(String.format("%d is invalid", invalidAssessmentId), get(ASSESSMENT_URL, invalidAssessmentId)));
    }

    @Test
    void givenAValidAssessmentId_whenGetAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        var testAssessment = existingAssessmentEntities.get(0);
        assertTrue(runSuccessScenario(
                assessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(testAssessment),
                get(ASSESSMENT_URL, testAssessment.getId())));
    }

    @Test
    void givenAValidAssessmentId_whenGetAssessmentIsInvoked_theCorrectRelationshipsResponseIsReturned() throws Exception {
        var testAssessment = existingAssessmentEntities.get(3);
        assertTrue(runSuccessScenario(assessmentMapper.financialAssessmentEntityToFinancialAssessmentDTO(testAssessment), get(ASSESSMENT_URL, testAssessment.getId())));
    }

    @Test
    void givenAZeroAssessmentId_whenDeleteAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario("Financial Assessment id is required", delete(ASSESSMENT_URL, 0)));
    }

    @Test
    void givenAnInvalidAssessmentId_whenDeleteAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer invalidAssessmentId = 999;
        assertTrue(runBadRequestErrorScenario(String.format("%d is invalid", invalidAssessmentId), delete(ASSESSMENT_URL, invalidAssessmentId)));
    }

    @Test
    void givenAValidAssessmentId_whenDeleteAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertThat(runSuccessScenario(delete(ASSESSMENT_URL, existingAssessmentEntities.get(0).getId())).getResponse().getStatus()).isEqualTo(200);
    }

    @Test
    void givenARepIdWithNoOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_theCorrectResponseIsReturned() throws Exception {
        OutstandingAssessmentResultDTO expectedResponse = OutstandingAssessmentResultDTO.builder().build();
        assertTrue(runSuccessScenario(expectedResponse, get(CHECK_OUTSTANDING_URL, REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS)));
    }

    @Test
    void givenARepIdWithOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_theCorrectResponseIsReturned() throws Exception {
        OutstandingAssessmentResultDTO expectedResponse =
                OutstandingAssessmentResultDTO.builder()
                        .outstandingAssessments(true)
                        .message(FinancialAssessmentImpl.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND).build();
        assertTrue(runSuccessScenario(expectedResponse, get(CHECK_OUTSTANDING_URL, REP_ID_WITH_OUTSTANDING_ASSESSMENTS)));
    }

    @Test
    void givenARepIdWithOutstandingPassportAssessments_whenCheckForOutstandingAssessmentsIsInvoked_theCorrectResponseIsReturned() throws Exception {
        OutstandingAssessmentResultDTO expectedResponse =
                OutstandingAssessmentResultDTO.builder()
                        .outstandingAssessments(true)
                        .message(FinancialAssessmentImpl.MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND).build();
        assertTrue(runSuccessScenario(expectedResponse, get(CHECK_OUTSTANDING_URL, REP_ID_WITH_OUTSTANDING_PASSPORT_ASSESSMENTS)));
    }

    @Test
    void givenAnAssessmentWithNoRepId_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runCreateAssessmentErrorScenario("Rep Order ID is required", CreateFinancialAssessment.builder().build()));
    }

    @Test
    void givenAnAssessmentWithNoCriteriaId_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runCreateAssessmentErrorScenario(
                "Assessment Criteria ID is required",
                CreateFinancialAssessment.builder().repId(1).build()));
    }

    @Test
    void givenAnAssessmentWithNoCmuId_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runCreateAssessmentErrorScenario(
                "Case management unit ID is required",
                CreateFinancialAssessment.builder().repId(1).initialAscrId(1).build()));
    }

    @Test
    void givenAnAssessmentWithNoNewWorkReason_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runCreateAssessmentErrorScenario(
                "New work reason code is required",
                CreateFinancialAssessment.builder().repId(1).initialAscrId(1).cmuId(1).build()));
    }

    @Test
    void givenAnAssessmentWithNoUser_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runCreateAssessmentErrorScenario(
                "Username is required",
                CreateFinancialAssessment.builder().repId(1).initialAscrId(1).cmuId(1).nworCode("FMA").build()));
    }

    @Test
    void givenAValidAssessmentBody_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        CreateFinancialAssessment body = TestModelDataBuilder.getCreateFinancialAssessmentWithRelationships();
        body.setRepId(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS);

        FinancialAssessmentDTO expectedResponse = TestModelDataBuilder.getFinancialAssessmentDTO();
        expectedResponse.setRepId(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS);
        expectedResponse.setChildWeightings(List.of(TestModelDataBuilder.getChildWeightings()));
        expectedResponse.setAssessmentDetails(List.of(TestModelDataBuilder.getFinancialAssessmentDetails()));
        expectedResponse.setAssessmentType("INIT");
        expectedResponse.setInitialAssessmentDate(body.getInitialAssessmentDate());

        MvcResult result =
                runSuccessScenario(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));

        List<FinancialAssessmentEntity> matchingAssessments =
                repos.financialAssessment.findAll()
                        .stream()
                        .filter(assessment -> assessment.getRepOrder().getId().equals(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS))
                        .collect(Collectors.toList());

        FinancialAssessmentEntity createdAssessment =
                matchingAssessments.stream().filter(assessment -> assessment.getReplaced().equals("N")).collect(Collectors.toList()).get(0);

        expectedResponse.setId(createdAssessment.getId());
        expectedResponse.setDateCreated(createdAssessment.getDateCreated());
        expectedResponse.setUpdated(createdAssessment.getUpdated());
        expectedResponse.getAssessmentDetails().get(0).setId(createdAssessment.getAssessmentDetails().get(0).getId());
        expectedResponse.getChildWeightings().get(0).setId(createdAssessment.getChildWeightings().get(0).getId());
        expectedResponse.getAssessmentDetails().get(0).setDateModified(createdAssessment.getAssessmentDetails().get(0).getDateModified());
        expectedResponse.setInitialAssessmentDate(createdAssessment.getInitialAssessmentDate());

        SoftAssertions.assertSoftly(softly -> {
            assertThat(matchingAssessments.size()).isEqualTo(2);
            assertThat((int) matchingAssessments.stream().filter(assessment -> assessment.getReplaced().equals("Y")).count())
                    .isEqualTo(1);
            assertChildWeightingsEqual(expectedResponse.getChildWeightings(), createdAssessment.getChildWeightings());
            assertFinancialAssessmentDetailsEqual(expectedResponse.getAssessmentDetails(), createdAssessment.getAssessmentDetails());
            assertFinancialAssessmentEqual(expectedResponse, createdAssessment);
        });
    }

    @Test
    void givenAnAssessmentWithNoRepId_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runUpdateAssessmentErrorScenario("Rep Order ID is required", UpdateFinancialAssessment.builder().build()));
    }

    @Test
    void givenAnAssessmentWithNoCriteriaId_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runUpdateAssessmentErrorScenario(
                "Assessment Criteria ID is required",
                UpdateFinancialAssessment.builder().repId(1).build()));
    }

    @Test
    void givenAnAssessmentWithNoCmuId_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runUpdateAssessmentErrorScenario(
                "Case management unit ID is required",
                UpdateFinancialAssessment.builder().repId(1).initialAscrId(1).build()));
    }

    @Test
    void givenAnAssessmentWithAnInvalidId_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer assessmentId = 0;
        assertTrue(runUpdateAssessmentErrorScenario(
                "Financial Assessment id is required",
                UpdateFinancialAssessment.builder().id(assessmentId).repId(1).initialAscrId(1).cmuId(1).build()));
    }

    @Test
    void givenAnAssessmentWithAnIdThatDoesNotExist_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer assessmentId = 999;
        assertTrue(runUpdateAssessmentErrorScenario(
                String.format("%d is invalid", assessmentId),
                UpdateFinancialAssessment.builder().id(assessmentId).repId(1).initialAscrId(1).cmuId(1).build()));
    }

    @Test
    void givenAnAssessmentWithNoUserModifiedSet_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runUpdateAssessmentErrorScenario(
                "Username is required",
                UpdateFinancialAssessment.builder().id(existingAssessmentEntities.get(0).getId()).repId(1).initialAscrId(1).cmuId(1).build()));
    }

    @Test
    void givenAnAssessmentWithIncomeEvidence_whenUpdateAssessmentIsInvoked_thenNewIncomeEvidenceIsReturned() throws Exception {
        FinancialAssessmentEntity assessmentToUpdate =
                existingAssessmentEntities.stream().filter(item -> item.getRepOrder().getId().equals(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS)).findFirst().orElse(null);

        UpdateFinancialAssessment body = TestModelDataBuilder.getUpdateFinancialAssessment();
        body.setFinAssIncomeEvidences(List.of(TestModelDataBuilder.getFinancialAssessmentIncomeEvidence()));
        body.setId(assessmentToUpdate.getId());
        body.setRepId(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS);

        MvcResult result =
                runSuccessScenario(put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
        FinancialAssessmentEntity updatedAssessment = repos.financialAssessment.findById(assessmentToUpdate.getId()).orElse(null);

        assertThat(updatedAssessment.getFinAssIncomeEvidences().size()).isEqualTo(1);
        assertThat(updatedAssessment.getFinAssIncomeEvidences().get(0).getIncomeEvidence()).isEqualTo("WAGE SLIP");
    }

    @Test
    void givenAnAssessmentWithMultipleIncomeEvidence_whenUpdateAssessmentIsInvoked_thenNewIncomeEvidenceIsReturned() throws Exception {
        FinancialAssessmentEntity assessmentToUpdate =
                existingAssessmentEntities.stream().filter(item -> item.getRepOrder().getId().equals(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS)).findFirst().orElse(null);

        UpdateFinancialAssessment body = TestModelDataBuilder.getUpdateFinancialAssessment();
        List<FinancialAssessmentIncomeEvidence> incomeEvidences = getFinancialAssessmentIncomeEvidences();

        body.setFinAssIncomeEvidences(incomeEvidences);
        body.setId(assessmentToUpdate.getId());
        body.setRepId(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS);

        MvcResult result =
                runSuccessScenario(put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
        FinancialAssessmentEntity updatedAssessment = repos.financialAssessment.findById(assessmentToUpdate.getId()).orElse(null);

        assertThat(updatedAssessment.getFinAssIncomeEvidences().size()).isEqualTo(3);
    }

    @NotNull
    private static List<FinancialAssessmentIncomeEvidence> getFinancialAssessmentIncomeEvidences() {
        FinancialAssessmentIncomeEvidence financialAssessmentIncomeEvidence = TestModelDataBuilder.getFinancialAssessmentIncomeEvidence();
        financialAssessmentIncomeEvidence.setIncomeEvidence("WAGE SLIP");
        FinancialAssessmentIncomeEvidence financialAssessmentIncomeEvidence1 = TestModelDataBuilder.getFinancialAssessmentIncomeEvidence();
        financialAssessmentIncomeEvidence.setIncomeEvidence("BANK STATEMENT");
        FinancialAssessmentIncomeEvidence financialAssessmentIncomeEvidence2 = TestModelDataBuilder.getFinancialAssessmentIncomeEvidence();
        financialAssessmentIncomeEvidence.setIncomeEvidence("P60");

        List<FinancialAssessmentIncomeEvidence> incomeEvidences = List.of(
                        financialAssessmentIncomeEvidence,
                        financialAssessmentIncomeEvidence1,
                        financialAssessmentIncomeEvidence2);
        return incomeEvidences;
    }

    @Test
    void givenAValidAssessmentBody_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        FinancialAssessmentEntity assessmentToUpdate =
                existingAssessmentEntities.stream().filter(item -> item.getRepOrder().getId().equals(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS)).findFirst().orElse(null);

        Integer assessmentId = Objects.requireNonNull(assessmentToUpdate).getId();
        UpdateFinancialAssessment body = TestModelDataBuilder.getUpdateFinancialAssessment();
        body.setChildWeightings(List.of(TestModelDataBuilder.getChildWeightings()));
        body.setAssessmentDetails(List.of(TestModelDataBuilder.getFinancialAssessmentDetails()));
        body.setRepId(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS);
        body.setId(assessmentId);

        FinancialAssessmentDTO expectedResponse = TestModelDataBuilder.getFinancialAssessmentDTO();
        expectedResponse.setId(assessmentToUpdate.getId());
        expectedResponse.setUserModified(body.getUserModified());
        expectedResponse.setRepId(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS);
        expectedResponse.setChildWeightings(List.of(TestModelDataBuilder.getChildWeightings()));
        expectedResponse.setAssessmentDetails(List.of(TestModelDataBuilder.getFinancialAssessmentDetails()));
        expectedResponse.setAssessmentType("INIT");
        expectedResponse.setInitialAssessmentDate(body.getInitialAssessmentDate());
        expectedResponse.setNewWorkReason(NewWorkReason.builder().code(assessmentToUpdate.getNewWorkReason().getCode()).build());
        expectedResponse.setUserCreated(assessmentToUpdate.getUserCreated());
        expectedResponse.setDateCreated(assessmentToUpdate.getDateCreated());
        expectedResponse.setCmuId(assessmentToUpdate.getCmuId());
        expectedResponse.setUsn(assessmentToUpdate.getUsn());


        MvcResult result =
                runSuccessScenario(put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));

        FinancialAssessmentEntity updatedAssessment = repos.financialAssessment.findById(assessmentToUpdate.getId()).orElse(null);
        if (updatedAssessment != null) {
            expectedResponse.getAssessmentDetails().get(0).setId(updatedAssessment.getAssessmentDetails().get(0).getId());
            expectedResponse.getChildWeightings().get(0).setId(updatedAssessment.getChildWeightings().get(0).getId());
        }
        assertThat(assessmentToUpdate.getUpdated()).isNotEqualTo(Objects.requireNonNull(updatedAssessment).getUpdated());
        expectedResponse.setUpdated(updatedAssessment.getUpdated());
        expectedResponse.getAssessmentDetails().get(0).setDateModified(updatedAssessment.getAssessmentDetails().get(0).getDateModified());
        expectedResponse.setInitialAssessmentDate(updatedAssessment.getInitialAssessmentDate());

        SoftAssertions.assertSoftly(softly -> {
            assertThat(updatedAssessment).isNotNull();
            assertChildWeightingsEqual(expectedResponse.getChildWeightings(), updatedAssessment.getChildWeightings());
            assertFinancialAssessmentDetailsEqual(expectedResponse.getAssessmentDetails(), updatedAssessment.getAssessmentDetails());
            assertFinancialAssessmentEqual(expectedResponse, updatedAssessment);
        });
    }

    @Test
    void givenAValidAssessmentIdWithoutAFullAssessmentAvailable_whenCreateAssessmentHistoryIsInvoked_theCorrectResponseIsReturned() throws Exception {
        boolean fullAvailable = false;
        FinancialAssessmentEntity assessmentEntity =
                existingAssessmentEntities.stream().filter(item -> item.getRepOrder().getId().equals(existingRepOrder.getId())).findFirst().orElse(null);

        assertThat(runSuccessScenario(post(CREATE_ASSESSMENT_HISTORY_URL, Objects.requireNonNull(assessmentEntity).getId(), fullAvailable)).getResponse().getStatus()).isEqualTo(200);
        assertAssessmentHistoryCreated(assessmentEntity, fullAvailable, existingRepOrder);
    }

    @Test
    void givenAValidAssessmentIdWithAFullAssessmentAvailable_whenCreateAssessmentHistoryIsInvoked_theCorrectResponseIsReturned() throws Exception {
        boolean fullAvailable = true;
        FinancialAssessmentEntity assessmentEntity =
                existingAssessmentEntities.stream().filter(item -> item.getRepOrder().getId().equals(existingRepOrder.getId())).findFirst().orElse(null);

        assertThat(runSuccessScenario(post(CREATE_ASSESSMENT_HISTORY_URL, Objects.requireNonNull(assessmentEntity).getId(), fullAvailable)).getResponse().getStatus()).isEqualTo(200);
        assertAssessmentHistoryCreated(assessmentEntity, fullAvailable, existingRepOrder);
    }

    @Test
    public void givenValidFinancialAssessmentId_whenMeansAssessorDetailsIsInvoked_thenPopulatedAssessorDetailsAreReturned() throws Exception {
        Integer financialAssessmentId = existingAssessmentEntities.get(0).getId();

        AssessorDetails expectedIOJAssessorDetails = AssessorDetails.builder()
                .fullName("First Name Of [test-f] Surname Of [test-f]")
                .userName(TestEntityDataBuilder.TEST_USER)
                .build();

        runSuccessScenario(expectedIOJAssessorDetails, get(MEANS_ASSESSOR_DETAILS_URL, financialAssessmentId));
    }

    @Test
    public void givenUnknownFinancialAssessmentId_whenMeansAssessorDetailsIsInvoked_thenNotFoundResponseIsReturned() throws Exception {
        runNotFoundErrorScenario("No Financial Assessment found for financial assessment Id: [99999]",
                get(MEANS_ASSESSOR_DETAILS_URL, 99999));
    }

    public void assertAssessmentHistoryCreated(FinancialAssessmentEntity assessmentEntity,
                                               Boolean fullAvailable,
                                               RepOrderEntity existingRepOrder) {

        var createdHistoryEntities = repos.financialAssessmentsHistory.findAll();
        assertThat(createdHistoryEntities.size()).isEqualTo(1);
        var createdHistory = createdHistoryEntities.get(0);

        assertThat(createdHistory.getFinancialAssessment().getId()).isEqualTo(assessmentEntity.getId());
        assertThat(createdHistory.getFullAvailable()).isEqualTo(fullAvailable ? "Y" : "N");
        assertThat(createdHistory.getRepId()).isEqualTo(assessmentEntity.getRepOrder().getId());
        assertThat(createdHistory.getInitialAscrId()).isEqualTo(assessmentEntity.getInitialAscrId());
        assertThat(createdHistory.getAssessmentType()).isEqualTo(assessmentEntity.getAssessmentType());
        assertThat(createdHistory.getNewWorkReason().getCode()).isEqualTo(assessmentEntity.getNewWorkReason().getCode());
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
        assertThat(createdHistory.getAssessmentDetails().size()).isEqualTo(1);
        var childWeightHistory = createdHistory.getChildWeightings().get(0);
        var expectedChildWeightHistory = TestEntityDataBuilder.getChildWeightHistoryEntity();
        assertThat(childWeightHistory.getNoOfChildren()).isEqualTo(expectedChildWeightHistory.getNoOfChildren());
        assertThat(childWeightHistory.getChildWeightingId()).isEqualTo(expectedChildWeightHistory.getChildWeightingId());

        // Check assessment details history.
        assertThat(createdHistory.getAssessmentDetails().size()).isEqualTo(1);
        var assessmentDetailsHistory = createdHistory.getAssessmentDetails().get(0);
        var expectedDetailsHistory = TestEntityDataBuilder.getFinancialAssessmentDetailsHistoryEntity();
        assertThat(assessmentDetailsHistory.getCriteriaDetailId()).isEqualTo(expectedDetailsHistory.getCriteriaDetailId());
        assertThat(assessmentDetailsHistory.getApplicantAmount()).isEqualByComparingTo(expectedDetailsHistory.getApplicantAmount().toString());
        assertThat(assessmentDetailsHistory.getPartnerAmount()).isEqualByComparingTo(expectedDetailsHistory.getPartnerAmount().toString());
        assertThat(assessmentDetailsHistory.getUserCreated()).isEqualTo(expectedDetailsHistory.getUserCreated());
    }

    private boolean runCreateAssessmentErrorScenario(String errorMessage, CreateFinancialAssessment body) throws Exception {
        return runBadRequestErrorScenario(
                errorMessage,
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }

    private boolean runUpdateAssessmentErrorScenario(String errorMessage, UpdateFinancialAssessment body) throws Exception {
        return runBadRequestErrorScenario(
                errorMessage,
                put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }

    private void assertChildWeightingsEqual(List<ChildWeightings> expectedChildWeightingsList, List<ChildWeightingsEntity> createdEntities) {
        assertThat(expectedChildWeightingsList.size()).isEqualTo(createdEntities.size());
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
        assertThat(expectedAssessmentDetailList.size()).isEqualTo(createdEntities.size());
        for (int i = 0; i < expectedAssessmentDetailList.size(); i++) {
            var currentDetails = expectedAssessmentDetailList.get(i);
            var currentEntity = createdEntities.get(i);
            assertThat(currentDetails.getCriteriaDetailId()).isEqualTo(currentEntity.getCriteriaDetailId());
            assertThat(currentDetails.getApplicantAmount()).isEqualByComparingTo(currentEntity.getApplicantAmount().toString());
            assertThat(currentDetails.getApplicantFrequency()).isEqualTo(currentEntity.getApplicantFrequency());
            assertThat(currentDetails.getPartnerAmount()).isEqualByComparingTo(currentEntity.getPartnerAmount().toString());
            assertThat(currentDetails.getPartnerFrequency()).isEqualTo(currentEntity.getPartnerFrequency());
        }
    }

    private void assertFinancialAssessmentEqual(FinancialAssessmentDTO expectedAssessment, FinancialAssessmentEntity actualAssessment) {
        assertThat(expectedAssessment.getRepId()).isEqualTo(actualAssessment.getRepOrder().getId());
        assertThat(expectedAssessment.getInitialAscrId()).isEqualTo(actualAssessment.getInitialAscrId());
        assertThat(expectedAssessment.getNewWorkReason().getCode()).isEqualTo(actualAssessment.getNewWorkReason().getCode());
        assertThat(expectedAssessment.getUserCreated()).isEqualTo(actualAssessment.getUserCreated());
        assertThat(expectedAssessment.getFassInitStatus()).isEqualTo(actualAssessment.getFassInitStatus());
        assertThat(expectedAssessment.getInitialAssessmentDate()).isEqualTo(actualAssessment.getInitialAssessmentDate());
        assertThat(expectedAssessment.getInitTotAggregatedIncome()).isEqualByComparingTo(actualAssessment.getInitTotAggregatedIncome().toString());
        assertThat(expectedAssessment.getInitAdjustedIncomeValue()).isEqualByComparingTo(actualAssessment.getInitAdjustedIncomeValue().toString());
        assertThat(expectedAssessment.getCmuId()).isEqualTo(actualAssessment.getCmuId());
        assertThat(expectedAssessment.getAssessmentType()).isEqualTo(actualAssessment.getAssessmentType());
        assertThat(expectedAssessment.getInitResult()).isEqualTo(actualAssessment.getInitResult());
        assertThat(expectedAssessment.getInitApplicationEmploymentStatus()).isEqualTo(actualAssessment.getInitApplicationEmploymentStatus());
        assertThat(expectedAssessment.getUpdated()).isEqualTo(actualAssessment.getUpdated());
    }
}

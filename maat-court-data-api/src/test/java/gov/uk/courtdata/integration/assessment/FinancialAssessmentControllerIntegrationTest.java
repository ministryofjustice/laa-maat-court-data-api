package gov.uk.courtdata.integration.assessment;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.NewWorkReason;
import gov.uk.courtdata.model.assessment.*;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class, NewWorkReasonRepository.class})
public class FinancialAssessmentControllerIntegrationTest extends MockMvcIntegrationTest {

    private final String BASE_URL = "/api/internal/v1/assessment/financial-assessments/";
    private final String ASSESSMENT_URL = BASE_URL + "{financialAssessmentId}";
    private final String CHECK_OUTSTANDING_URL = BASE_URL + "/check-outstanding/{repId}";
    private final Integer REP_ID_WITH_OUTSTANDING_ASSESSMENTS = 1111;
    private final Integer REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS = 2222;
    private final Integer REP_ID_WITH_OUTSTANDING_PASSPORT_ASSESSMENTS = 3333;

    @Autowired
    private FinancialAssessmentRepository financialAssessmentRepository;
    @Autowired
    private HardshipReviewRepository hardshipReviewRepository;
    @Autowired
    private PassportAssessmentRepository passportAssessmentRepository;
    @Autowired
    private FinancialAssessmentMapper assessmentMapper;
    @Autowired
    private NewWorkReasonRepository newWorkReasonRepository;
    @Autowired
    private ChildWeightingsRepository childWeightingsRepository;
    @Autowired
    private FinancialAssessmentDetailsRepository financialAssessmentDetailsRepository;
    @Autowired
    private RepOrderRepository repOrderRepository;
    @Autowired
    private ChildWeightHistoryRepository childWeightHistoryRepository;
    @Autowired
    private FinancialAssessmentsHistoryRepository financialAssessmentsHistoryRepository;
    @Autowired
    private FinancialAssessmentDetailsHistoryRepository financialAssessmentDetailsHistoryRepository;

    private List<FinancialAssessmentEntity> existingAssessmentEntities;
    private RepOrderEntity existingRepOrder;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        setupTestData();
    }

    private void setupTestData() {

        hardshipReviewRepository.deleteAll();
        passportAssessmentRepository.deleteAll();
        childWeightingsRepository.deleteAll();
        financialAssessmentDetailsRepository.deleteAll();
        financialAssessmentsHistoryRepository.deleteAll();
        financialAssessmentDetailsHistoryRepository.deleteAll();
        childWeightHistoryRepository.deleteAll();
        financialAssessmentRepository.deleteAll();
        newWorkReasonRepository.deleteAll();

        LocalDateTime testCreationDate = LocalDateTime.of(2022, 1, 1, 12, 0);
        String testUser = "test-f";

        existingRepOrder = repOrderRepository.save(
                RepOrderEntity.builder()
                        .id(4444)
                        .catyCaseType("case-type")
                        .magsOutcome("outcome")
                        .magsOutcomeDate(testCreationDate.toString())
                        .magsOutcomeDateSet(testCreationDate.toLocalDate())
                        .committalDate(testCreationDate.toLocalDate())
                        .rderCode("rder-code")
                        .ccRepDec("cc-rep-doc")
                        .ccRepType("cc-rep-type")
                        .build()
        );

        NewWorkReasonEntity newWorkReasonEntity = newWorkReasonRepository.save(
                NewWorkReasonEntity.builder()
                        .code("FMA")
                        .type("ASS")
                        .description("")
                        .dateCreated(testCreationDate)
                        .userCreated(testUser)
                        .build());

        var assessmentDetails = TestEntityDataBuilder.getFinancialAssessmentDetailsEntity();
        assessmentDetails.setId(null);

        FinancialAssessmentEntity assessmentWithWeightingsAndDetails = FinancialAssessmentEntity.builder()
                .repId(existingRepOrder.getId())
                .assessmentType("INIT")
                .fassFullStatus("COMPLETE")
                .dateCreated(testCreationDate)
                .userCreated(testUser)
                .initialAscrId(10)
                .usn(11)
                .cmuId(12)
                .replaced("N")
                .newWorkReason(newWorkReasonEntity)
                .build();
        assessmentWithWeightingsAndDetails.addAssessmentDetail(assessmentDetails);
        assessmentWithWeightingsAndDetails.addChildWeighting(TestEntityDataBuilder.getChildWeightingsEntity());

        List<FinancialAssessmentEntity> assessmentsToCreate = List.of(

                FinancialAssessmentEntity.builder()
                        .repId(REP_ID_WITH_OUTSTANDING_ASSESSMENTS)
                        .fassFullStatus("IN PROGRESS")
                        .dateCreated(testCreationDate)
                        .userCreated(testUser)
                        .initialAscrId(1)
                        .usn(2)
                        .cmuId(3)
                        .replaced("N")
                        .newWorkReason(newWorkReasonEntity)
                        .build(),
                FinancialAssessmentEntity.builder()
                        .repId(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS)
                        .fassFullStatus("COMPLETE")
                        .dateCreated(testCreationDate)
                        .userCreated(testUser)
                        .initialAscrId(4)
                        .usn(5)
                        .cmuId(6)
                        .replaced("N")
                        .newWorkReason(newWorkReasonEntity)
                        .build(),
                FinancialAssessmentEntity.builder()
                        .repId(REP_ID_WITH_OUTSTANDING_PASSPORT_ASSESSMENTS)
                        .fassFullStatus("COMPLETE")
                        .dateCreated(testCreationDate)
                        .userCreated(testUser)
                        .initialAscrId(7)
                        .usn(8)
                        .cmuId(9)
                        .replaced("N")
                        .newWorkReason(newWorkReasonEntity)
                        .build(),
                assessmentWithWeightingsAndDetails
        );

        existingAssessmentEntities = financialAssessmentRepository.saveAll(assessmentsToCreate);

        passportAssessmentRepository.save(
                PassportAssessmentEntity.builder()
                        .repId(REP_ID_WITH_OUTSTANDING_PASSPORT_ASSESSMENTS)
                        .pastStatus("IN PROGRESS")
                        .build());
    }

    @Test
    public void givenAZeroAssessmentId_whenGetAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runBadRequestErrorScenario("Financial Assessment id is required", get(ASSESSMENT_URL, 0));
    }

    @Test
    public void givenAnInvalidAssessmentId_whenGetAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer invalidAssessmentId = 999;
        runBadRequestErrorScenario(String.format("%d is invalid", invalidAssessmentId), get(ASSESSMENT_URL, invalidAssessmentId));
    }

    @Test
    public void givenAValidAssessmentId_whenGetAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        var testAssessment = existingAssessmentEntities.get(0);
        runSuccessScenario(
                assessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(testAssessment),
                get(ASSESSMENT_URL, testAssessment.getId()));
    }

    @Test
    public void givenAZeroAssessmentId_whenDeleteAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runBadRequestErrorScenario("Financial Assessment id is required", delete(ASSESSMENT_URL, 0));
    }

    @Test
    public void givenAnInvalidAssessmentId_whenDeleteAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer invalidAssessmentId = 999;
        runBadRequestErrorScenario(String.format("%d is invalid", invalidAssessmentId), delete(ASSESSMENT_URL, invalidAssessmentId));
    }

    @Test
    public void givenAValidAssessmentId_whenDeleteAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runSuccessScenario(delete(ASSESSMENT_URL, existingAssessmentEntities.get(0).getId()));
    }

    @Test
    public void givenARepIdWithNoOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_theCorrectResponseIsReturned() throws Exception {
        OutstandingAssessmentResultDTO expectedResponse = OutstandingAssessmentResultDTO.builder().build();
        runSuccessScenario(expectedResponse, get(CHECK_OUTSTANDING_URL, REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS));
    }

    @Test
    public void givenARepIdWithOutstandingAssessments_whenCheckForOutstandingAssessmentsIsInvoked_theCorrectResponseIsReturned() throws Exception {
        OutstandingAssessmentResultDTO expectedResponse =
                OutstandingAssessmentResultDTO.builder()
                        .outstandingAssessments(true)
                        .message(FinancialAssessmentImpl.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND).build();
        runSuccessScenario(expectedResponse, get(CHECK_OUTSTANDING_URL, REP_ID_WITH_OUTSTANDING_ASSESSMENTS));
    }

    @Test
    public void givenARepIdWithOutstandingPassportAssessments_whenCheckForOutstandingAssessmentsIsInvoked_theCorrectResponseIsReturned() throws Exception {
        OutstandingAssessmentResultDTO expectedResponse =
                OutstandingAssessmentResultDTO.builder()
                        .outstandingAssessments(true)
                        .message(FinancialAssessmentImpl.MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND).build();
        runSuccessScenario(expectedResponse, get(CHECK_OUTSTANDING_URL, REP_ID_WITH_OUTSTANDING_PASSPORT_ASSESSMENTS));
    }

    @Test
    public void givenAnAssessmentWithNoRepId_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreateAssessmentErrorScenario("Rep Order ID is required", CreateFinancialAssessment.builder().build());
    }

    @Test
    public void givenAnAssessmentWithNoCriteriaId_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreateAssessmentErrorScenario(
                "Assessment Criteria ID is required",
                CreateFinancialAssessment.builder().repId(1).build());
    }

    @Test
    public void givenAnAssessmentWithNoCmuId_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreateAssessmentErrorScenario(
                "Case management unit ID is required",
                CreateFinancialAssessment.builder().repId(1).initialAscrId(1).build());
    }

    @Test
    public void givenAnAssessmentWithNoNewWorkReason_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreateAssessmentErrorScenario(
                "New work reason code is required",
                CreateFinancialAssessment.builder().repId(1).initialAscrId(1).cmuId(1).build());
    }

    @Test
    public void givenAnAssessmentWithNoUser_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreateAssessmentErrorScenario(
                "Username is required",
                CreateFinancialAssessment.builder().repId(1).initialAscrId(1).cmuId(1).nworCode("FMA").build());
    }

    @Test
    public void givenAValidAssessmentBody_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
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
                financialAssessmentRepository.findAll()
                        .stream()
                        .filter(assessment -> assessment.getRepId().equals(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS))
                        .collect(Collectors.toList());

        FinancialAssessmentEntity createdAssessment =
                matchingAssessments.stream().filter(assessment -> assessment.getReplaced().equals("N")).collect(Collectors.toList()).get(0);

        expectedResponse.setId(createdAssessment.getId());
        expectedResponse.setDateCreated(createdAssessment.getDateCreated());
        expectedResponse.setUpdated(createdAssessment.getUpdated());

        SoftAssertions.assertSoftly(softly -> {
            assertThat(matchingAssessments.size()).isEqualTo(2);
            assertThat((int) matchingAssessments.stream().filter(assessment -> assessment.getReplaced().equals("Y")).count())
                    .isEqualTo(1);
            assertChildWeightingsEqual(expectedResponse.getChildWeightings(), createdAssessment.getChildWeightings());
            assertFinancialAssessmentDetailsEqual(expectedResponse.getAssessmentDetails(), createdAssessment.getAssessmentDetails());
            assertFinancialAssessmentEqual(expectedResponse, createdAssessment);
        });

        assertThat(objectMapper.writeValueAsString(expectedResponse)).isEqualTo(result.getResponse().getContentAsString());
    }

    @Test
    public void givenAnAssessmentWithNoRepId_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdateAssessmentErrorScenario("Rep Order ID is required", UpdateFinancialAssessment.builder().build());
    }

    @Test
    public void givenAnAssessmentWithNoCriteriaId_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdateAssessmentErrorScenario(
                "Assessment Criteria ID is required",
                UpdateFinancialAssessment.builder().repId(1).build());
    }

    @Test
    public void givenAnAssessmentWithNoCmuId_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdateAssessmentErrorScenario(
                "Case management unit ID is required",
                UpdateFinancialAssessment.builder().repId(1).initialAscrId(1).build());
    }

    @Test
    public void givenAnAssessmentWithAnInvalidId_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer assessmentId = 0;
        runUpdateAssessmentErrorScenario(
                "Financial Assessment id is required",
                UpdateFinancialAssessment.builder().id(assessmentId).repId(1).initialAscrId(1).cmuId(1).build());
    }

    @Test
    public void givenAnAssessmentWithAnIdThatDoesNotExist_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer assessmentId = 999;
        runUpdateAssessmentErrorScenario(
                String.format("%d is invalid", assessmentId),
                UpdateFinancialAssessment.builder().id(assessmentId).repId(1).initialAscrId(1).cmuId(1).build());
    }

    @Test
    public void givenAnAssessmentWithNoUserModifiedSet_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdateAssessmentErrorScenario(
                "Username is required",
                UpdateFinancialAssessment.builder().id(existingAssessmentEntities.get(0).getId()).repId(1).initialAscrId(1).cmuId(1).build());
    }

    @Test
    public void givenAValidAssessmentBody_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        FinancialAssessmentEntity assessmentToUpdate =
                existingAssessmentEntities.stream().filter(item -> item.getRepId().equals(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS)).findFirst().orElse(null);

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

        FinancialAssessmentEntity updatedAssessment = financialAssessmentRepository.findById(assessmentToUpdate.getId()).orElse(null);

        assertThat(assessmentToUpdate.getUpdated()).isNotEqualTo(Objects.requireNonNull(updatedAssessment).getUpdated());
        expectedResponse.setUpdated(updatedAssessment.getUpdated());

        SoftAssertions.assertSoftly(softly -> {
            assertThat(updatedAssessment).isNotNull();
            assertChildWeightingsEqual(expectedResponse.getChildWeightings(), updatedAssessment.getChildWeightings());
            assertFinancialAssessmentDetailsEqual(expectedResponse.getAssessmentDetails(), updatedAssessment.getAssessmentDetails());
            assertFinancialAssessmentEqual(expectedResponse, updatedAssessment);
        });

        assertThat(objectMapper.writeValueAsString(expectedResponse)).isEqualTo(result.getResponse().getContentAsString());
    }

    @Test
    @Ignore("This test will fail until LCAM-85 is addressed.")
    public void givenAValidAssessmentIdWithoutAFullAssessmentAvailable_whenCreateAssessmentHistoryIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreateAssessmentHistoryScenario(false);
    }

    @Test
    @Ignore("This test will fail until LCAM-85 is addressed.")
    public void givenAValidAssessmentIdWithAFullAssessmentAvailable_whenCreateAssessmentHistoryIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreateAssessmentHistoryScenario(true);
    }

    private void runCreateAssessmentHistoryScenario(Boolean fullAvailable) throws Exception {
        FinancialAssessmentEntity assessmentEntity =
                existingAssessmentEntities.stream().filter(item -> item.getRepId().equals(existingRepOrder.getId())).findFirst().orElse(null);

        String CREATE_ASSESSMENT_HISTORY_URL = BASE_URL + "history/{financialAssessmentId}/fullAvailable/{fullAvailable}";
        runSuccessScenario(post(CREATE_ASSESSMENT_HISTORY_URL, Objects.requireNonNull(assessmentEntity).getId(), fullAvailable));
        assertAssessmentHistoryCreated(assessmentEntity, fullAvailable, existingRepOrder);
    }

    public void assertAssessmentHistoryCreated(
            FinancialAssessmentEntity assessmentEntity, Boolean fullAvailable, RepOrderEntity existingRepOrder) {

        var createdHistoryEntities = financialAssessmentsHistoryRepository.findAll();
        assertThat(createdHistoryEntities.size()).isEqualTo(1);
        var createdHistory = createdHistoryEntities.get(0);

        assertThat(createdHistory.getFinancialAssessment().getId()).isEqualTo(assessmentEntity.getId());
        assertThat(createdHistory.getFullAvailable()).isEqualTo(fullAvailable ? "Y" : "N");
        assertThat(createdHistory.getRepId()).isEqualTo(assessmentEntity.getRepId());
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
        assertThat(createdHistory.getRderCode()).isEqualTo(existingRepOrder.getRderCode());
        assertThat(createdHistory.getCcRepDec()).isEqualTo(existingRepOrder.getCcRepDec());
        assertThat(createdHistory.getCcRepType()).isEqualTo(existingRepOrder.getCcRepType());
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

    private void runCreateAssessmentErrorScenario(String errorMessage, CreateFinancialAssessment body) throws Exception {
        runBadRequestErrorScenario(
                errorMessage,
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }

    private void runUpdateAssessmentErrorScenario(String errorMessage, UpdateFinancialAssessment body) throws Exception {
        runBadRequestErrorScenario(
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
        assertThat(expectedAssessment.getRepId()).isEqualTo(actualAssessment.getRepId());
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

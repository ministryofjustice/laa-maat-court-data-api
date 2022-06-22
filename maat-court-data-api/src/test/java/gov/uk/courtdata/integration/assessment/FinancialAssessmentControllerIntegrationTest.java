package gov.uk.courtdata.integration.assessment;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.assessment.impl.FinancialAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.assessment.ChildWeightings;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.FinancialAssessmentDetails;
import gov.uk.courtdata.model.authorization.AuthorizationResponse;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import org.apache.tomcat.jni.Local;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    private List<FinancialAssessmentEntity> existingAssessmentEntities;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        setupTestData();
    }

    private void setupTestData() {
        financialAssessmentRepository.deleteAll();
        hardshipReviewRepository.deleteAll();
        passportAssessmentRepository.deleteAll();
        newWorkReasonRepository.deleteAll();
        childWeightingsRepository.deleteAll();
        financialAssessmentDetailsRepository.deleteAll();

        LocalDateTime testCreationDate = LocalDateTime.of(2022, 1, 1, 12, 0);
        String testUser = "test-user";

        NewWorkReasonEntity newWorkReasonEntity = newWorkReasonRepository.save(
                NewWorkReasonEntity.builder()
                        .code("FMA")
                        .type("ASS")
                        .description("")
                        .dateCreated(testCreationDate)
                        .userCreated(testUser)
                        .build());

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
                        .build()
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

        FinancialAssessmentDTO expectedResponse = assessmentMapper.CreateFinancialAssessmentToFinancialAssessmentDTO(body);
        expectedResponse.setAssessmentType("INIT");

        MvcResult result =
                runSuccessScenario(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));

        List<FinancialAssessmentEntity> matchingAssessments =
                financialAssessmentRepository.findAll()
                        .stream()
                        .filter(assessment -> assessment.getRepId().equals(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS))
                        .collect(Collectors.toList());

        FinancialAssessmentEntity createdAssessment =
                matchingAssessments.stream().filter(assessment -> assessment.getReplaced().equals("N")).collect(Collectors.toList()).get(0);

        List<ChildWeightingsEntity> childWeightings =
                childWeightingsRepository.findAll().stream()
                        .filter(item -> item.getFinancialAssessment().getRepId().equals(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS))
                        .collect(Collectors.toList());
        List<FinancialAssessmentDetailEntity> assessmentDetails =
                financialAssessmentDetailsRepository.findAllByFinancialAssessmentId(createdAssessment.getId());

        SoftAssertions.assertSoftly(softly -> {
            assertThat(matchingAssessments.size()).isEqualTo(2);
            assertThat((int) matchingAssessments.stream().filter(assessment -> assessment.getReplaced().equals("Y")).count())
                    .isEqualTo(1);
            assertChildWeightingsEqual(expectedResponse.getChildWeightings(), childWeightings);
            assertFinancialAssessmentDetailsEqual(expectedResponse.getAssessmentDetails(), assessmentDetails);
            assertFinancialAssessmentEqual(expectedResponse, createdAssessment);
        });

        expectedResponse.setId(createdAssessment.getId());
        expectedResponse.setDateCreated(createdAssessment.getDateCreated());
        expectedResponse.setUpdated(createdAssessment.getUpdated());
        assertThat(objectMapper.writeValueAsString(expectedResponse)).isEqualTo(result.getResponse().getContentAsString());
    }

    private void runCreateAssessmentErrorScenario(String errorMessage, CreateFinancialAssessment body) throws Exception {
        runBadRequestErrorScenario(
                errorMessage,
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
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

    private void assertFinancialAssessmentEqual(FinancialAssessmentDTO expectedAssessment, FinancialAssessmentEntity createdAssessment) {
        assertThat(expectedAssessment.getRepId()).isEqualTo(createdAssessment.getRepId());
        assertThat(expectedAssessment.getInitialAscrId()).isEqualTo(createdAssessment.getInitialAscrId());
        assertThat(expectedAssessment.getNewWorkReason().getCode()).isEqualTo(createdAssessment.getNewWorkReason().getCode());
        assertThat(expectedAssessment.getUserCreated()).isEqualTo(createdAssessment.getUserCreated());
        assertThat(expectedAssessment.getFassInitStatus()).isEqualTo(createdAssessment.getFassInitStatus());
        assertThat(expectedAssessment.getInitialAssessmentDate()).isEqualTo(createdAssessment.getInitialAssessmentDate());
        assertThat(expectedAssessment.getInitTotAggregatedIncome()).isEqualByComparingTo(createdAssessment.getInitTotAggregatedIncome().toString());
        assertThat(expectedAssessment.getInitAdjustedIncomeValue()).isEqualByComparingTo(createdAssessment.getInitAdjustedIncomeValue().toString());
        assertThat(expectedAssessment.getCmuId()).isEqualTo(createdAssessment.getCmuId());
        assertThat(expectedAssessment.getAssessmentType()).isEqualTo(createdAssessment.getAssessmentType());
        assertThat(expectedAssessment.getInitResult()).isEqualTo(createdAssessment.getInitResult());
        assertThat(expectedAssessment.getInitApplicationEmploymentStatus()).isEqualTo(createdAssessment.getInitApplicationEmploymentStatus());
    }


}

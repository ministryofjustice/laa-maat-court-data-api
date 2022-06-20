package gov.uk.courtdata.integration.assessment;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.assessment.mapper.FinancialAssessmentMapper;
import gov.uk.courtdata.dto.FinancialAssessmentDTO;
import gov.uk.courtdata.entity.*;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.authorization.AuthorizationResponse;
import gov.uk.courtdata.repository.*;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import org.apache.tomcat.jni.Local;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class, NewWorkReasonRepository.class})
public class FinancialAssessmentControllerIntegrationTest extends MockMvcIntegrationTest {

    private final String BASE_URL = "/api/internal/v1/assessment/financial-assessments/";
    private final String ASSESSMENT_URL = BASE_URL + "{financialAssessmentId}";
    private final String CHECK_OUTSTANDING_URL = BASE_URL + "/check-outstanding/{repId}";

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

    private FinancialAssessmentEntity existingAssessmentEntity;

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

        existingAssessmentEntity = financialAssessmentRepository.save(
                FinancialAssessmentEntity.builder()
                        .repId(1234)
                        .dateCreated(testCreationDate)
                        .userCreated(testUser)
                        .initialAscrId(1)
                        .usn(2)
                        .cmuId(3)
                        .replaced("N")
                        .newWorkReason(newWorkReasonEntity)
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
        runSuccessScenario(
                assessmentMapper.FinancialAssessmentEntityToFinancialAssessmentDTO(existingAssessmentEntity),
                get(ASSESSMENT_URL, existingAssessmentEntity.getId()));
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
        runSuccessScenario(delete(ASSESSMENT_URL, existingAssessmentEntity.getId()));
    }
}

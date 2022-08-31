package gov.uk.courtdata.integration.assessment;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.NewWorkReasonEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.integration.MockNewWorkReasonRepository;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class, MockNewWorkReasonRepository.class})
public class PassportAssessmentControllerIntegrationTest extends MockMvcIntegrationTest {

    private final String BASE_URL = "/api/internal/v1/assessment/passport-assessments/";
    private final String ASSESSMENT_URL = BASE_URL + "{passportAssessmentId}";
    private final Integer REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS = 1111;
    private final String ASSESSMENT_BY_REP_ID_URL = BASE_URL + "repId/{repId}";
    private final Integer INVALID_ASSESSMENT_ID = 999;

    @Autowired
    private FinancialAssessmentRepository financialAssessmentRepository;
    @Autowired
    private HardshipReviewRepository hardshipReviewRepository;
    @Autowired
    private PassportAssessmentRepository passportAssessmentRepository;
    @Autowired
    private RepOrderRepository repOrderRepository;
    @Autowired
    private MockNewWorkReasonRepository newWorkReasonRepository;
    @Autowired
    private PassportAssessmentMapper passportAssessmentMapper;

    private PassportAssessmentEntity existingPassportAssessmentEntity;
    private FinancialAssessmentEntity existingFinancialAssessmentEntity;
    private PassportAssessmentEntity completePassportAssessmentEntity;

    @BeforeEach
    public void setUp() throws Exception {
        setupTestData();
    }

    private void setupTestData() {
        financialAssessmentRepository.deleteAll();
        hardshipReviewRepository.deleteAll();
        passportAssessmentRepository.deleteAll();
        newWorkReasonRepository.deleteAll();
        repOrderRepository.deleteAll();

        LocalDateTime testCreationDate = LocalDateTime.of(2022, 1, 1, 12, 0);
        String testUser = "test-f";

        repOrderRepository.save(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS));

        NewWorkReasonEntity existingNewWorkReason = newWorkReasonRepository.save(TestEntityDataBuilder.getFmaNewWorkReasonEntity());

        existingPassportAssessmentEntity = passportAssessmentRepository.save(
                PassportAssessmentEntity.builder()
                        .repId(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS)
                        .assessmentDate(testCreationDate)
                        .userCreated(testUser)
                        .pastStatus("IN PROGRESS")
                        .replaced("N")
                        .build());

        completePassportAssessmentEntity = passportAssessmentRepository.save(
                PassportAssessmentEntity.builder()
                        .repId(2222)
                        .assessmentDate(testCreationDate)
                        .userCreated(testUser)
                        .replaced("N")
                        .pastStatus("COMPLETE")
                        .build());

        FinancialAssessmentEntity testFinancialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        testFinancialAssessment.getRepOrder().setId(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS);

        existingFinancialAssessmentEntity = financialAssessmentRepository.save(testFinancialAssessment);

        HardshipReviewEntity hardshipReview = TestEntityDataBuilder.getHardshipReviewEntity();
        hardshipReview.setId(null);
        hardshipReview.setRepId(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS);
        hardshipReview.setReplaced("N");
        hardshipReview.setNewWorkReason(existingNewWorkReason);

        hardshipReviewRepository.save(hardshipReview);
    }

    @Test
    public void givenAZeroAssessmentId_whenGetAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runBadRequestErrorScenario("Passport Assessment Id is required", get(ASSESSMENT_URL, 0));
    }

    @Test
    public void givenAnInvalidAssessmentId_whenGetAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runBadRequestErrorScenario(String.format("%d is invalid", INVALID_ASSESSMENT_ID), get(ASSESSMENT_URL, INVALID_ASSESSMENT_ID));
    }

    @Test
    public void givenAValidAssessmentId_whenGetAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runSuccessScenario(
                passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(existingPassportAssessmentEntity),
                get(ASSESSMENT_URL, existingPassportAssessmentEntity.getId()));
    }

    @Test
    public void givenAnInvalidRepId_whenGetAssessmentByRepIdIsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer invalidRepId = 0;
        runNotFoundErrorScenario(
                String.format("No Passport Assessment found for REP ID: %s", invalidRepId),
                get(ASSESSMENT_BY_REP_ID_URL, invalidRepId));
    }

    @Test
    public void givenAValidRepId_whenGetAssessmentByRepIdIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runSuccessScenario(
                passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(existingPassportAssessmentEntity),
                get(ASSESSMENT_BY_REP_ID_URL, existingPassportAssessmentEntity.getRepId()));
    }

    @Test
    public void givenAMissingRepId_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreatePassportAssessmentErrorScenario("Rep Order ID is required", CreatePassportAssessment.builder().build());
    }

    @Test
    public void givenAMissingCmuId_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreatePassportAssessmentErrorScenario(
                "Case Management Unit (CMU) ID is required",
                CreatePassportAssessment.builder().repId(1).build());
    }

    @Test
    public void givenAMissingNewWorkReasonCode_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreatePassportAssessmentErrorScenario(
                "New Work Reason (NWOR) code is required",
                CreatePassportAssessment.builder().repId(1).cmuId(2).build());
    }

    @Test
    public void givenAMissingPastStatus_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreatePassportAssessmentErrorScenario(
                "Past Status is required",
                CreatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").build());
    }

    @Test
    public void givenAMissingUserCreated_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreatePassportAssessmentErrorScenario(
                "Username is required",
                CreatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").pastStatus("test").build());
    }

    @Test
    public void givenAMissingFinancialAssessmentId_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreatePassportAssessmentErrorScenario(
                "Financial Assessment ID is required",
                CreatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").pastStatus("test").userCreated("test").build());
    }

    @Test
    public void givenAValidPassportAssessmentBody_whenCreateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepId();
        CreatePassportAssessment body = TestModelDataBuilder.getCreatePassportAssessment();
        body.setRepId(repId);
        body.setFinancialAssessmentId(existingFinancialAssessmentEntity.getId());

        PassportAssessmentDTO expectedResponse = TestModelDataBuilder.getPassportAssessmentDTO();
        expectedResponse.setRepId(repId);
        expectedResponse.setUserModified(null);
        expectedResponse.setFinancialAssessmentId(existingFinancialAssessmentEntity.getId());

        MvcResult result =
                runSuccessScenario(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));

        // Check existing financial assessment are marked as replaced.
        long updatedFinancialAssessmentsCount =
                financialAssessmentRepository.findAll()
                        .stream()
                        .filter(assessment -> assessment.getRepOrder().getId().equals(repId) && assessment.getReplaced().equals("Y"))
                        .count();

        assertThat(updatedFinancialAssessmentsCount).isEqualTo(1L);

        // Check that existing hardship reviews are marked as replaced.
        long updatedHardshipReviewCount =
                hardshipReviewRepository.findAll()
                        .stream()
                        .filter(review -> review.getRepId().equals(repId) && review.getReplaced().equals("Y"))
                        .count();

        assertThat(updatedHardshipReviewCount).isEqualTo(1L);

        // Check that there are now 2 passport assessments for the given repId.
        // One current and the other marked as replaced.
        List<PassportAssessmentEntity> matchingPassportAssessments =
                passportAssessmentRepository.findAll()
                        .stream()
                        .filter(assessment -> assessment.getRepId().equals(repId))
                        .collect(Collectors.toList());

        assertThat(matchingPassportAssessments.size()).isEqualTo(2);

        List<PassportAssessmentEntity> newPassportAssessments =
                matchingPassportAssessments
                        .stream().filter(assessment -> assessment.getReplaced().equals("N")).collect(Collectors.toList());

        assertThat(newPassportAssessments.size()).isEqualTo(1);

        PassportAssessmentEntity createdPassportAssessment = newPassportAssessments.get(0);

        expectedResponse.setId(createdPassportAssessment.getId());
        expectedResponse.setDateCreated(createdPassportAssessment.getDateCreated());
        expectedResponse.setDateModified(createdPassportAssessment.getDateModified());

        // Check the contents of the created passport assessment in the DB.
        assertPassportAssessmentsEqual(expectedResponse, createdPassportAssessment);

        // Check the contents of the returned passport assessment.
        assertThat(objectMapper.writeValueAsString(expectedResponse)).isEqualTo(result.getResponse().getContentAsString());
    }

    @Test
    public void givenAMissingRepId_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdatePassportAssessmentErrorScenario("Rep Order ID is required", UpdatePassportAssessment.builder().build());
    }

    @Test
    public void givenAMissingCmuId_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdatePassportAssessmentErrorScenario(
                "Case Management Unit (CMU) ID is required",
                UpdatePassportAssessment.builder().repId(1).build());
    }

    @Test
    public void givenAMissingNewWorkReasonCode_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdatePassportAssessmentErrorScenario(
                "New Work Reason (NWOR) code is required",
                UpdatePassportAssessment.builder().repId(1).cmuId(2).build());
    }

    @Test
    public void givenAMissingPastStatus_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdatePassportAssessmentErrorScenario(
                "Past Status is required",
                UpdatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").build());
    }

    @Test
    public void givenAMissingUserModified_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdatePassportAssessmentErrorScenario(
                "Username is required",
                UpdatePassportAssessment.builder()
                        .repId(1).cmuId(2).nworCode("FMA").pastStatus("test").id(existingPassportAssessmentEntity.getId())
                        .build());
    }

    @Test
    public void givenAMissingPassportAssessmentId_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdatePassportAssessmentErrorScenario(
                "Passport Assessment Id is required",
                UpdatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").pastStatus("test").build());
    }

    @Test
    public void givenAPassportAssessmentIdThatDoesNotExist_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdatePassportAssessmentErrorScenario(
                String.format("%d is invalid", INVALID_ASSESSMENT_ID),
                UpdatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").pastStatus("test").id(INVALID_ASSESSMENT_ID).build());
    }

    @Test
    public void givenAZeroPassportAssessmentId_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdatePassportAssessmentErrorScenario(
                "Passport Assessment Id is required",
                UpdatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").pastStatus("test").id(0).build());
    }

    @Test
    public void givenACompleteAssessmentToUpdate_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        UpdatePassportAssessment body = TestModelDataBuilder.getUpdatePassportAssessment();
        body.setRepId(completePassportAssessmentEntity.getRepId());
        body.setId(completePassportAssessmentEntity.getId());

        runUpdatePassportAssessmentErrorScenario("User cannot modify a completed assessment", body);
    }

    @Test
    @Disabled("This test will fail until LCAM-89 is fixed.")
    public void givenAValidPassportAssessmentBody_whenUpdateAssessmentIsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer id = existingPassportAssessmentEntity.getId();
        Integer repId = existingPassportAssessmentEntity.getRepId();
        UpdatePassportAssessment body = TestModelDataBuilder.getUpdatePassportAssessment();
        body.setRepId(repId);
        body.setId(id);

        PassportAssessmentDTO expectedResponse = TestModelDataBuilder.getPassportAssessmentDTO();
        expectedResponse.setId(id);
        expectedResponse.setRepId(repId);
        expectedResponse.setDateCreated(existingPassportAssessmentEntity.getDateCreated());
        expectedResponse.setUsn(null);
        expectedResponse.setValid(null);
        expectedResponse.setRtCode(null);

        MvcResult result =
                runSuccessScenario(put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
        passportAssessmentRepository.flush();
        List<PassportAssessmentEntity> matchingPassportAssessments =
                passportAssessmentRepository.findAll()
                        .stream()
                        .filter(assessment -> assessment.getRepId().equals(repId))
                        .collect(Collectors.toList());

        assertThat(matchingPassportAssessments.size()).isEqualTo(1);

        PassportAssessmentEntity updatedPassportAssessment = matchingPassportAssessments.get(0);

        expectedResponse.setDateModified(updatedPassportAssessment.getDateModified());

        // Check the contents of the created passport assessment in the DB.
        assertPassportAssessmentsEqual(expectedResponse, updatedPassportAssessment);

        // Check the contents of the returned passport assessment.
        assertThat(objectMapper.writeValueAsString(expectedResponse)).isEqualTo(result.getResponse().getContentAsString());
    }

    private void assertPassportAssessmentsEqual(PassportAssessmentDTO expectedPassportAssessment, PassportAssessmentEntity passportAssessmentEntity) {
        assertThat(passportAssessmentEntity.getId()).isEqualTo(expectedPassportAssessment.getId());
        assertThat(passportAssessmentEntity.getRepId()).isEqualTo(expectedPassportAssessment.getRepId());
        assertThat(passportAssessmentEntity.getCmuId()).isEqualTo(expectedPassportAssessment.getCmuId());
        assertThat(passportAssessmentEntity.getNworCode()).isEqualTo(expectedPassportAssessment.getNworCode());
        assertThat(passportAssessmentEntity.getPastStatus()).isEqualTo(expectedPassportAssessment.getPastStatus());
        assertThat(passportAssessmentEntity.getUserCreated()).isEqualTo(expectedPassportAssessment.getUserCreated());
        assertThat(passportAssessmentEntity.getDateCreated()).isEqualTo(expectedPassportAssessment.getDateCreated());
        assertThat(passportAssessmentEntity.getAssessmentDate()).isEqualTo(expectedPassportAssessment.getAssessmentDate());
        assertThat(passportAssessmentEntity.getDwpResult()).isEqualTo(expectedPassportAssessment.getDwpResult());
        assertThat(passportAssessmentEntity.getEsa()).isEqualTo(expectedPassportAssessment.getEsa());
        assertThat(passportAssessmentEntity.getIncomeSupport()).isEqualTo(expectedPassportAssessment.getIncomeSupport());
        assertThat(passportAssessmentEntity.getJobSeekers()).isEqualTo(expectedPassportAssessment.getJobSeekers());
        assertThat(passportAssessmentEntity.getResult()).isEqualTo(expectedPassportAssessment.getResult());
        assertThat(passportAssessmentEntity.getPartnerFirstName()).isEqualTo(expectedPassportAssessment.getPartnerFirstName());
        assertThat(passportAssessmentEntity.getPartnerSurname()).isEqualTo(expectedPassportAssessment.getPartnerSurname());
        assertThat(passportAssessmentEntity.getPartnerNiNumber()).isEqualTo(expectedPassportAssessment.getPartnerNiNumber());
        assertThat(passportAssessmentEntity.getPartnerBenefitClaimed()).isEqualTo(expectedPassportAssessment.getPartnerBenefitClaimed());
        assertThat(passportAssessmentEntity.getPartnerDob()).isEqualTo(expectedPassportAssessment.getPartnerDob());
        assertThat(passportAssessmentEntity.getStatePensionCredit()).isEqualTo(expectedPassportAssessment.getStatePensionCredit());
        assertThat(passportAssessmentEntity.getUnder16()).isEqualTo(expectedPassportAssessment.getUnder16());
        assertThat(passportAssessmentEntity.getUnder18FullEducation()).isEqualTo(expectedPassportAssessment.getUnder18FullEducation());
        assertThat(passportAssessmentEntity.getPcobConfirmation()).isEqualTo(expectedPassportAssessment.getPcobConfirmation());
        assertThat(passportAssessmentEntity.getDwpResult()).isEqualTo(expectedPassportAssessment.getDwpResult());
        assertThat(passportAssessmentEntity.getBetween16And17()).isEqualTo(expectedPassportAssessment.getBetween16And17());
        assertThat(passportAssessmentEntity.getUnder18HeardInYouthCourt()).isEqualTo(expectedPassportAssessment.getUnder18HeardInYouthCourt());
        assertThat(passportAssessmentEntity.getUnder18HeardInMagsCourt()).isEqualTo(expectedPassportAssessment.getUnder18HeardInMagsCourt());
        assertThat(passportAssessmentEntity.getLastSignOnDate()).isEqualTo(expectedPassportAssessment.getLastSignOnDate());
        assertThat(passportAssessmentEntity.getDateCompleted()).isEqualTo(expectedPassportAssessment.getDateCompleted());
        assertThat(passportAssessmentEntity.getUsn()).isEqualTo(expectedPassportAssessment.getUsn());
        assertThat(passportAssessmentEntity.getValid()).isEqualTo(expectedPassportAssessment.getValid());
        assertThat(passportAssessmentEntity.getRtCode()).isEqualTo(expectedPassportAssessment.getRtCode());
        assertThat(passportAssessmentEntity.getWhoDWPChecked()).isEqualTo(expectedPassportAssessment.getWhoDWPChecked());
    }

    private void runCreatePassportAssessmentErrorScenario(String errorMessage, CreatePassportAssessment body) throws Exception {
        runBadRequestErrorScenario(
                errorMessage,
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }

    private void runUpdatePassportAssessmentErrorScenario(String errorMessage, UpdatePassportAssessment body) throws Exception {
        runBadRequestErrorScenario(
                errorMessage,
                put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }
}

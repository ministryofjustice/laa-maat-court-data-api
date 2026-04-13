package gov.uk.courtdata.integration.assessment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.APPLICANT_AGE;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.PASS;

import com.jayway.jsonpath.JsonPath;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.NewWorkReasonEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest(classes = {MAATCourtDataApplication.class})
class PassportAssessmentControllerIntegrationTest extends MockMvcIntegrationTest {

    private final String BASE_URL = "/api/internal/v1/assessment/passport-assessments";
    private final String BASE_V2_URL = "/api/internal/v2/assessment/passport-assessments";
    private final String ASSESSMENT_URL = BASE_URL + "/{passportAssessmentId}";
    private final String ASSESSMENT_BY_REP_ID_URL = BASE_URL + "/repId/{repId}";
    private final Integer INVALID_ASSESSMENT_ID = 999;

    @Autowired
    private PassportAssessmentMapper passportAssessmentMapper;

    @MockitoSpyBean
    private gov.uk.courtdata.passport.mapper.PassportAssessmentMapper passportMapperV2;

    private PassportAssessmentEntity existingPassportAssessmentEntity;
    private FinancialAssessmentEntity existingFinancialAssessmentEntity;
    private PassportAssessmentEntity completePassportAssessmentEntity;

    @BeforeEach
    void setUp() throws Exception {
        setupTestData();
    }

    private void setupTestData() {
        LocalDateTime testCreationDate = LocalDateTime.of(2022, 1, 1, 12, 0);
        String testUser = "test-f";

        RepOrderEntity noOutstandingRepOrder = repos.repOrder.save(
            TestEntityDataBuilder.getPopulatedRepOrder());
        Integer REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS = noOutstandingRepOrder.getId();

        RepOrderEntity completedRepOrder = repos.repOrder.save(
            TestEntityDataBuilder.getPopulatedRepOrder());
        Integer REP_ID_WITH_COMPLETED_ASSESSMENT = completedRepOrder.getId();

        NewWorkReasonEntity existingNewWorkReason =
            repos.mockNewWorkReason.save(TestEntityDataBuilder.getFmaNewWorkReasonEntity());

        existingPassportAssessmentEntity = repos.passportAssessment.save(
                PassportAssessmentEntity.builder()
                        .repOrder(noOutstandingRepOrder)
                        .result(PASS.getCode())
                        .pcobConfirmation(APPLICANT_AGE.getConfirmation())
                        .assessmentDate(testCreationDate)
                        .userCreated(testUser)
                        .pastStatus("IN PROGRESS")
                        .replaced("N")
                        .build());

        completePassportAssessmentEntity = repos.passportAssessment.save(
                PassportAssessmentEntity.builder()
                        .repOrder(completedRepOrder)
                        .assessmentDate(testCreationDate)
                        .result(PASS.getCode())
                        .pcobConfirmation(APPLICANT_AGE.getConfirmation())
                        .userCreated(testUser)
                        .replaced("N")
                        .pastStatus("COMPLETE")
                        .build());

        FinancialAssessmentEntity testFinancialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        testFinancialAssessment.getRepOrder().setId(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS);

        existingFinancialAssessmentEntity = repos.financialAssessment.save(testFinancialAssessment);

        HardshipReviewEntity hardshipReview = TestEntityDataBuilder.getHardshipReviewEntity();
        hardshipReview.setId(null);
        hardshipReview.setRepId(REP_ID_WITH_NO_OUTSTANDING_ASSESSMENTS);
        hardshipReview.setReplaced("N");
        hardshipReview.setNewWorkReason(existingNewWorkReason);

        repos.hardshipReview.save(hardshipReview);
    }

    @Test
    void givenAZeroAssessmentId_whenGetAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario("Passport Assessment Id is required", get(ASSESSMENT_URL, 0)));
    }

    @Test
    void givenAnInvalidAssessmentId_whenGetAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runBadRequestErrorScenario(String.format("%d is invalid", INVALID_ASSESSMENT_ID), get(ASSESSMENT_URL, INVALID_ASSESSMENT_ID)));
    }

    @Test
    void givenAValidAssessmentId_whenGetAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runSuccessScenario(
                passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(existingPassportAssessmentEntity),
                get(ASSESSMENT_URL, existingPassportAssessmentEntity.getId())));
    }

    @Test
    void givenAnInvalidRepId_whenGetAssessmentByRepIdV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer invalidRepId = 0;
        assertTrue(runNotFoundErrorScenario(
                String.format("No Passport Assessment found for REP ID: %s", invalidRepId),
                get(ASSESSMENT_BY_REP_ID_URL, invalidRepId)));
    }

    @Test
    void givenAValidRepId_whenGetAssessmentByRepIdV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runSuccessScenario(
                passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(existingPassportAssessmentEntity),
                get(ASSESSMENT_BY_REP_ID_URL, existingPassportAssessmentEntity.getRepOrder().getId())));
    }

    @Test
    void givenAValidRepId_whenGetAssessmentByRepIdV2IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runSuccessScenario(
                passportMapperV2.toApiGetPassportedAssessmentResponse(existingPassportAssessmentEntity),
                get(BASE_V2_URL+"/"+existingPassportAssessmentEntity.getId(), existingPassportAssessmentEntity.getId())
        ));
    }

    @Test
    void givenAnInvalidRepId_whenGetAssessmentByRepIdV2IsInvoked_theCorrectResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_V2_URL + "/"+0)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(jsonPath("$.detail").value("No Passported Assessment found for ID: 0"));
    }

    @Test
    void givenAMissingRepId_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runCreatePassportAssessmentErrorScenario("Rep Order ID is required", CreatePassportAssessment.builder().build()));
    }

    @Test
    void givenAMissingCmuId_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runCreatePassportAssessmentErrorScenario(
                "Case Management Unit (CMU) ID is required",
                CreatePassportAssessment.builder().repId(1).build()));
    }

    @Test
    void givenAMissingNewWorkReasonCode_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runCreatePassportAssessmentErrorScenario(
                "New Work Reason (NWOR) code is required",
                CreatePassportAssessment.builder().repId(1).cmuId(2).build()));
    }

    @Test
    void givenAMissingPastStatus_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runCreatePassportAssessmentErrorScenario(
                "Past Status is required",
                CreatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").build()));
    }

    @Test
    void givenAMissingUserCreated_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runCreatePassportAssessmentErrorScenario(
                "Username is required",
                CreatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").pastStatus("test").build()));
    }

    @Test
    void givenAMissingFinancialAssessmentId_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runCreatePassportAssessmentErrorScenario(
                "Financial Assessment ID is required",
                CreatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").pastStatus("test").userCreated("test").build()));
    }

    @Test
    void givenAValidPassportAssessmentBody_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        CreatePassportAssessment body = TestModelDataBuilder.getCreatePassportAssessment();
        body.setRepId(repId);
        body.setFinancialAssessmentId(existingFinancialAssessmentEntity.getId());

        PassportAssessmentDTO expectedResponse = TestModelDataBuilder.getPassportAssessmentDTO();
        expectedResponse.setRepId(repId);
        expectedResponse.setUserModified(null);

        MvcResult result =
                runSuccessScenario(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));

        // Check existing financial assessment are marked as replaced.
        long updatedFinancialAssessmentsCount =
            repos.financialAssessment.findAll()
                        .stream()
                        .filter(assessment -> assessment.getRepOrder().getId().equals(repId) && assessment.getReplaced().equals("Y"))
                        .count();

        assertThat(updatedFinancialAssessmentsCount).isEqualTo(1L);

        // Check that existing hardship reviews are marked as replaced.
        long updatedHardshipReviewCount =
            repos.hardshipReview.findAll()
                        .stream()
                        .filter(review -> review.getRepId().equals(repId) && review.getReplaced().equals("Y"))
                        .count();

        assertThat(updatedHardshipReviewCount).isEqualTo(1L);

        // Check that there are now 2 passport assessments for the given repId.
        // One current and the other marked as replaced.
        List<PassportAssessmentEntity> matchingPassportAssessments =
            repos.passportAssessment.findAll()
                        .stream()
                        .filter(assessment -> assessment.getRepOrder().getId().equals(repId))
                        .toList();

        assertThat(matchingPassportAssessments).hasSize(2);

        List<PassportAssessmentEntity> newPassportAssessments =
                matchingPassportAssessments
                        .stream().filter(assessment -> assessment.getReplaced().equals("N")).toList();

        assertThat(newPassportAssessments).hasSize(1);

        PassportAssessmentEntity createdPassportAssessment = newPassportAssessments.get(0);

        expectedResponse.setId(createdPassportAssessment.getId());
        expectedResponse.setDateCreated(createdPassportAssessment.getDateCreated());
        expectedResponse.setDateModified(createdPassportAssessment.getDateModified());

        // Check the contents of the created passport assessment in the DB.
        assertPassportAssessmentsEqual(expectedResponse, createdPassportAssessment);

        // Check the contents of the returned passport assessment.
        assertThat(objectMapper.writeValueAsString(expectedResponse)).isEqualTo(result.getResponse().getContentAsString());
    }

    /** Truth Table for variations.
     * Covers main routes of the mapping/saving/partnerId population.
     */
    private static Stream<Arguments> createAssessmentV2Conditions() {
        return Stream.of(
                Arguments.of(true, true, true ),
                Arguments.of(true, true, false ),
                Arguments.of(true, false, true ),
                Arguments.of(true, false, false ),
                Arguments.of(false, true, true ),
                Arguments.of(false, true, false ),
                Arguments.of(false, false, true),
                Arguments.of(false, false, false)
        );
    }

    @ParameterizedTest
    @MethodSource("createAssessmentV2Conditions")
    void givenFullRequest_whenCreateAssessmentV2IsInvoked_theCorrectResponseIsReturned(boolean isUnder18, boolean hasDeclaredBenefits, boolean populatePartner) throws Exception {

        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        Integer partnerId = (populatePartner? repos.applicantRepository.save(TestEntityDataBuilder.getApplicant(TestEntityDataBuilder.APPLICANT_ID)).getId() : null);

        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, partnerId, isUnder18, hasDeclaredBenefits);

        // add watchers to allow mapper verification. Can rely on mapper tests.
        when(passportMapperV2.toPassportAssessmentEntity(any())).thenCallRealMethod();
        when(passportMapperV2.toApiCreatePassportedAssessmentResponse(any())).thenCallRealMethod();

        MvcResult result =
                runSuccessScenario(post(BASE_V2_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)));
        Integer createdId = JsonPath.parse(result.getResponse().getContentAsString()).read("$.legacyAssessmentId");

        List<PassportAssessmentEntity> passportAssessments = repos.passportAssessment.findAll().stream()
                .filter(assessment -> repId.equals(assessment.getRepOrder().getId()))
                .toList();

        // check we've set all old passported assessments to replaced.
        assertThat(passportAssessments.stream().filter(x-> "Y".equals(x.getReplaced()))
                .map(PassportAssessmentEntity::getId).toList()).doesNotContain(createdId).hasSize(passportAssessments.size()-1);
        // check the id is correct and saved.
        assertThat(passportAssessments.stream().filter(x-> "N".equals(x.getReplaced()))
                .map(PassportAssessmentEntity::getId).toList()).contains(createdId).hasSize(1);


        // check the old financial has been replaced.
        assertThat(repos.financialAssessment.findAll().stream()
                .filter(x -> x.getRepOrder().getId().equals(repId))
                .filter(x->"Y".equals(x.getReplaced()))).hasSize(1);
        assertThat(repos.financialAssessment.findAll().stream()
                .filter(x -> x.getRepOrder().getId().equals(repId))
                .filter(x->"N".equals(x.getReplaced()))).isEmpty();
        // check old hardship reviews have been replaced.
        assertThat(repos.hardshipReview.findAll().stream()
                .filter(x -> x.getRepId().equals(repId))
                .filter(x->"Y".equals(x.getReplaced()))).hasSize(1);
        assertThat(repos.hardshipReview.findAll().stream()
                .filter(x -> x.getRepId().equals(repId))
                .filter(x->"N".equals(x.getReplaced()))).isEmpty();

        // validate mapper is being called.
        verify(passportMapperV2).toPassportAssessmentEntity(any());
        verify(passportMapperV2).toApiCreatePassportedAssessmentResponse(any());
    }

    @Test
    void givenAMissingRepId_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runUpdatePassportAssessmentErrorScenario("Rep Order ID is required", UpdatePassportAssessment.builder().build()));
    }

    @Test
    void givenAMissingCmuId_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runUpdatePassportAssessmentErrorScenario(
                "Case Management Unit (CMU) ID is required",
                UpdatePassportAssessment.builder().repId(1).build()));
    }

    @Test
    void givenAMissingNewWorkReasonCode_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runUpdatePassportAssessmentErrorScenario(
                "New Work Reason (NWOR) code is required",
                UpdatePassportAssessment.builder().repId(1).cmuId(2).build()));
    }

    @Test
    void givenAMissingPastStatus_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runUpdatePassportAssessmentErrorScenario(
                "Past Status is required",
                UpdatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").build()));
    }

    @Test
    void givenAMissingUserModified_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runUpdatePassportAssessmentErrorScenario(
                "Username is required",
                UpdatePassportAssessment.builder()
                        .repId(1).cmuId(2).nworCode("FMA").pastStatus("test").id(existingPassportAssessmentEntity.getId())
                        .build()));
    }

    @Test
    void givenAMissingPassportAssessmentId_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runUpdatePassportAssessmentErrorScenario(
                "Passport Assessment Id is required",
                UpdatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").pastStatus("test").build()));
    }

    @Test
    void givenAPassportAssessmentIdThatDoesNotExist_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runUpdatePassportAssessmentErrorScenario(
                String.format("%d is invalid", INVALID_ASSESSMENT_ID),
                UpdatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").pastStatus("test").id(INVALID_ASSESSMENT_ID).build()));
    }

    @Test
    void givenAZeroPassportAssessmentId_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertTrue(runUpdatePassportAssessmentErrorScenario(
                "Passport Assessment Id is required",
                UpdatePassportAssessment.builder().repId(1).cmuId(2).nworCode("FMA").pastStatus("test").id(0).build()));
    }

    @Test
    void givenACompleteAssessmentToUpdate_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        UpdatePassportAssessment body = TestModelDataBuilder.getUpdatePassportAssessment();
        body.setRepId(completePassportAssessmentEntity.getRepOrder().getId());
        body.setId(completePassportAssessmentEntity.getId());

        assertTrue(runUpdatePassportAssessmentErrorScenario("User cannot modify a completed assessment", body));
    }

    @Test
    @Disabled("This test will fail until LCAM-89 is fixed.")
    void givenAValidPassportAssessmentBody_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer id = existingPassportAssessmentEntity.getId();
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
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
        repos.passportAssessment.flush();
        List<PassportAssessmentEntity> matchingPassportAssessments =
            repos.passportAssessment.findAll()
                        .stream()
                        .filter(assessment -> assessment.getRepOrder().getId().equals(repId))
                        .toList();

        assertThat(matchingPassportAssessments).hasSize(1);

        PassportAssessmentEntity updatedPassportAssessment = matchingPassportAssessments.get(0);

        expectedResponse.setDateModified(updatedPassportAssessment.getDateModified());

        // Check the contents of the created passport assessment in the DB.
        assertPassportAssessmentsEqual(expectedResponse, updatedPassportAssessment);

        // Check the contents of the returned passport assessment.
        assertThat(objectMapper.writeValueAsString(expectedResponse)).isEqualTo(result.getResponse().getContentAsString());
    }

    private void assertPassportAssessmentsEqual(PassportAssessmentDTO expectedPassportAssessment, PassportAssessmentEntity passportAssessmentEntity) {
        assertThat(passportAssessmentEntity.getId()).isEqualTo(expectedPassportAssessment.getId());
        assertThat(passportAssessmentEntity.getRepOrder().getId()).isEqualTo(expectedPassportAssessment.getRepId());
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

    private boolean runCreatePassportAssessmentErrorScenario(String errorMessage, CreatePassportAssessment body) throws Exception {
        return runBadRequestErrorScenario(
                errorMessage,
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }

    private boolean runUpdatePassportAssessmentErrorScenario(String errorMessage, UpdatePassportAssessment body) throws Exception {
        return runBadRequestErrorScenario(
                errorMessage,
                put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }




}

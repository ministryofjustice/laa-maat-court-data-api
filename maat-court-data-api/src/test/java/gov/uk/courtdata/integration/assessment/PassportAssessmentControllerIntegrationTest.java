package gov.uk.courtdata.integration.assessment;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.APPLICANT_ID;
import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;
import static gov.uk.courtdata.dto.application.AssessmentStatusDTO.COMPLETE;
import static gov.uk.courtdata.dto.application.AssessmentStatusDTO.INCOMPLETE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.PASS;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.APPLICANT_AGE;
import static uk.gov.justice.laa.crime.error.ProblemDetailError.VALIDATION_FAILURE;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.assessment.service.OutstandingAssessmentService;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.NewWorkReasonEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentRequest;
import uk.gov.justice.laa.crime.enums.BenefitRecipient;
import uk.gov.justice.laa.crime.enums.BenefitType;
import uk.gov.justice.laa.crime.error.ErrorExtension;
import uk.gov.justice.laa.crime.error.ErrorMessage;
import uk.gov.justice.laa.crime.util.ProblemDetailUtil;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class PassportAssessmentControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String BASE_URL = "/api/internal/v1/assessment/passport-assessments";
    private static final String BASE_V2_URL = "/api/internal/v2/assessment/passport-assessments";
    private static final String ASSESSMENT_URL = BASE_URL + "/{passportAssessmentId}";
    private static final String ASSESSMENT_BY_REP_ID_URL = BASE_URL + "/repId/{repId}";
    private static final Integer INVALID_ASSESSMENT_ID = 999;
    private static final String LEGACY_APPLICATION_ID_FIELD = "passportedAssessmentMetadata.legacyApplicationId";
    private static final String LEGACY_PARTNER_ID_FIELD = "passportedAssessment.declaredBenefit.legacyPartnerId";
    private static final String LAST_SIGN_ON_DATE_FIELD = "passportedAssessment.declaredBenefit.lastSignOnDate";

    @Autowired
    private PassportAssessmentMapper passportAssessmentMapper;

    @MockitoSpyBean
    private gov.uk.courtdata.passport.mapper.PassportAssessmentMapper passportMapperV2;

    @MockitoSpyBean
    private HardshipReviewRepository hardshipReviewRepository;

    @MockitoSpyBean
    private FinancialAssessmentRepository financialAssessmentRepository;

    @MockitoSpyBean
    private PassportAssessmentRepository passportAssessmentRepository;

    @MockitoSpyBean
    private RepOrderRepository repOrderRepository;

    private PassportAssessmentEntity existingPassportAssessmentEntity;
    private FinancialAssessmentEntity existingFinancialAssessmentEntity;
    private PassportAssessmentEntity completePassportAssessmentEntity;
    private HardshipReviewEntity hardshipReviewEntity;

    @BeforeEach
    void setUp() {
        setupTestData();
    }

    private void setupTestData() {
        LocalDateTime testCreationDate = LocalDateTime.of(2022, 1, 1, 12, 0);
        String testUser = "test-f";

        RepOrderEntity noOutstandingRepOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder());
        Integer repIdWithNoOutstandingAssessments = noOutstandingRepOrder.getId();

        RepOrderEntity completedRepOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder());

        NewWorkReasonEntity existingNewWorkReason =
                repos.mockNewWorkReason.save(TestEntityDataBuilder.getFmaNewWorkReasonEntity());

        existingPassportAssessmentEntity = repos.passportAssessment.save(PassportAssessmentEntity.builder()
                .repOrder(noOutstandingRepOrder)
                .result(PASS.getCode())
                .pcobConfirmation(APPLICANT_AGE.getConfirmation())
                .assessmentDate(testCreationDate)
                .userCreated(testUser)
                .pastStatus(COMPLETE)
                .replaced(NO)
                .build());

        completePassportAssessmentEntity = repos.passportAssessment.save(PassportAssessmentEntity.builder()
                .repOrder(completedRepOrder)
                .assessmentDate(testCreationDate)
                .result(PASS.getCode())
                .pcobConfirmation(APPLICANT_AGE.getConfirmation())
                .userCreated(testUser)
                .replaced(NO)
                .pastStatus(COMPLETE)
                .build());

        FinancialAssessmentEntity testFinancialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        testFinancialAssessment.getRepOrder().setId(repIdWithNoOutstandingAssessments);

        existingFinancialAssessmentEntity = repos.financialAssessment.save(testFinancialAssessment);

        hardshipReviewEntity = TestEntityDataBuilder.getHardshipReviewEntity();
        hardshipReviewEntity.setId(null);
        hardshipReviewEntity.setRepId(repIdWithNoOutstandingAssessments);
        hardshipReviewEntity.setReplaced(NO);
        hardshipReviewEntity.setNewWorkReason(existingNewWorkReason);
        hardshipReviewEntity.setFinancialAssessmentId(existingFinancialAssessmentEntity.getId());

        repos.hardshipReview.save(hardshipReviewEntity);
    }

    @Test
    void givenAZeroAssessmentId_whenGetAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertThat(runBadRequestErrorScenario("Passport Assessment Id is required", get(ASSESSMENT_URL, 0)))
                .isTrue();
    }

    @Test
    void givenAnInvalidAssessmentId_whenGetAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertThat(runBadRequestErrorScenario(
                        String.format("%d is invalid", INVALID_ASSESSMENT_ID),
                        get(ASSESSMENT_URL, INVALID_ASSESSMENT_ID)))
                .isTrue();
    }

    @Test
    void givenAValidAssessmentId_whenGetAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertThat(runSuccessScenario(
                        passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(
                                existingPassportAssessmentEntity),
                        get(ASSESSMENT_URL, existingPassportAssessmentEntity.getId())))
                .isTrue();
    }

    @Test
    void givenAnInvalidRepId_whenGetAssessmentByRepIdV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        Integer invalidRepId = 0;
        assertThat(runNotFoundErrorScenario(
                        String.format("No Passport Assessment found for REP ID: %s", invalidRepId),
                        get(ASSESSMENT_BY_REP_ID_URL, invalidRepId)))
                .isTrue();
    }

    @Test
    void givenAValidRepId_whenGetAssessmentByRepIdV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        assertThat(runSuccessScenario(
                        passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(
                                existingPassportAssessmentEntity),
                        get(
                                ASSESSMENT_BY_REP_ID_URL,
                                existingPassportAssessmentEntity.getRepOrder().getId())))
                .isTrue();
    }

    @Test
    void givenAValidRepId_whenGetAssessmentByRepIdV2IsInvoked_theCorrectResponseIsReturned() throws Exception {
        var repId = existingPassportAssessmentEntity.getRepOrder().getId();
        Applicant partner = TestEntityDataBuilder.getApplicant(null);
        repos.applicantRepository.save(partner);

        var link = new RepOrderApplicantLinksEntity();
        link.setRepId(repId);
        link.setPartnerApplId(partner.getId());
        link.setLinkDate(LocalDate.of(2000, 1, 1));
        repos.repOrderApplicantLinks.save(link);
        assertThat(runSuccessScenario(
                        passportMapperV2.toApiGetPassportedAssessmentResponse(
                                existingPassportAssessmentEntity, partner.getId()),
                        get(
                                BASE_V2_URL + "/" + existingPassportAssessmentEntity.getId(),
                                existingPassportAssessmentEntity.getId())))
                .isTrue();
    }

    @Test
    void givenAnInvalidRepId_whenGetAssessmentByRepIdV2IsInvoked_theCorrectResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_V2_URL + "/" + 0).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is(404))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE))
                .andExpect(jsonPath("$.detail").value("No Passported Assessment found for ID: 0"));
    }

    @Test
    void givenAMissingRepId_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreatePassportAssessmentErrorScenario(
                "Rep Order ID is required", CreatePassportAssessment.builder().build());
    }

    @Test
    void givenAMissingCmuId_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreatePassportAssessmentErrorScenario(
                "Case Management Unit (CMU) ID is required",
                CreatePassportAssessment.builder().repId(1).build());
    }

    @Test
    void givenAMissingNewWorkReasonCode_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        runCreatePassportAssessmentErrorScenario(
                "New Work Reason (NWOR) code is required",
                CreatePassportAssessment.builder().repId(1).cmuId(2).build());
    }

    @Test
    void givenAMissingPastStatus_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreatePassportAssessmentErrorScenario(
                "Past Status is required",
                CreatePassportAssessment.builder()
                        .repId(1)
                        .cmuId(2)
                        .nworCode("FMA")
                        .build());
    }

    @Test
    void givenAMissingUserCreated_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        runCreatePassportAssessmentErrorScenario(
                "Username is required",
                CreatePassportAssessment.builder()
                        .repId(1)
                        .cmuId(2)
                        .nworCode("FMA")
                        .pastStatus("test")
                        .build());
    }

    @Test
    void givenAMissingFinancialAssessmentId_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        runCreatePassportAssessmentErrorScenario(
                "Financial Assessment ID is required",
                CreatePassportAssessment.builder()
                        .repId(1)
                        .cmuId(2)
                        .nworCode("FMA")
                        .pastStatus("test")
                        .userCreated("test")
                        .build());
    }

    @Test
    void givenAValidPassportAssessmentBody_whenCreateAssessmentV1IsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        CreatePassportAssessment body = TestModelDataBuilder.getCreatePassportAssessment();
        body.setRepId(repId);
        body.setFinancialAssessmentId(existingFinancialAssessmentEntity.getId());

        PassportAssessmentDTO expectedResponse = TestModelDataBuilder.getPassportAssessmentDTO();
        expectedResponse.setRepId(repId);
        expectedResponse.setUserModified(null);

        MvcResult result = runSuccessScenario(
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));

        // Check existing financial assessment are marked as replaced.
        long updatedFinancialAssessmentsCount = repos.financialAssessment.findAll().stream()
                .filter(assessment -> assessment.getRepOrder().getId().equals(repId)
                        && assessment.getReplaced().equals(YES))
                .count();

        assertThat(updatedFinancialAssessmentsCount).isEqualTo(1L);

        // Check that existing hardship reviews are marked as replaced.
        long updatedHardshipReviewCount = repos.hardshipReview.findAll().stream()
                .filter(review ->
                        review.getRepId().equals(repId) && review.getReplaced().equals(YES))
                .count();

        assertThat(updatedHardshipReviewCount).isEqualTo(1L);

        // Check that there are now 2 passport assessments for the given repId.
        // One current and the other marked as replaced.
        List<PassportAssessmentEntity> matchingPassportAssessments = repos.passportAssessment.findAll().stream()
                .filter(assessment -> assessment.getRepOrder().getId().equals(repId))
                .toList();

        assertThat(matchingPassportAssessments).hasSize(2);

        List<PassportAssessmentEntity> newPassportAssessments = matchingPassportAssessments.stream()
                .filter(assessment -> assessment.getReplaced().equals(NO))
                .toList();

        assertThat(newPassportAssessments).hasSize(1);

        PassportAssessmentEntity createdPassportAssessment = newPassportAssessments.getFirst();

        expectedResponse.setId(createdPassportAssessment.getId());
        expectedResponse.setDateCreated(createdPassportAssessment.getDateCreated());
        expectedResponse.setDateModified(createdPassportAssessment.getDateModified());

        // Check the contents of the created passport assessment in the DB.
        assertPassportAssessmentsEqual(expectedResponse, createdPassportAssessment);

        // Check the contents of the returned passport assessment.
        assertThat(objectMapper.writeValueAsString(expectedResponse))
                .isEqualTo(result.getResponse().getContentAsString());
    }

    @Test
    void givenAValidRequestButHasInProgressPassport_whenCreateAssessmentV1IsInvoked_thenCorrect400ErrorReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        CreatePassportAssessment body = TestModelDataBuilder.getCreatePassportAssessment();
        body.setRepId(repId);
        body.setFinancialAssessmentId(existingFinancialAssessmentEntity.getId());
        existingPassportAssessmentEntity.setPastStatus(INCOMPLETE);
        repos.passportAssessment.save(existingPassportAssessmentEntity);

        runCreatePassportAssessmentErrorScenario(
                OutstandingAssessmentService.MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND, body);
    }

    @Test
    void
            givenAValidRequestButHasInitialInProgressFinancial_whenCreateAssessmentV1IsInvoked_thenCorrect400ErrorReturned()
                    throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        CreatePassportAssessment body = TestModelDataBuilder.getCreatePassportAssessment();
        body.setRepId(repId);
        body.setFinancialAssessmentId(existingFinancialAssessmentEntity.getId());
        existingFinancialAssessmentEntity.setFassInitStatus(INCOMPLETE);
        repos.financialAssessment.save(existingFinancialAssessmentEntity);

        runCreatePassportAssessmentErrorScenario(
                OutstandingAssessmentService.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND, body);
    }

    @Test
    void givenAValidRequestButHasFullInProgressFinancial_whenCreateAssessmentV1IsInvoked_thenCorrect400ErrorReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        CreatePassportAssessment body = TestModelDataBuilder.getCreatePassportAssessment();
        body.setRepId(repId);
        body.setFinancialAssessmentId(existingFinancialAssessmentEntity.getId());
        existingFinancialAssessmentEntity.setFassFullStatus(INCOMPLETE);
        repos.financialAssessment.save(existingFinancialAssessmentEntity);

        runCreatePassportAssessmentErrorScenario(
                OutstandingAssessmentService.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND, body);
    }

    @Test
    void givenAValidRequestButHasInProgressHardship_whenCreateAssessmentV1IsInvoked_thenCorrect400ErrorReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        CreatePassportAssessment body = TestModelDataBuilder.getCreatePassportAssessment();
        body.setRepId(repId);
        body.setFinancialAssessmentId(existingFinancialAssessmentEntity.getId());
        hardshipReviewEntity.setStatus(INCOMPLETE);
        repos.hardshipReview.save(hardshipReviewEntity);

        runCreatePassportAssessmentErrorScenario(
                OutstandingAssessmentService.MSG_OUTSTANDING_HARDSHIP_ASSESSMENT_FOUND, body);
    }

    /**
     * Simple 3 boolean truth table
     */
    private static Stream<Arguments> threeBooleanTruthTable() {
        return Stream.of(
                Arguments.of(true, true, true),
                Arguments.of(true, true, false),
                Arguments.of(true, false, true),
                Arguments.of(true, false, false),
                Arguments.of(false, true, true),
                Arguments.of(false, true, false),
                Arguments.of(false, false, true),
                Arguments.of(false, false, false));
    }

    /**
     * Test to verify behaviour of the CreatePassportedAssessment V2 endpoint. Flags are set in order to test different
     * logical paths. The specific testing of values is in the PassportMapper unit tests, but are here for completeness.
     *
     * @param isUnder18           boolean controlling if the request being made should be flagged as under-18.
     * @param hasDeclaredBenefits boolean controlling if the request being made should have a DeclaredBenefit section.
     * @param populatePartner     boolean controlling if the request being made should have a partner applicant set.
     */
    @ParameterizedTest
    @MethodSource("threeBooleanTruthTable")
    void givenFullRequest_whenCreateAssessmentV2IsInvoked_theCorrectResponseIsReturned(
            boolean isUnder18, boolean hasDeclaredBenefits, boolean populatePartner) throws Exception {

        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        Integer partnerId = null;
        if (populatePartner) {
            partnerId = repos.applicantRepository
                    .save(TestEntityDataBuilder.getApplicant(APPLICANT_ID))
                    .getId();
            repos.repOrderApplicantLinks.save(
                    TestEntityDataBuilder.getRepOrderApplicantLinksEntity(repId, partnerId, null));
        }

        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                repId, partnerId, isUnder18, hasDeclaredBenefits);

        // add watchers to allow mapper verification. Can rely on mapper tests.
        when(passportMapperV2.toPassportAssessmentEntity(any())).thenCallRealMethod();
        when(passportMapperV2.toApiCreatePassportedAssessmentResponse(any())).thenCallRealMethod();

        MvcResult result = runSuccessScenario(post(BASE_V2_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        Integer createdPassportedAssessmentId =
                JsonPath.parse(result.getResponse().getContentAsString()).read("$.legacyAssessmentId");

        List<PassportAssessmentEntity> passportAssessments = repos.passportAssessment.findAll().stream()
                .filter(a -> repId.equals(a.getRepOrder().getId()))
                .toList();
        List<HardshipReviewEntity> hardshipReviews = repos.hardshipReview.findAll().stream()
                .filter(hr -> hr.getRepId().equals(repId))
                .toList();
        List<FinancialAssessmentEntity> financialAssessments = repos.financialAssessment.findAll().stream()
                .filter(fa -> fa.getRepOrder().getId().equals(repId))
                .toList();

        // check we've set all old passported assessments to replaced.
        assertThat(passportAssessments.stream()
                        .filter(a -> YES.equals(a.getReplaced()))
                        .map(PassportAssessmentEntity::getId)
                        .toList())
                .hasSize(1)
                .doesNotContain(createdPassportedAssessmentId);
        // check the id is correct and saved and not replaced.
        assertThat(passportAssessments.stream()
                        .filter(x -> NO.equals(x.getReplaced()))
                        .map(PassportAssessmentEntity::getId)
                        .toList())
                .hasSize(1)
                .contains(createdPassportedAssessmentId);

        // check the old financial has been replaced.
        assertThat(financialAssessments.stream().filter(x -> NO.equals(x.getReplaced())))
                .isEmpty();
        assertThat(financialAssessments.stream().filter(x -> YES.equals(x.getReplaced())))
                .hasSize(1);

        // check old hardship reviews have been replaced.
        assertThat(hardshipReviews.stream().filter(x -> NO.equals(x.getReplaced())))
                .isEmpty();
        assertThat(hardshipReviews.stream().filter(x -> YES.equals(x.getReplaced())))
                .hasSize(1);

        // check assessment completed date has been set on the RepOrder.
        Optional<RepOrderEntity> repOrder = repos.repOrder.findById(repId);
        assertThat(repOrder).isPresent();

        // validate mapper is being called.
        verify(passportMapperV2).toPassportAssessmentEntity(any());
        verify(passportMapperV2).toApiCreatePassportedAssessmentResponse(any());
    }

    @Test
    void givenAValidRequestButHasInProgressPassport_whenCreateAssessmentV2IsInvoked_thenCorrect400ErrorReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        var request =
                TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, null, false, true);
        existingPassportAssessmentEntity.setPastStatus(INCOMPLETE);
        repos.passportAssessment.save(existingPassportAssessmentEntity);
        ErrorMessage expectedErrorMessage =
                new ErrorMessage("", OutstandingAssessmentService.MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND);

        runBadRequestScenarioForCreateV2WithExpectedError(request, expectedErrorMessage);
    }

    @Test
    void
            givenAValidRequestButHasInitialInProgressFinancial_whenCreateAssessmentV2IsInvoked_thenCorrect400ErrorReturned()
                    throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        var request =
                TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, null, false, true);

        existingFinancialAssessmentEntity.setFassInitStatus(INCOMPLETE);
        repos.financialAssessment.save(existingFinancialAssessmentEntity);

        ErrorMessage expectedErrorMessage =
                new ErrorMessage("", OutstandingAssessmentService.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND);

        runBadRequestScenarioForCreateV2WithExpectedError(request, expectedErrorMessage);
    }

    @Test
    void givenAValidRequestButHasFullInProgressFinancial_whenCreateAssessmentV2IsInvoked_thenCorrect400ErrorReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        var request =
                TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, null, false, true);

        existingFinancialAssessmentEntity.setFassFullStatus(INCOMPLETE);
        repos.financialAssessment.save(existingFinancialAssessmentEntity);
        ErrorMessage expectedErrorMessage =
                new ErrorMessage("", OutstandingAssessmentService.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND);

        runBadRequestScenarioForCreateV2WithExpectedError(request, expectedErrorMessage);
    }

    @Test
    void givenAValidRequestButHasInProgressHardship_whenCreateAssessmentV2IsInvoked_thenCorrect400ErrorReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        var request =
                TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, null, false, true);

        hardshipReviewEntity.setStatus(INCOMPLETE);
        repos.hardshipReview.save(hardshipReviewEntity);
        ErrorMessage expectedErrorMessage =
                new ErrorMessage("", OutstandingAssessmentService.MSG_OUTSTANDING_HARDSHIP_ASSESSMENT_FOUND);

        runBadRequestScenarioForCreateV2WithExpectedError(request, expectedErrorMessage);
    }

    @Test
    void givenRepOrderInvalid_whenCreateAssessmentV2IsInvoked_theValidationResponseIsReturned() throws Exception {
        Integer repId = 0;
        var request =
                TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, null, true, true);
        var expectedErrorMessage = new ErrorMessage(LEGACY_APPLICATION_ID_FIELD, "RepOrder does not exist");

        runBadRequestScenarioForCreateV2WithExpectedError(request, expectedErrorMessage);
    }

    @Test
    void givenJobSeekersNoSignOnDate_whenCreateAssessmentV2IsInvoked_theValidationResponseIsReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        var request =
                TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, null, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setLastSignOnDate(null);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitType(BenefitType.JSA);
        var expectedErrorMessage =
                new ErrorMessage(LAST_SIGN_ON_DATE_FIELD, "last sign on date cannot be null for job seekers");

        runBadRequestScenarioForCreateV2WithExpectedError(request, expectedErrorMessage);
    }

    @Test
    void givenPartnerRecipientWithNoLinkedPartner_whenCreateAssessmentV2IsInvoked_theValidationResponseIsReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, 0, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(BenefitRecipient.PARTNER);
        var expectedErrorMessage = new ErrorMessage(LEGACY_PARTNER_ID_FIELD, "Partner is not linked to Rep Order");

        runBadRequestScenarioForCreateV2WithExpectedError(request, expectedErrorMessage);
    }

    @Test
    void givenPartnerRecipientWithOtherLinkedPartner_whenCreateAssessmentV2IsInvoked_theValidationResponseIsReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        repos.repOrderApplicantLinks.save(
                TestEntityDataBuilder.getRepOrderApplicantLinksEntity(repId, APPLICANT_ID, null));
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, 0, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(BenefitRecipient.PARTNER);
        var expectedErrorMessage = new ErrorMessage(LEGACY_PARTNER_ID_FIELD, "Partner is not linked to Rep Order");

        runBadRequestScenarioForCreateV2WithExpectedError(request, expectedErrorMessage);
    }

    @Test
    void givenPartnerRecipientWithUnLinkedPartner_whenCreateAssessmentV2IsInvoked_theValidationResponseIsReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        Integer partnerId = repos.applicantRepository
                .save(TestEntityDataBuilder.getApplicant(APPLICANT_ID))
                .getId();
        repos.repOrderApplicantLinks.save(
                TestEntityDataBuilder.getRepOrderApplicantLinksEntity(repId, partnerId, LocalDate.parse("2021-10-09")));

        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                repId, partnerId, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(BenefitRecipient.PARTNER);
        var expectedErrorMessage = new ErrorMessage(LEGACY_PARTNER_ID_FIELD, "Partner is not linked to Rep Order");

        runBadRequestScenarioForCreateV2WithExpectedError(request, expectedErrorMessage);
    }

    @Test
    void givenMaatFailureOnHardshipReplacement_whenCreateAssessmentV2IsInvoked_theTransactionIsRolledBack()
            throws Exception {
        doThrow(new DataIntegrityViolationException("Test Exception"))
                .when(hardshipReviewRepository)
                .replaceAllByRepId(any());
        runAndValidateDatabaseFailureOnCreatePassportedV2();
    }

    @Test
    void givenMaatFailureOnFinancialReplacement_whenCreateAssessmentV2IsInvoked_theTransactionIsRolledBack()
            throws Exception {
        doThrow(new DataIntegrityViolationException("Test Exception"))
                .when(financialAssessmentRepository)
                .replaceAllByRepId(any());
        runAndValidateDatabaseFailureOnCreatePassportedV2();
    }

    @Test
    void givenMaatFailureOnAssessmentReplacement_whenCreateAssessmentV2IsInvoked_theTransactionIsRolledBack()
            throws Exception {
        doThrow(new DataIntegrityViolationException("Test Exception"))
                .when(passportAssessmentRepository)
                .replaceAllByRepIdExcludingPassportedAssessment(any(), any());
        runAndValidateDatabaseFailureOnCreatePassportedV2();
    }

    private void runBadRequestScenarioForCreateV2WithExpectedError(
            ApiCreatePassportedAssessmentRequest createRequest, ErrorMessage expectedErrorMessage) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_V2_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();

        ProblemDetail problemDetail =
                ProblemDetailUtil.parseProblemDetailJson(result.getResponse().getContentAsString());
        assertThat(problemDetail)
                .hasFieldOrPropertyWithValue("type", URI.create("about:blank"))
                .hasFieldOrPropertyWithValue("status", 400)
                .hasFieldOrPropertyWithValue("title", "Bad Request")
                .hasFieldOrPropertyWithValue("detail", VALIDATION_FAILURE.defaultDetail())
                .hasFieldOrPropertyWithValue("instance", URI.create(BASE_V2_URL));
        Optional<ErrorExtension> extension = ProblemDetailUtil.getErrorExtension(problemDetail);
        assertThat(extension).isPresent().get().hasFieldOrPropertyWithValue("code", VALIDATION_FAILURE.code());
        assertThat(ProblemDetailUtil.getErrorMessages(problemDetail)).hasSize(1).contains(expectedErrorMessage);
    }

    private void runAndValidateDatabaseFailureOnCreatePassportedV2() throws Exception {

        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        Integer partnerId = repos.applicantRepository
                .save(TestEntityDataBuilder.getApplicant(APPLICANT_ID))
                .getId();

        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                repId, partnerId, false, true);

        // add watchers to allow mapper verification. Can rely on mapper tests.
        when(passportMapperV2.toPassportAssessmentEntity(any())).thenCallRealMethod();
        when(passportMapperV2.toApiCreatePassportedAssessmentResponse(any())).thenCallRealMethod();

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_V2_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.detail").value("Request violates a data constraint"));

        List<PassportAssessmentEntity> passportAssessments = repos.passportAssessment.findAll().stream()
                .filter(assessment -> repId.equals(assessment.getRepOrder().getId()))
                .toList();

        // check we've set not set any old passported assessments to replaced.
        assertThat(passportAssessments.stream()
                        .filter(x -> YES.equals(x.getReplaced()))
                        .map(PassportAssessmentEntity::getId))
                .isEmpty();
        // check there is still only one value. The other should have been rolled back.
        assertThat(passportAssessments.stream()
                        .filter(x -> NO.equals(x.getReplaced()))
                        .map(PassportAssessmentEntity::getId)
                        .toList())
                .hasSize(1);

        // check the old financial has been replaced.
        assertThat(repos.financialAssessment.findAll().stream()
                        .filter(x -> x.getRepOrder().getId().equals(repId))
                        .filter(x -> YES.equals(x.getReplaced())))
                .isEmpty();
        assertThat(repos.financialAssessment.findAll().stream()
                        .filter(x -> x.getRepOrder().getId().equals(repId))
                        .filter(x -> NO.equals(x.getReplaced())))
                .hasSize(1);
        // check old hardship reviews have been replaced.
        assertThat(repos.hardshipReview.findAll().stream()
                        .filter(x -> x.getRepId().equals(repId))
                        .filter(x -> YES.equals(x.getReplaced())))
                .isEmpty();
        assertThat(repos.hardshipReview.findAll().stream()
                        .filter(x -> x.getRepId().equals(repId))
                        .filter(x -> NO.equals(x.getReplaced())))
                .hasSize(1);

        // check assessment completed date has not been set.
        Optional<RepOrderEntity> repOrder = repos.repOrder.findById(repId);
        assertThat(repOrder).isPresent().get().hasFieldOrPropertyWithValue("assessmentDateCompleted", null);

        // validate mapper is being called.
        verify(passportMapperV2).toPassportAssessmentEntity(any());
        verify(passportMapperV2, never()).toApiCreatePassportedAssessmentResponse(any());
    }

    @Test
    void givenAMissingRepId_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdatePassportAssessmentErrorScenario(
                "Rep Order ID is required", UpdatePassportAssessment.builder().build());
    }

    @Test
    void givenAMissingCmuId_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        runUpdatePassportAssessmentErrorScenario(
                "Case Management Unit (CMU) ID is required",
                UpdatePassportAssessment.builder().repId(1).build());
    }

    @Test
    void givenAMissingNewWorkReasonCode_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        runUpdatePassportAssessmentErrorScenario(
                "New Work Reason (NWOR) code is required",
                UpdatePassportAssessment.builder().repId(1).cmuId(2).build());
    }

    @Test
    void givenAMissingPastStatus_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        UpdatePassportAssessment assessment = UpdatePassportAssessment.builder()
                .repId(1)
                .cmuId(2)
                .nworCode("FMA")
                .build();

        runUpdatePassportAssessmentErrorScenario("Past Status is required", assessment);
    }

    @Test
    void givenAMissingUserModified_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned() throws Exception {
        UpdatePassportAssessment assessment = UpdatePassportAssessment.builder()
                .repId(1)
                .cmuId(2)
                .nworCode("FMA")
                .pastStatus("test")
                .id(existingPassportAssessmentEntity.getId())
                .build();

        runUpdatePassportAssessmentErrorScenario("Username is required", assessment);
    }

    @Test
    void givenAMissingPassportAssessmentId_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        UpdatePassportAssessment assessment = UpdatePassportAssessment.builder()
                .repId(1)
                .cmuId(2)
                .nworCode("FMA")
                .pastStatus("test")
                .build();

        runUpdatePassportAssessmentErrorScenario("Passport Assessment Id is required", assessment);
    }

    @Test
    void givenAPassportAssessmentIdThatDoesNotExist_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        UpdatePassportAssessment assessment = UpdatePassportAssessment.builder()
                .repId(1)
                .cmuId(2)
                .nworCode("FMA")
                .pastStatus("test")
                .id(INVALID_ASSESSMENT_ID)
                .build();

        runUpdatePassportAssessmentErrorScenario(String.format("%d is invalid", INVALID_ASSESSMENT_ID), assessment);
    }

    @Test
    void givenAZeroPassportAssessmentId_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        UpdatePassportAssessment assessment = UpdatePassportAssessment.builder()
                .repId(1)
                .cmuId(2)
                .nworCode("FMA")
                .pastStatus("test")
                .id(0)
                .build();

        runUpdatePassportAssessmentErrorScenario("Passport Assessment Id is required", assessment);
    }

    @Test
    void givenACompleteAssessmentToUpdate_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned()
            throws Exception {
        UpdatePassportAssessment body = TestModelDataBuilder.getUpdatePassportAssessment();
        body.setRepId(completePassportAssessmentEntity.getRepOrder().getId());
        body.setId(completePassportAssessmentEntity.getId());

        runUpdatePassportAssessmentErrorScenario("User cannot modify a completed assessment", body);
    }

    @Test
    @Disabled("This test will fail until LCAM-89 is fixed.")
    void givenAValidPassportAssessmentBody_whenUpdateAssessmentV1IsInvoked_theCorrectResponseIsReturned()
            throws Exception {
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

        MvcResult result = runSuccessScenario(
                put(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
        repos.passportAssessment.flush();
        List<PassportAssessmentEntity> matchingPassportAssessments = repos.passportAssessment.findAll().stream()
                .filter(assessment -> assessment.getRepOrder().getId().equals(repId))
                .toList();

        assertThat(matchingPassportAssessments).hasSize(1);

        PassportAssessmentEntity updatedPassportAssessment = matchingPassportAssessments.getFirst();

        expectedResponse.setDateModified(updatedPassportAssessment.getDateModified());

        // Check the contents of the created passport assessment in the DB.
        assertPassportAssessmentsEqual(expectedResponse, updatedPassportAssessment);

        // Check the contents of the returned passport assessment.
        assertThat(objectMapper.writeValueAsString(expectedResponse))
                .isEqualTo(result.getResponse().getContentAsString());
    }

    private void assertPassportAssessmentsEqual(
            PassportAssessmentDTO expectedPassportAssessment, PassportAssessmentEntity passportAssessmentEntity) {
        assertThat(passportAssessmentEntity.getId()).isEqualTo(expectedPassportAssessment.getId());
        assertThat(passportAssessmentEntity.getRepOrder().getId()).isEqualTo(expectedPassportAssessment.getRepId());
        assertThat(passportAssessmentEntity.getCmuId()).isEqualTo(expectedPassportAssessment.getCmuId());
        assertThat(passportAssessmentEntity.getNworCode()).isEqualTo(expectedPassportAssessment.getNworCode());
        assertThat(passportAssessmentEntity.getPastStatus()).isEqualTo(expectedPassportAssessment.getPastStatus());
        assertThat(passportAssessmentEntity.getUserCreated()).isEqualTo(expectedPassportAssessment.getUserCreated());
        assertThat(passportAssessmentEntity.getDateCreated()).isEqualTo(expectedPassportAssessment.getDateCreated());
        assertThat(passportAssessmentEntity.getAssessmentDate())
                .isEqualTo(expectedPassportAssessment.getAssessmentDate());
        assertThat(passportAssessmentEntity.getDwpResult()).isEqualTo(expectedPassportAssessment.getDwpResult());
        assertThat(passportAssessmentEntity.getEsa()).isEqualTo(expectedPassportAssessment.getEsa());
        assertThat(passportAssessmentEntity.getIncomeSupport())
                .isEqualTo(expectedPassportAssessment.getIncomeSupport());
        assertThat(passportAssessmentEntity.getJobSeekers()).isEqualTo(expectedPassportAssessment.getJobSeekers());
        assertThat(passportAssessmentEntity.getResult()).isEqualTo(expectedPassportAssessment.getResult());
        assertThat(passportAssessmentEntity.getPartnerFirstName())
                .isEqualTo(expectedPassportAssessment.getPartnerFirstName());
        assertThat(passportAssessmentEntity.getPartnerSurname())
                .isEqualTo(expectedPassportAssessment.getPartnerSurname());
        assertThat(passportAssessmentEntity.getPartnerNiNumber())
                .isEqualTo(expectedPassportAssessment.getPartnerNiNumber());
        assertThat(passportAssessmentEntity.getPartnerBenefitClaimed())
                .isEqualTo(expectedPassportAssessment.getPartnerBenefitClaimed());
        assertThat(passportAssessmentEntity.getPartnerDob()).isEqualTo(expectedPassportAssessment.getPartnerDob());
        assertThat(passportAssessmentEntity.getStatePensionCredit())
                .isEqualTo(expectedPassportAssessment.getStatePensionCredit());
        assertThat(passportAssessmentEntity.getUnder16()).isEqualTo(expectedPassportAssessment.getUnder16());
        assertThat(passportAssessmentEntity.getUnder18FullEducation())
                .isEqualTo(expectedPassportAssessment.getUnder18FullEducation());
        assertThat(passportAssessmentEntity.getPcobConfirmation())
                .isEqualTo(expectedPassportAssessment.getPcobConfirmation());
        assertThat(passportAssessmentEntity.getDwpResult()).isEqualTo(expectedPassportAssessment.getDwpResult());
        assertThat(passportAssessmentEntity.getBetween16And17())
                .isEqualTo(expectedPassportAssessment.getBetween16And17());
        assertThat(passportAssessmentEntity.getUnder18HeardInYouthCourt())
                .isEqualTo(expectedPassportAssessment.getUnder18HeardInYouthCourt());
        assertThat(passportAssessmentEntity.getUnder18HeardInMagsCourt())
                .isEqualTo(expectedPassportAssessment.getUnder18HeardInMagsCourt());
        assertThat(passportAssessmentEntity.getLastSignOnDate())
                .isEqualTo(expectedPassportAssessment.getLastSignOnDate());
        assertThat(passportAssessmentEntity.getDateCompleted())
                .isEqualTo(expectedPassportAssessment.getDateCompleted());
        assertThat(passportAssessmentEntity.getUsn()).isEqualTo(expectedPassportAssessment.getUsn());
        assertThat(passportAssessmentEntity.getValid()).isEqualTo(expectedPassportAssessment.getValid());
        assertThat(passportAssessmentEntity.getRtCode()).isEqualTo(expectedPassportAssessment.getRtCode());
        assertThat(passportAssessmentEntity.getWhoDWPChecked())
                .isEqualTo(expectedPassportAssessment.getWhoDWPChecked());
    }

    private void runCreatePassportAssessmentErrorScenario(String errorMessage, CreatePassportAssessment body)
            throws Exception {
        assertThat(runBadRequestErrorScenario(
                        errorMessage,
                        post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body))))
                .isTrue();
    }

    private void runUpdatePassportAssessmentErrorScenario(String errorMessage, UpdatePassportAssessment body)
            throws Exception {
        assertThat(runBadRequestErrorScenario(
                        errorMessage,
                        put(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body))))
                .isTrue();
    }
}

package gov.uk.courtdata.integration.assessment;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.APPLICANT_ID;
import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecision.PASS;
import static uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason.APPLICANT_AGE;
import static uk.gov.justice.laa.crime.error.ProblemDetailError.VALIDATION_FAILURE;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.NewWorkReasonEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

    private static final String BASE_V2_URL = "/api/internal/v2/assessment/passport-assessments";
    private static final String LEGACY_APPLICATION_ID_FIELD = "passportedAssessmentMetadata.legacyApplicationId";
    private static final String LEGACY_PARTNER_ID_FIELD = "passportedAssessment.declaredBenefit.legacyPartnerId";
    private static final String LAST_SIGN_ON_DATE_FIELD = "passportedAssessment.declaredBenefit.lastSignOnDate";

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
                .pastStatus("IN PROGRESS")
                .replaced(NO)
                .build());

        completePassportAssessmentEntity = repos.passportAssessment.save(PassportAssessmentEntity.builder()
                .repOrder(completedRepOrder)
                .assessmentDate(testCreationDate)
                .result(PASS.getCode())
                .pcobConfirmation(APPLICANT_AGE.getConfirmation())
                .userCreated(testUser)
                .replaced(NO)
                .pastStatus("COMPLETE")
                .build());

        FinancialAssessmentEntity testFinancialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        testFinancialAssessment.getRepOrder().setId(repIdWithNoOutstandingAssessments);

        existingFinancialAssessmentEntity = repos.financialAssessment.save(testFinancialAssessment);

        HardshipReviewEntity hardshipReview = TestEntityDataBuilder.getHardshipReviewEntity();
        hardshipReview.setId(null);
        hardshipReview.setRepId(repIdWithNoOutstandingAssessments);
        hardshipReview.setReplaced(NO);
        hardshipReview.setNewWorkReason(existingNewWorkReason);
        hardshipReview.setFinancialAssessmentId(existingFinancialAssessmentEntity.getId());

        repos.hardshipReview.save(hardshipReview);
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
    void givenRepOrderInvalid_whenCreateAssessmentV2IsInvoked_theValidationResponseIsReturned() throws Exception {
        Integer repId = 0;

        var request =
                TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, null, true, true);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_V2_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
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

        var expectedErrorMessage = new ErrorMessage(LEGACY_APPLICATION_ID_FIELD, "RepOrder does not exist");
        List<ErrorMessage> errors = extension.get().errors();
        assertThat(errors).containsOnly(expectedErrorMessage);
    }

    @Test
    void givenJobSeekersNoSignOnDate_whenCreateAssessmentV2IsInvoked_theValidationResponseIsReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        var request =
                TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, null, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setLastSignOnDate(null);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitType(BenefitType.JSA);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_V2_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
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

        var expectedErrorMessage =
                new ErrorMessage(LAST_SIGN_ON_DATE_FIELD, "last sign on date cannot be null for job seekers");
        List<ErrorMessage> errors = extension.get().errors();
        assertThat(errors).containsOnly(expectedErrorMessage);
    }

    @Test
    void givenPartnerRecipientWithNoLinkedPartner_whenCreateAssessmentV2IsInvoked_theValidationResponseIsReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, 0, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(BenefitRecipient.PARTNER);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_V2_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
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

        var expectedErrorMessage = new ErrorMessage(LEGACY_PARTNER_ID_FIELD, "Partner is not linked to Rep Order");
        List<ErrorMessage> errors = extension.get().errors();
        assertThat(errors).containsOnly(expectedErrorMessage);
    }

    @Test
    void givenPartnerRecipientWithOtherLinkedPartner_whenCreateAssessmentV2IsInvoked_theValidationResponseIsReturned()
            throws Exception {
        Integer repId = existingPassportAssessmentEntity.getRepOrder().getId();
        repos.repOrderApplicantLinks.save(
                TestEntityDataBuilder.getRepOrderApplicantLinksEntity(repId, APPLICANT_ID, null));
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(repId, 0, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(BenefitRecipient.PARTNER);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_V2_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
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

        var expectedErrorMessage = new ErrorMessage(LEGACY_PARTNER_ID_FIELD, "Partner is not linked to Rep Order");
        List<ErrorMessage> errors = extension.get().errors();
        assertThat(errors).containsOnly(expectedErrorMessage);
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

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(BASE_V2_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
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

        var expectedErrorMessage = new ErrorMessage(LEGACY_PARTNER_ID_FIELD, "Partner is not linked to Rep Order");
        List<ErrorMessage> errors = extension.get().errors();
        assertThat(errors).containsOnly(expectedErrorMessage);
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
}

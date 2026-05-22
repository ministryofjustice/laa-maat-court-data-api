package gov.uk.courtdata.passport.mapper;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Named.named;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.applicant.mapper.RepOrderApplicantLinksMapper;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.applicant.service.ApplicantService;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.reporder.service.RepOrderService;
import uk.gov.justice.laa.crime.common.model.passported.ApiCreatePassportedAssessmentRequest;
import uk.gov.justice.laa.crime.common.model.passported.DeclaredBenefit;
import uk.gov.justice.laa.crime.enums.BenefitRecipient;
import uk.gov.justice.laa.crime.enums.BenefitType;
import uk.gov.justice.laa.crime.enums.PassportAssessmentDecision;
import uk.gov.justice.laa.crime.enums.PassportAssessmentDecisionReason;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PassportAssessmentMapperV2Impl.class, PassportAssessmentMapperHelper.class})
class PassportAssessmentMapperTest {

    @MockitoBean
    private RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;

    @MockitoBean
    private RepOrderApplicantLinksMapper repOrderApplicantLinksMapper;

    @MockitoBean
    private RepOrderService repOrderService;

    @MockitoBean
    private ApplicantService applicantService;

    @Autowired
    private PassportAssessmentMapperHelper passportAssessmentMapperHelper;

    @Autowired
    private PassportAssessmentMapper passportAssessmentMapper;

    private RepOrderApplicantLinksDTO getRepOrderApplicantLinksDto(
            PassportAssessmentEntity passportAssessmentEntity,
            RepOrderApplicantLinksEntity repOrderApplicantLinksEntity,
            LocalDate unlinkDate) {
        return RepOrderApplicantLinksDTO.builder()
                .repId(passportAssessmentEntity.getRepOrder().getId())
                .partnerApplId(repOrderApplicantLinksEntity.getPartnerApplId())
                .unlinkDate(unlinkDate)
                .linkDate(LocalDate.now())
                .build();
    }

    @Test
    void givenPassportAssessmentEntity_whenMapToApiGetPassportedAssessmentResponse_thenAllFieldsMapped() {
        var entity = TestEntityDataBuilder.getPassportAssessmentEntity();
        entity.setIncomeSupport(true);
        entity.setJobSeekers(null);
        entity.setEsa(null);
        entity.setStatePensionCredit(null);
        // This is how we get the partner ID
        var applicantLinksEntity = TestEntityDataBuilder.getRepOrderApplicantLinksEntity();

        RepOrderApplicantLinksDTO repOrderApplicantLinksDto =
                getRepOrderApplicantLinksDto(entity, applicantLinksEntity, null);

        when(repOrderApplicantLinksMapper.mapEntityToDTO(List.of(applicantLinksEntity)))
                .thenReturn(List.of(repOrderApplicantLinksDto));

        var response = passportAssessmentMapper.toApiGetPassportedAssessmentResponse(
                entity, applicantLinksEntity.getPartnerApplId());

        assertThat(response.getLegacyAssessmentId()).isEqualTo(entity.getId());
        assertThat(response.getAssessmentId()).isNull();
        assertThat(response.getUsn()).isEqualTo(entity.getUsn());
        assertThat(response.getCaseManagementUnitId()).isEqualTo(entity.getCmuId());
        assertThat(response.getAssessmentDate()).isEqualTo(entity.getAssessmentDate());
        assertThat(response.getAssessmentReason().getCode()).isEqualTo(entity.getNworCode());
        assertThat(response.getReviewType().getCode()).isEqualTo(entity.getRtCode());
        assertThat(response.getDeclaredUnder18()).isFalse();
        assertThat(response.getDeclaredBenefit().getBenefitType()).isEqualTo(BenefitType.INCOME_SUPPORT);
        assertThat(response.getDeclaredBenefit().getLastSignOnDate()).isEqualTo(entity.getLastSignOnDate());
        assertThat(response.getDeclaredBenefit().getBenefitRecipient()).isEqualTo(BenefitRecipient.PARTNER);
        assertThat(response.getDeclaredBenefit().getLegacyPartnerId())
                .isEqualTo(applicantLinksEntity.getPartnerApplId());
        assertThat(response.getAssessmentDecision()).isEqualTo(PassportAssessmentDecision.PASS);
        assertThat(response.getDecisionReason()).isEqualTo(PassportAssessmentDecisionReason.DWP_CHECK);
        assertThat(response.getNotes()).isEqualTo(entity.getPassportNote());
    }

    @Test
    void
            givenPassportAssessmentEntityForUnder18_whenMapToApiGetPassportedAssessmentResponse_thenDeclaredBenefitIsNotMapped() {
        var entity = TestEntityDataBuilder.getPassportAssessmentEntity();
        entity.setUnder18HeardInMagsCourt(true);

        var response = passportAssessmentMapper.toApiGetPassportedAssessmentResponse(entity, null);

        assertThat(response.getLegacyAssessmentId()).isEqualTo(entity.getId());
        assertThat(response.getDeclaredUnder18()).isTrue();
        assertThat(response.getDeclaredBenefit()).isNull();
    }

    private static Stream<Arguments> declaredUnder18TestData() {
        // heardInYouthCourt, heardInMagsCourt, expectedUnder18Declaration
        return Stream.of(
                Arguments.of(true, true, true),
                Arguments.of(true, null, true),
                Arguments.of(true, false, true),
                Arguments.of(null, true, true),
                Arguments.of(false, true, true),
                Arguments.of(null, null, false),
                Arguments.of(false, false, false));
    }

    @ParameterizedTest
    @MethodSource("declaredUnder18TestData")
    void givenHeardInYouthCourtAndHeardInMagsCourt_whenMapUnder18Called_thenUnder18IsMapped(
            Boolean heardInYouthCourt, Boolean heardInMagsCourt, Boolean expectedUnder18Declaration) {
        var entity = TestEntityDataBuilder.getPassportAssessmentEntity();
        entity.setUnder18HeardInYouthCourt(heardInYouthCourt);
        entity.setUnder18HeardInMagsCourt(heardInMagsCourt);

        Boolean under18 = passportAssessmentMapperHelper.mapUnder18(entity);

        assertThat(under18).isEqualTo(expectedUnder18Declaration);
    }

    private static Stream<Arguments> benefitTypeTestData() {
        // incomeSupport, jobSeekers, esa, pensionCredit, universalCredit, expectedBenefit
        return Stream.of(
                Arguments.of(true, null, null, null, null, BenefitType.INCOME_SUPPORT),
                Arguments.of(null, true, null, null, null, BenefitType.JSA),
                Arguments.of(null, null, true, null, null, BenefitType.ESA),
                Arguments.of(null, null, null, true, null, BenefitType.GSPC),
                Arguments.of(null, null, null, null, true, BenefitType.UC));
    }

    @ParameterizedTest
    @MethodSource("benefitTypeTestData")
    void givenABenefit_whenMapBenefitTypeCalled_thenBenefitTypeIsMapped(
            Boolean incomeSupport,
            Boolean jobSeekers,
            Boolean esa,
            Boolean pensionCredit,
            Boolean universalCredit,
            BenefitType expectedBenefit) {
        var entity = TestEntityDataBuilder.getPassportAssessmentEntity();
        entity.setIncomeSupport(incomeSupport);
        entity.setJobSeekers(jobSeekers);
        entity.setEsa(esa);
        entity.setStatePensionCredit(pensionCredit);
        entity.setUniversalCredit(universalCredit);

        BenefitType benefitType = passportAssessmentMapperHelper.mapBenefitType(entity);

        assertThat(benefitType).isEqualTo(expectedBenefit);
    }

    private static Stream<Arguments> benefitRecipientTestData() {
        // partnerBenefitClaimed, expectedRecipient
        return Stream.of(
                Arguments.of(null, BenefitRecipient.APPLICANT),
                Arguments.of(false, BenefitRecipient.APPLICANT),
                Arguments.of(true, BenefitRecipient.PARTNER));
    }

    @ParameterizedTest
    @MethodSource("benefitRecipientTestData")
    void givenPartnerBenefitClaimed_whenMapBenefitRecipientCalled_thenBenefitRecipientIsMapped(
            Boolean partnerBenefitClaimed, BenefitRecipient expectedRecipient) {
        var entity = TestEntityDataBuilder.getPassportAssessmentEntity();
        entity.setPartnerBenefitClaimed(partnerBenefitClaimed);

        BenefitRecipient benefitRecipient = passportAssessmentMapperHelper.mapBenefitRecipient(entity);

        assertThat(benefitRecipient).isEqualTo(expectedRecipient);
    }

    private static Stream<Arguments> decisionReasonsTestData() {
        return Stream.of(
                Arguments.of(
                        named("result", "PASS"),
                        named("pcobConfirmation", "AGEREL"),
                        named("expectedDecisionReason", PassportAssessmentDecisionReason.APPLICANT_AGE)),
                Arguments.of(
                        named("result", "PASS"),
                        named("pcobConfirmation", "DWP"),
                        named("expectedDecisionReason", PassportAssessmentDecisionReason.DWP_CHECK)),
                Arguments.of(
                        named("result", "PASS"),
                        named("pcobConfirmation", "DOCSUP"),
                        named("expectedDecisionReason", PassportAssessmentDecisionReason.DOCUMENTATION_SUPPLIED)),
                Arguments.of(
                        named("result", "TEMP"),
                        named("pcobConfirmation", "NOCONFPOS"),
                        named("expectedDecisionReason", PassportAssessmentDecisionReason.DWP_CHECK_UNAVAILABLE)),
                Arguments.of(
                        named("result", "TEMP"),
                        named("pcobConfirmation", "INCUSTODY"),
                        named("expectedDecisionReason", PassportAssessmentDecisionReason.IN_CUSTODY)),
                Arguments.of(
                        named("result", "FAIL CONTINUE"),
                        named("pcobConfirmation", "DWP"),
                        named("expectedDecisionReason", PassportAssessmentDecisionReason.DWP_CHECK)),
                Arguments.of(
                        named("result", "FAIL CONTINUE"),
                        named("pcobConfirmation", "AGEREL"),
                        named("expectedDecisionReason", null)));
    }

    @ParameterizedTest
    @MethodSource("decisionReasonsTestData")
    void givenAResultAndPcobConfirmation_whenMapperCalled_thenDecisionReasonIsMapped(
            String result, String pcobConfirmation, PassportAssessmentDecisionReason expectedDecisionReason) {
        var entity = TestEntityDataBuilder.getPassportAssessmentEntity();
        entity.setResult(result);
        entity.setPcobConfirmation(pcobConfirmation);

        PassportAssessmentDecisionReason reason = passportAssessmentMapperHelper.mapDecisionReason(entity);

        assertThat(reason).isEqualTo(expectedDecisionReason);
    }

    private static Stream<Arguments> assessmentDecisionTestData() {
        return Stream.of(
                Arguments.of(
                        named("result", "PASS"), named("expectedAssessmentDecision", PassportAssessmentDecision.PASS)),
                Arguments.of(
                        named("result", "TEMP"),
                        named("expectedAssessmentDecision", PassportAssessmentDecision.TEMP_PASS)),
                Arguments.of(
                        named("result", "FAIL CONTINUE"),
                        named("expectedAssessmentDecision", PassportAssessmentDecision.FAIL_BYPASS)),
                Arguments.of(
                        named("result", "FAIL"), named("expectedAssessmentDecision", PassportAssessmentDecision.FAIL)));
    }

    @ParameterizedTest
    @MethodSource("assessmentDecisionTestData")
    void givenAResult_whenMapperCalled_thenAssessmentDecisionIsMapped(
            String result, PassportAssessmentDecision expectedAssessmentDecision) {
        var entity = TestEntityDataBuilder.getPassportAssessmentEntity();
        entity.setResult(result);

        PassportAssessmentDecision assessmentDecision = passportAssessmentMapperHelper.mapAssessmentDecision(entity);

        assertThat(assessmentDecision).isEqualTo(expectedAssessmentDecision);
    }

    @Test
    void givenCreateRequestWithDeclaredBenefit_whenMapped_thenBenefitFieldsAreSetCorrectly() {
        var repOrder = TestEntityDataBuilder.getPopulatedRepOrder(REP_ID);
        Integer partnerId = TestEntityDataBuilder.APPLICANT_ID;

        ApiCreatePassportedAssessmentRequest request =
                TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                        repOrder.getId(), partnerId, false, true);

        var entity = passportAssessmentMapper.toPassportAssessmentEntity(request);

        validatePassportedAssessmentV2UnconditionalMappings(request, entity);

        BenefitType expectedBenefit =
                request.getPassportedAssessment().getDeclaredBenefit().getBenefitType();
        validateBenefitTypeMapping(expectedBenefit, entity);
        assertThat(entity.getPartnerBenefitClaimed()).isFalse();
    }

    @Test
    void givenCreateRequestWithNoDeclaredBenefit_whenMapToEntityCalled_thenEntityIsCorrectlyCreated() {
        var repOrder = TestEntityDataBuilder.getPopulatedRepOrder(REP_ID);

        ApiCreatePassportedAssessmentRequest request =
                TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                        repOrder.getId(), null, false, false);

        var entity = passportAssessmentMapper.toPassportAssessmentEntity(request);

        validatePassportedAssessmentV2UnconditionalMappings(request, entity);
        validateBenefitTypeMapping(null, entity);
        assertThat(entity.getPartnerBenefitClaimed()).isFalse();
    }

    private static Stream<Arguments> benefitMapperTestData() {
        // benefitRecipient, partnerBenefitClaimed
        return Stream.of(Arguments.of(BenefitRecipient.PARTNER, true), Arguments.of(BenefitRecipient.APPLICANT, false));
    }

    @MethodSource(value = "benefitMapperTestData")
    @ParameterizedTest
    void givenSpecificBenefitRecipient_whenMapPartnerBenefitClaimedCalled_thenPartnerBenefitIsMappedCorrectly(
            BenefitRecipient benefitRecipient, Boolean expectedOutput) {
        DeclaredBenefit declaredBenefit = TestModelDataBuilder.buildDeclaredBenefit(benefitRecipient);

        assertThat(passportAssessmentMapperHelper.mapPartnerBenefitClaimed(declaredBenefit))
                .isEqualTo(expectedOutput);
    }

    @MethodSource(value = "benefitMapperTestData")
    @ParameterizedTest
    void givenCreateRequestWithSpecificBenefitRecipient_whenMapToEntityCalled_thenPartnerBenefitIsMappedCorrectly(
            BenefitRecipient benefitRecipient, Boolean expectedOutput) {
        var repOrder = TestEntityDataBuilder.getPopulatedRepOrder(REP_ID);
        Integer partnerId = TestEntityDataBuilder.APPLICANT_ID;

        ApiCreatePassportedAssessmentRequest request =
                TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                        repOrder.getId(), partnerId, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(benefitRecipient);

        var entity = passportAssessmentMapper.toPassportAssessmentEntity(request);

        validatePassportedAssessmentV2UnconditionalMappings(request, entity);
        validateBenefitTypeMapping(
                request.getPassportedAssessment().getDeclaredBenefit().getBenefitType(), entity);

        assertThat(entity.getPartnerBenefitClaimed()).isEqualTo(expectedOutput);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenCreateRequestUnder18_whenMapToEntityCalled_thenBenefitDetailsShouldBeEmpty(boolean hasDeclaredBenefits) {
        var repOrder = TestEntityDataBuilder.getPopulatedRepOrder(REP_ID);
        Integer partnerId;
        partnerId = TestEntityDataBuilder.APPLICANT_ID;

        ApiCreatePassportedAssessmentRequest request =
                TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                        repOrder.getId(), partnerId, true, hasDeclaredBenefits);

        var entity = passportAssessmentMapper.toPassportAssessmentEntity(request);

        // general mappings should be correct.
        validatePassportedAssessmentV2UnconditionalMappings(request, entity);

        // check case specific mappings.
        validateBenefitTypeMapping(null, entity);
        assertThat(entity.getPartnerBenefitClaimed()).isFalse();
    }

    @ParameterizedTest
    @EnumSource(value = BenefitType.class)
    void givenSpecificBenefit_whenMapBenefitIsCalled_thenCorrectBenefitIsMapped(BenefitType benefitType) {
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(false);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitType(benefitType);
        Boolean result;
        // loop through all benefit types and check that it returns "Y" for the correct one, otherwise "N".
        for (BenefitType currentBenefitType : BenefitType.values()) {
            result = passportAssessmentMapper.mapBenefitType(currentBenefitType, request);
            assertThat(result).isEqualTo(isBenefitType(benefitType, currentBenefitType));
        }
    }

    @Test
    void givenJSA_whenMapperIsCalled_thenLastSignOnMapped() {
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(false);
        var signOnDateTime = LocalDateTime.now();
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitType(BenefitType.JSA);
        request.getPassportedAssessment().getDeclaredBenefit().setLastSignOnDate(signOnDateTime);

        var result = passportAssessmentMapper.toPassportAssessmentEntity(request);
        assertThat(result.getLastSignOnDate()).isEqualTo(signOnDateTime);
    }

    @ParameterizedTest
    @EnumSource(value = BenefitType.class, mode = EnumSource.Mode.EXCLUDE, names = "JSA")
    void givenNonJSA_whenMapperIsCalled_thenLastSignOnNotMapped(BenefitType benefitType) {
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(false);
        var signOnDateTime = LocalDateTime.now();
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitType(benefitType);
        request.getPassportedAssessment().getDeclaredBenefit().setLastSignOnDate(signOnDateTime);

        var result = passportAssessmentMapper.toPassportAssessmentEntity(request);
        assertThat(result.getLastSignOnDate()).isNull();
    }

    private void validatePassportedAssessmentV2UnconditionalMappings(
            ApiCreatePassportedAssessmentRequest request, PassportAssessmentEntity entity) {
        assertThat(entity.getPastStatus()).isEqualTo("COMPLETE");
        assertThat(entity.getDateCompleted()).isNotNull().isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));
        assertThat(entity.getAssessmentDate())
                .isNotNull()
                .isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));
        assertThat(entity.getNworCode())
                .isEqualTo(
                        request.getPassportedAssessment().getAssessmentReason().getCode());
        assertThat(entity.getRtCode())
                .isEqualTo(request.getPassportedAssessment().getReviewType().getCode());
        assertThat(entity.getPcobConfirmation())
                .isEqualTo(request.getPassportedAssessment().getDecisionReason().getConfirmation());
        assertThat(entity.getRepOrder().getId())
                .isEqualTo(request.getPassportedAssessmentMetadata().getLegacyApplicationId());
        assertThat(entity.getPassportNote())
                .isEqualTo(request.getPassportedAssessment().getNotes());
        assertThat(entity.getUsn())
                .isEqualTo(request.getPassportedAssessmentMetadata().getUsn());
        assertThat(entity.getCmuId())
                .isEqualTo(request.getPassportedAssessmentMetadata().getCaseManagementUnitId());
        assertThat(entity.getResult())
                .isEqualTo(request.getPassportedAssessment()
                        .getAssessmentDecision()
                        .getCode());
        assertThat(entity.getUserCreated())
                .isEqualTo(request.getPassportedAssessmentMetadata()
                        .getUserSession()
                        .getUserName());
        // TODO: LCAM-2073 - Under 18 court asserts.
        //
        // assertThat(entity.getUnder18HeardInMagsCourt()).isEqualTo(request.getPassportedAssessment().getDecisionReason().getConfirmation());
        //
        // assertThat(entity.getUnder18HeardInYouthCourt()).isEqualTo(request.getPassportedAssessment().getDecisionReason().getConfirmation());
    }

    private void validateBenefitTypeMapping(BenefitType expectedBenefit, PassportAssessmentEntity entity) {
        assertThat(entity.getIncomeSupport()).isEqualTo(isBenefitType(expectedBenefit, BenefitType.INCOME_SUPPORT));
        assertThat(entity.getJobSeekers()).isEqualTo(isBenefitType(expectedBenefit, BenefitType.JSA));
        assertThat(entity.getEsa()).isEqualTo(isBenefitType(expectedBenefit, BenefitType.ESA));
        assertThat(entity.getStatePensionCredit()).isEqualTo(isBenefitType(expectedBenefit, BenefitType.GSPC));
        assertThat(entity.getUniversalCredit()).isEqualTo(isBenefitType(expectedBenefit, BenefitType.UC));
    }

    private Boolean isBenefitType(BenefitType actualType, BenefitType expected) {
        return expected.equals(actualType) ? true : false;
    }
}

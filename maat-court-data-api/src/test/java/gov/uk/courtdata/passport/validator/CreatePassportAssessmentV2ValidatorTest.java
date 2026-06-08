package gov.uk.courtdata.passport.validator;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.APPLICANT_ID;
import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.applicant.service.PartnerResolver;
import gov.uk.courtdata.assessment.service.OutstandingAssessmentService;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.CrimeValidationException;
import gov.uk.courtdata.reporder.service.RepOrderService;
import uk.gov.justice.laa.crime.enums.BenefitRecipient;
import uk.gov.justice.laa.crime.enums.BenefitType;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreatePassportAssessmentV2ValidatorTest {

    private static final String LEGACY_APPLICATION_ID_FIELD = "passportedAssessmentMetadata.legacyApplicationId";
    private static final String LEGACY_PARTNER_ID_FIELD = "passportedAssessment.declaredBenefit.legacyPartnerId";

    @Mock
    RepOrderService repOrderService;

    @Mock
    OutstandingAssessmentService outstandingAssessmentService;

    @Mock
    PartnerResolver partnerResolver;

    @InjectMocks
    CreatePassportAssessmentV2Validator createPassportAssessmentV2Validator;

    @Test
    void givenValidRequest_whenValidateIsInvoked_thenShouldSucceed() {
        when(repOrderService.exists(REP_ID)).thenReturn(true);
        when(partnerResolver.hasLinkedPartner(REP_ID, APPLICANT_ID)).thenReturn(true);
        when(outstandingAssessmentService.checkForOutstandingAssessments(any())).thenReturn(Optional.empty());
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(BenefitRecipient.PARTNER);
        assertThatCode(() -> createPassportAssessmentV2Validator.validateCreateRequest(request))
                .doesNotThrowAnyException();
    }

    @Test
    void givenRequestWithInvalidRepOrderId_whenValidateIsInvoked_thenShouldError() {
        var expectedErrorMessage = new ErrorMessage(LEGACY_APPLICATION_ID_FIELD, "RepOrder does not exist");
        when(repOrderService.exists(REP_ID)).thenReturn(false);
        when(outstandingAssessmentService.checkForOutstandingAssessments(any())).thenReturn(Optional.empty());

        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitType(BenefitType.JSA);
        request.getPassportedAssessment().getDeclaredBenefit().setLastSignOnDate(LocalDateTime.now());

        assertThatThrownBy(() -> createPassportAssessmentV2Validator.validateCreateRequest(request))
                .isInstanceOfSatisfying(CrimeValidationException.class, e -> assertThat(e.getExceptionMessages())
                        .containsOnly(expectedErrorMessage));
    }

    @Test
    void givenRequestWithPartnerRecipientNoId_whenValidateIsInvoked_thenShouldError() {
        var expectedErrorMessage =
                new ErrorMessage(LEGACY_PARTNER_ID_FIELD, "Partner Id must be populated if partner receiving benefit");
        when(repOrderService.exists(REP_ID)).thenReturn(true);
        when(outstandingAssessmentService.checkForOutstandingAssessments(any())).thenReturn(Optional.empty());

        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(BenefitRecipient.PARTNER);
        request.getPassportedAssessment().getDeclaredBenefit().setLegacyPartnerId(null);

        assertThatThrownBy(() -> createPassportAssessmentV2Validator.validateCreateRequest(request))
                .isInstanceOfSatisfying(CrimeValidationException.class, e -> assertThat(e.getExceptionMessages())
                        .containsOnly(expectedErrorMessage));
    }

    @Test
    void givenRequestWithPartnerRecipientDoesNotExist_whenValidateIsInvoked_thenShouldError() {
        var expectedErrorMessage = new ErrorMessage(LEGACY_PARTNER_ID_FIELD, "Partner is not linked to Rep Order");
        when(repOrderService.exists(REP_ID)).thenReturn(true);
        when(partnerResolver.hasLinkedPartner(REP_ID, APPLICANT_ID)).thenReturn(false);
        when(outstandingAssessmentService.checkForOutstandingAssessments(any())).thenReturn(Optional.empty());

        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(BenefitRecipient.PARTNER);

        assertThatThrownBy(() -> createPassportAssessmentV2Validator.validateCreateRequest(request))
                .isInstanceOfSatisfying(CrimeValidationException.class, e -> assertThat(e.getExceptionMessages())
                        .containsOnly(expectedErrorMessage));
    }

    @Test
    void givenOutstandingAssessment_whenValidateIsInvoked_outstandingAssessmentErrorShouldPresent() {
        ErrorMessage expectedErrorMessage = new ErrorMessage("test", "test error should be present.");
        when(repOrderService.exists(REP_ID)).thenReturn(true);
        when(partnerResolver.hasLinkedPartner(REP_ID, APPLICANT_ID)).thenReturn(true);
        when(outstandingAssessmentService.checkForOutstandingAssessments(any()))
                .thenReturn(Optional.of(expectedErrorMessage));

        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(BenefitRecipient.PARTNER);

        assertThatThrownBy(() -> createPassportAssessmentV2Validator.validateCreateRequest(request))
                .isInstanceOfSatisfying(CrimeValidationException.class, e -> assertThat(e.getExceptionMessages())
                        .containsOnly(expectedErrorMessage));
    }
}

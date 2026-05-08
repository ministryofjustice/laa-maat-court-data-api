package gov.uk.courtdata.passport.validator;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.APPLICANT_ID;
import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.applicant.service.PartnerResolver;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.CrimeValidationException;
import gov.uk.courtdata.reporder.service.RepOrderService;
import uk.gov.justice.laa.crime.enums.BenefitRecipient;
import uk.gov.justice.laa.crime.enums.BenefitType;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.time.LocalDateTime;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreatePassportAssessmentV2ValidatorTest {

    private static final String LEGACY_APPLICATION_ID_FIELD = "passportedAssessmentMetadata.legacyApplicationId";
    private static final String LAST_SIGN_ON_DATE_FIELD = "passportedAssessment.declaredBenefit.lastSignOnDate";
    private static final String LEGACY_PARTNER_ID_FIELD = "passportedAssessment.declaredBenefit.legacyPartnerId";

    @Mock
    RepOrderService repOrderService;

    @Mock
    PartnerResolver partnerResolver;

    @InjectMocks
    CreatePassportAssessmentV2Validator createPassportAssessmentV2Validator;

    @Test
    void givenValidRequest_whenValidateIsInvoked_thenShouldSucceed() {
        when(repOrderService.exists(REP_ID)).thenReturn(true);
        when(partnerResolver.hasLinkedPartner(REP_ID, APPLICANT_ID)).thenReturn(true);
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(BenefitRecipient.PARTNER);
        assertDoesNotThrow(() -> createPassportAssessmentV2Validator.validateCreateRequest(request));
    }

    @Test
    void givenRequestWithJsaNoSignOn_whenValidateIsInvoked_thenShouldError() {
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitType(BenefitType.JSA);
        request.getPassportedAssessment().getDeclaredBenefit().setLastSignOnDate(null);
        var expectedErrorMessage =
                new ErrorMessage(LAST_SIGN_ON_DATE_FIELD, "last sign on date cannot be null for job seekers");
        when(repOrderService.exists(REP_ID)).thenReturn(true);

        CrimeValidationException e = assertThrows(
                CrimeValidationException.class,
                () -> createPassportAssessmentV2Validator.validateCreateRequest(request));

        assertThat(e.getExceptionMessages())
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsOnly(expectedErrorMessage);
    }

    @Test
    void givenRequestWithJsaWithSignOn_whenValidateIsInvoked_thenShouldError() {
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitType(BenefitType.JSA);
        request.getPassportedAssessment().getDeclaredBenefit().setLastSignOnDate(LocalDateTime.now());
        when(repOrderService.exists(REP_ID)).thenReturn(true);

        assertDoesNotThrow(() -> createPassportAssessmentV2Validator.validateCreateRequest(request));
    }

    @Test
    void givenRequestWithInvalidRepOrderId_whenValidateIsInvoked_thenShouldError() {
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitType(BenefitType.JSA);
        request.getPassportedAssessment().getDeclaredBenefit().setLastSignOnDate(LocalDateTime.now());
        var expectedErrorMessage = new ErrorMessage(LEGACY_APPLICATION_ID_FIELD, "RepOrder does not exist");
        when(repOrderService.exists(REP_ID)).thenReturn(false);

        CrimeValidationException e = assertThrows(
                CrimeValidationException.class,
                () -> createPassportAssessmentV2Validator.validateCreateRequest(request));

        assertThat(e.getExceptionMessages())
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsOnly(expectedErrorMessage);
    }

    @Test
    void givenRequestWithPartnerRecipientNoId_whenValidateIsInvoked_thenShouldError() {
        when(repOrderService.exists(REP_ID)).thenReturn(true);
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(BenefitRecipient.PARTNER);
        request.getPassportedAssessment().getDeclaredBenefit().setLegacyPartnerId(null);

        var expectedErrorMessage =
                new ErrorMessage(LEGACY_PARTNER_ID_FIELD, "Partner Id must be populated if partner receiving benefit");

        CrimeValidationException e = assertThrows(
                CrimeValidationException.class,
                () -> createPassportAssessmentV2Validator.validateCreateRequest(request));

        assertThat(e.getExceptionMessages())
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsOnly(expectedErrorMessage);
    }

    @Test
    void givenRequestWithPartnerRecipientDoesNotExist_whenValidateIsInvoked_thenShouldError() {
        when(repOrderService.exists(REP_ID)).thenReturn(true);
        when(partnerResolver.hasLinkedPartner(REP_ID, APPLICANT_ID)).thenReturn(false);
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(
                REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitRecipient(BenefitRecipient.PARTNER);

        var expectedErrorMessage = new ErrorMessage(LEGACY_PARTNER_ID_FIELD, "Partner is not linked to Rep Order");

        CrimeValidationException e = assertThrows(
                CrimeValidationException.class,
                () -> createPassportAssessmentV2Validator.validateCreateRequest(request));

        assertThat(e.getExceptionMessages())
                .asInstanceOf(InstanceOfAssertFactories.LIST)
                .containsOnly(expectedErrorMessage);
    }
}

package gov.uk.courtdata.passport.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.CrimeValidationException;
import gov.uk.courtdata.reporder.service.RepOrderService;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.enums.BenefitType;
import uk.gov.justice.laa.crime.error.ErrorMessage;


import java.time.LocalDateTime;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.APPLICANT_ID;
import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CreatePassportAssessmentV2ValidatorTest {

    @Mock
    RepOrderService repOrderService;

    @InjectMocks
    CreatePassportAssessmentV2Validator createPassportAssessmentV2Validator;

    @Test
    void givenValidRequest_whenValidateIsInvoked_thenShouldSucceed() {
        when(repOrderService.exists(REP_ID)).thenReturn(true);
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(REP_ID, APPLICANT_ID, false, true);
        assertDoesNotThrow(()
                -> createPassportAssessmentV2Validator.validateCreateRequest(request));
    }

    @Test
    void givenRequestWithJsaNoSignOn_whenValidateIsInvoked_thenShouldError() {
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitType(BenefitType.JSA);
        request.getPassportedAssessment().getDeclaredBenefit().setLastSignOnDate(null);
        var expectedErrorMessage = new ErrorMessage("lastSignOnDate","last sign on date cannot be null for job seekers");
        when(repOrderService.exists(REP_ID)).thenReturn(true);

        CrimeValidationException e = assertThrows(CrimeValidationException.class,()
                -> createPassportAssessmentV2Validator.validateCreateRequest(request));

        assertThat(e.getExceptionMessages()).asInstanceOf(InstanceOfAssertFactories.LIST).containsOnly(expectedErrorMessage);
    }

    @Test
    void givenRequestWithJsaWithSignOn_whenValidateIsInvoked_thenShouldError() {
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitType(BenefitType.JSA);
        request.getPassportedAssessment().getDeclaredBenefit().setLastSignOnDate(LocalDateTime.now());
        when(repOrderService.exists(REP_ID)).thenReturn(true);

        assertDoesNotThrow(()
                -> createPassportAssessmentV2Validator.validateCreateRequest(request));
    }

    @Test
    void givenRequestWithInvalidRepOrderId_whenValidateIsInvoked_thenShouldError(){
        var request = TestModelDataBuilder.buildValidPopulatedCreatePassportedAssessmentRequest(REP_ID, APPLICANT_ID, false, true);
        request.getPassportedAssessment().getDeclaredBenefit().setBenefitType(BenefitType.JSA);
        request.getPassportedAssessment().getDeclaredBenefit().setLastSignOnDate(LocalDateTime.now());
        var expectedErrorMessage = new ErrorMessage("legacyApplicationId","RepOrder does not exist");
        when(repOrderService.exists(REP_ID)).thenReturn(false);

        CrimeValidationException e = assertThrows(CrimeValidationException.class,()
                -> createPassportAssessmentV2Validator.validateCreateRequest(request));

        assertThat(e.getExceptionMessages()).asInstanceOf(InstanceOfAssertFactories.LIST).containsOnly(expectedErrorMessage);
    }


}
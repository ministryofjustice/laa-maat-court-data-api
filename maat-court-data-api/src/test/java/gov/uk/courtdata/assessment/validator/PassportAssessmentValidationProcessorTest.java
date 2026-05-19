package gov.uk.courtdata.assessment.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.assessment.service.OutstandingAssessmentService;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.PassportAssessment;
import gov.uk.courtdata.validator.PassportAssessmentIdValidator;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PassportAssessmentValidationProcessorTest {

    @InjectMocks
    private PassportAssessmentValidationProcessor passportAssessmentValidationProcessor;

    @Mock
    private CreatePassportAssessmentValidator createPassportAssessmentValidator;

    @Mock
    private PassportAssessmentIdValidator passportAssessmentIdValidator;

    @Mock
    private OutstandingAssessmentService outstandingAssessmentService;

    @Test
    void testPassportAssessmentValidationProcessor_whenIdIsPassed_thenCallsIdValidator() {
        when(passportAssessmentIdValidator.validate(any(Integer.class))).thenReturn(Optional.empty());
        passportAssessmentValidationProcessor.validate(1000);
        verify(passportAssessmentIdValidator).validate(1000);
    }

    @Test
    void testPassportAssessmentValidationProcessor_whenAssessmentIsNull_thenThrowsException() {
        assertThatThrownBy(() -> passportAssessmentValidationProcessor.validate((PassportAssessment) null))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Passport Assessment Request is empty");
    }

    @Test
    void testPassportAssessmentValidationProcessor_whenRepIdIsNull_thenThrowsException() {
        PassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        assessment.setRepId(null);
        assertThatThrownBy(() -> passportAssessmentValidationProcessor.validate(assessment))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Rep Order ID is required");
    }

    @Test
    void testPassportAssessmentValidationProcessor_whenCmuIdIsNull_thenThrowsException() {
        PassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        assessment.setCmuId(null);
        assertThatThrownBy(() -> passportAssessmentValidationProcessor.validate(assessment))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Case Management Unit (CMU) ID is required");
    }

    @NullAndEmptySource
    @ParameterizedTest
    void testPassportAssessmentValidationProcessor_whenNWORCodeIsBlank_thenThrowsException(String nworCode) {
        PassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        assessment.setNworCode(nworCode);
        assertThatThrownBy(() -> passportAssessmentValidationProcessor.validate(assessment))
                .isInstanceOf(ValidationException.class)
                .hasMessage("New Work Reason (NWOR) code is required");
    }

    @NullAndEmptySource
    @ParameterizedTest
    void testPassportAssessmentValidationProcessor_whenPastStatusIsEmpty_thenThrowsException(String pastStatus) {
        PassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        assessment.setPastStatus(pastStatus);
        assertThatThrownBy(() -> passportAssessmentValidationProcessor.validate(assessment))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Past Status is required");
    }

    @Test
    void testPassportAssessmentValidationProcessor_whenRequiredFieldsPresent_thenValidationPasses() {
        PassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        when(createPassportAssessmentValidator.validate(any(CreatePassportAssessment.class)))
                .thenReturn(Optional.empty());
        assertThat(passportAssessmentValidationProcessor.validate(assessment)).isNotPresent();
    }
}

package gov.uk.courtdata.integration.assessment;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.justice.laa.crime.enums.CurrentStatus.COMPLETE;
import static uk.gov.justice.laa.crime.enums.CurrentStatus.IN_PROGRESS;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.assessment.service.OutstandingAssessmentService;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import uk.gov.justice.laa.crime.enums.CurrentStatus;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class OutstandingAssessmentServiceIntegrationTest extends MockMvcIntegrationTest {

    @Autowired
    OutstandingAssessmentService outstandingAssessmentService;

    private static RepOrderEntity repOrder;

    @BeforeEach
    void setupData() {
        repOrder = TestEntityDataBuilder.getPopulatedRepOrder(null);
        repos.repOrder.save(repOrder);
        repos.mockNewWorkReason.save(TestEntityDataBuilder.getNewWorkReasonEntity());
        repos.mockNewWorkReason.save(TestEntityDataBuilder.getFmaNewWorkReasonEntity());

        // setup unrelated data.
        var existingRepOrder = repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(null));
        saveTestFullMeansAssessment(existingRepOrder, NO, YES, IN_PROGRESS);
        saveTestHardshipAssessment(existingRepOrder, NO, YES, IN_PROGRESS);
        saveTestPassportedAssessment(existingRepOrder, NO, YES, IN_PROGRESS);
    }

    // replaced / valid / status
    private static Stream<Arguments> outstandingAssessmentData() {
        return Stream.of(Arguments.of(NO, YES, IN_PROGRESS), Arguments.of(NO, null, IN_PROGRESS));
    }

    @ParameterizedTest
    @MethodSource(value = "outstandingAssessmentData")
    void givenOutstandingInitMeans_whenCheckForOutstandingAssessmentsIsCalled_thenErrorListReturned(
            String replaced, String valid, CurrentStatus status) {
        ErrorMessage expectedError =
                new ErrorMessage("", OutstandingAssessmentService.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND);
        saveTestInitMeansAssessment(repOrder, replaced, valid, status);
        List<ErrorMessage> errorList = outstandingAssessmentService.checkForOutstandingAssessments(repOrder.getId());

        assertThat(errorList).hasSize(1).containsOnly(expectedError);
    }

    @ParameterizedTest
    @MethodSource(value = "outstandingAssessmentData")
    void givenOutstandingFullMeans_whenCheckForOutstandingAssessmentsIsCalled_thenErrorListReturned(
            String replaced, String valid, CurrentStatus status) {
        ErrorMessage expectedError =
                new ErrorMessage("", OutstandingAssessmentService.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND);
        saveTestFullMeansAssessment(repOrder, replaced, valid, status);
        List<ErrorMessage> errorList = outstandingAssessmentService.checkForOutstandingAssessments(repOrder.getId());

        assertThat(errorList).hasSize(1).containsOnly(expectedError);
    }

    @ParameterizedTest
    @MethodSource(value = "outstandingAssessmentData")
    void givenOutstandingPassport_whenCheckForOutstandingAssessmentsIsCalled_thenErrorListReturned(
            String replaced, String valid, CurrentStatus status) {
        ErrorMessage expectedError =
                new ErrorMessage("", OutstandingAssessmentService.MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND);
        saveTestPassportedAssessment(repOrder, replaced, valid, status);
        List<ErrorMessage> errorList = outstandingAssessmentService.checkForOutstandingAssessments(repOrder.getId());

        assertThat(errorList).hasSize(1).containsOnly(expectedError);
    }

    @ParameterizedTest
    @MethodSource(value = "outstandingAssessmentData")
    void givenOutstandingHardship_whenCheckForOutstandingAssessmentsIsCalled_thenErrorListReturned(
            String replaced, String valid, CurrentStatus status) {
        ErrorMessage expectedError =
                new ErrorMessage("", OutstandingAssessmentService.MSG_OUTSTANDING_HARDSHIP_ASSESSMENT_FOUND);
        saveTestHardshipAssessment(repOrder, replaced, valid, status);
        List<ErrorMessage> errorList = outstandingAssessmentService.checkForOutstandingAssessments(repOrder.getId());

        assertThat(errorList).hasSize(1).containsOnly(expectedError);
    }

    // replaced / valid / status
    private static Stream<Arguments> nonOutstandingAssessmentData() {
        return Stream.of(
                Arguments.of(YES, YES, IN_PROGRESS),
                Arguments.of(YES, NO, IN_PROGRESS),
                Arguments.of(YES, YES, COMPLETE),
                Arguments.of(YES, NO, COMPLETE),
                Arguments.of(NO, NO, IN_PROGRESS),
                Arguments.of(NO, YES, COMPLETE),
                Arguments.of(NO, NO, COMPLETE));
    }

    @ParameterizedTest
    @MethodSource(value = "nonOutstandingAssessmentData")
    void givenNonOutstandingInitMeans_whenCheckForOutstandingAssessmentsIsCalled_thenErrorListEmpty(
            String replaced, String valid, CurrentStatus status) {
        saveTestInitMeansAssessment(repOrder, replaced, valid, status);
        List<ErrorMessage> errorList = outstandingAssessmentService.checkForOutstandingAssessments(repOrder.getId());

        assertThat(errorList).isEmpty();
    }

    @ParameterizedTest
    @MethodSource(value = "nonOutstandingAssessmentData")
    void givenNonOutstandingFullMeans_whenCheckForOutstandingAssessmentsIsCalled_thenErrorListEmpty(
            String replaced, String valid, CurrentStatus status) {
        saveTestFullMeansAssessment(repOrder, replaced, valid, status);
        List<ErrorMessage> errorList = outstandingAssessmentService.checkForOutstandingAssessments(repOrder.getId());

        assertThat(errorList).isEmpty();
    }

    @ParameterizedTest
    @MethodSource(value = "nonOutstandingAssessmentData")
    void givenNonOutstandingPassport_whenCheckForOutstandingAssessmentsIsCalled_thenErrorListEmpty(
            String replaced, String valid, CurrentStatus status) {
        saveTestPassportedAssessment(repOrder, replaced, valid, status);
        List<ErrorMessage> errorList = outstandingAssessmentService.checkForOutstandingAssessments(repOrder.getId());

        assertThat(errorList).isEmpty();
    }

    @ParameterizedTest
    @MethodSource(value = "nonOutstandingAssessmentData")
    void givenNonOutstandingHardship_whenCheckForOutstandingAssessmentsIsCalled_thenErrorListEmpty(
            String replaced, String valid, CurrentStatus status) {
        saveTestHardshipAssessment(repOrder, replaced, valid, status);
        List<ErrorMessage> errorList = outstandingAssessmentService.checkForOutstandingAssessments(repOrder.getId());

        assertThat(errorList).isEmpty();
    }

    @Test
    void givenAllThreeOutstandingAssessments_whenCheckForOutstandingAssessmentsIsCalled_thenErrorListHasThreeErrors() {
        List<ErrorMessage> expectedErrorList = List.of(
                new ErrorMessage("", OutstandingAssessmentService.MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND),
                new ErrorMessage("", OutstandingAssessmentService.MSG_OUTSTANDING_HARDSHIP_ASSESSMENT_FOUND),
                new ErrorMessage("", OutstandingAssessmentService.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND));
        saveTestPassportedAssessment(repOrder, NO, YES, IN_PROGRESS);
        saveTestHardshipAssessment(repOrder, NO, YES, IN_PROGRESS);
        saveTestFullMeansAssessment(repOrder, NO, YES, IN_PROGRESS);
        List<ErrorMessage> errorList = outstandingAssessmentService.checkForOutstandingAssessments(repOrder.getId());

        assertThat(errorList).hasSize(3).containsExactlyInAnyOrderElementsOf(expectedErrorList);
    }

    @Test
    void givenOutstandingMeans_whenLegacyCheckForOutstandingAssessmentsIsCalled_thenExceptionThrown() {
        var repId = repOrder.getId();
        saveTestFullMeansAssessment(repOrder, NO, YES, IN_PROGRESS);
        assertThatThrownBy(() -> outstandingAssessmentService.legacyCheckForOutstandingAssessments(repId))
                .isInstanceOf(ValidationException.class)
                .hasMessage(OutstandingAssessmentService.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND);
    }

    @Test
    void givenOutstandingPassport_whenLegacyCheckForOutstandingAssessmentsIsCalled_thenExceptionThrown() {
        var repId = repOrder.getId();
        saveTestPassportedAssessment(repOrder, NO, YES, IN_PROGRESS);
        assertThatThrownBy(() -> outstandingAssessmentService.legacyCheckForOutstandingAssessments(repId))
                .isInstanceOf(ValidationException.class)
                .hasMessage(OutstandingAssessmentService.MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND);
    }

    @Test
    void givenOutstandingHardship_whenLegacyCheckForOutstandingAssessmentsIsCalled_thenExceptionThrown() {
        var repId = repOrder.getId();
        saveTestHardshipAssessment(repOrder, NO, YES, IN_PROGRESS);
        assertThatThrownBy(() -> outstandingAssessmentService.legacyCheckForOutstandingAssessments(repId))
                .isInstanceOf(ValidationException.class)
                .hasMessage(OutstandingAssessmentService.MSG_OUTSTANDING_HARDSHIP_ASSESSMENT_FOUND);
    }

    @Test
    void givenMultipleOutstanding_whenLegacyCheckForOutstandingAssessmentsIsCalled_thenOnlyOneMessage() {
        var repId = repOrder.getId();
        saveTestPassportedAssessment(repOrder, NO, YES, IN_PROGRESS);
        saveTestHardshipAssessment(repOrder, NO, YES, IN_PROGRESS);
        saveTestFullMeansAssessment(repOrder, NO, YES, IN_PROGRESS);
        assertThatThrownBy(() -> outstandingAssessmentService.legacyCheckForOutstandingAssessments(repId))
                .isInstanceOf(ValidationException.class)
                .hasMessage(OutstandingAssessmentService.MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND);
    }

    private void saveTestInitMeansAssessment(
            RepOrderEntity repOrder, String replaced, String valid, CurrentStatus status) {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessment.setRepOrder(repOrder);
        financialAssessment.setReplaced(replaced);
        financialAssessment.setValid(valid);
        financialAssessment.setFassInitStatus(status.getStatus());
        repos.financialAssessment.save(financialAssessment);
    }

    private void saveTestFullMeansAssessment(
            RepOrderEntity repOrderEntity, String replaced, String valid, CurrentStatus status) {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessment.setRepOrder(repOrderEntity);
        financialAssessment.setReplaced(replaced);
        financialAssessment.setValid(valid);
        financialAssessment.setFassFullStatus(status.getStatus());
        repos.financialAssessment.save(financialAssessment);
    }

    private void saveTestPassportedAssessment(
            RepOrderEntity repOrderEntity, String replaced, String valid, CurrentStatus status) {
        PassportAssessmentEntity passportAssessment = TestEntityDataBuilder.getPassportAssessmentEntity();
        passportAssessment.setRepOrder(repOrderEntity);
        passportAssessment.setReplaced(replaced);
        passportAssessment.setValid(valid);
        passportAssessment.setPastStatus(status.getStatus());
        repos.passportAssessment.save(passportAssessment);
    }

    private void saveTestHardshipAssessment(
            RepOrderEntity repOrderEntity, String replaced, String valid, CurrentStatus status) {
        HardshipReviewEntity hardshipAssessment = TestEntityDataBuilder.getHardshipReviewEntity();
        hardshipAssessment.setRepId(repOrderEntity.getId());
        hardshipAssessment.setReplaced(replaced);
        hardshipAssessment.setValid(valid);
        hardshipAssessment.setStatus(status.getStatus());
        repos.hardshipReview.save(hardshipAssessment);
    }
}

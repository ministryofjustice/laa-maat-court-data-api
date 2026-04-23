package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static gov.uk.courtdata.builder.TestEntityDataBuilder.TEST_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AssessmentReplacementServiceTest {

    @Mock
    private HardshipReviewRepository hardshipReviewRepository;
    @Mock
    private PassportAssessmentRepository passportAssessmentRepository;
    @Mock
    private FinancialAssessmentRepository financialAssessmentRepository;

    @InjectMocks
    private AssessmentReplacementService assessmentReplacementService;

    // Financial Assessments

    @Test
    void givenFinancialAssessment_whenReplacePreviousAssessmentsIsInvoked_thenOldAssessmentsAreReplaced() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();

        assessmentReplacementService.replacePreviousAssessments(financialAssessment);

        verify(financialAssessmentRepository).replaceAllByRepIdExcludingFinancialAssessment(REP_ID, TEST_ID);
        verify(hardshipReviewRepository).replaceAllByRepIdExcludingFinancialAssessment(REP_ID, TEST_ID);
        verify(passportAssessmentRepository).replaceAllByRepId(REP_ID);
    }

    @Test
    void givenFinancialAssessmentMissingRepOrderId_whenReplacePreviousAssessmentsIsInvoked_thenNothingIsReplaced() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessment.setId(TEST_ID);
        financialAssessment.getRepOrder().setId(null);
        assessmentReplacementService.replacePreviousAssessments(financialAssessment);
        verifyNoInteractions(financialAssessmentRepository, hardshipReviewRepository, passportAssessmentRepository);
    }

    @Test
    void givenFinancialAssessmentMissingRepOrder_whenReplacePreviousAssessmentsIsInvoked_thenNothingIsReplaced() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessment.setId(TEST_ID);
        financialAssessment.setRepOrder(null);
        assessmentReplacementService.replacePreviousAssessments(financialAssessment);
        verifyNoInteractions(financialAssessmentRepository, hardshipReviewRepository, passportAssessmentRepository);
    }

    @Test
    void givenFinancialAssessmentMissingAssessmentId_whenReplacePreviousAssessmentsIsInvoked_thenNothingIsReplaced() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessment.setId(null);
        assessmentReplacementService.replacePreviousAssessments(financialAssessment);
        verifyNoInteractions(financialAssessmentRepository, hardshipReviewRepository, passportAssessmentRepository);
    }

    @Test
    void givenFinancialAssessmentMissingAssessmentIdAndMissingRepOrder_whenReplacePreviousAssessmentsIsInvoked_thenNothingIsReplaced() {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessment.setId(null);
        financialAssessment.setRepOrder(null);
        assessmentReplacementService.replacePreviousAssessments(financialAssessment);
        verifyNoInteractions(financialAssessmentRepository, hardshipReviewRepository, passportAssessmentRepository);
    }


    // Passported Assessments
    @Test
    void givenPassportAssessment_whenReplacePreviousAssessmentsIsInvoked_thenOldAssessmentsAreReplaced() {
        PassportAssessmentEntity passportAssessment = TestEntityDataBuilder.getPassportAssessmentEntity();

        assessmentReplacementService.replacePreviousAssessments(passportAssessment);

        verify(financialAssessmentRepository).replaceAllByRepId(REP_ID);
        verify(hardshipReviewRepository).replaceAllByRepId(REP_ID);
        verify(passportAssessmentRepository).replaceAllByRepIdExcludingPassportedAssessment(REP_ID, TEST_ID);
    }

    @Test
    void givenPassportedAssessmentMissingRepOrderId_whenReplacePreviousAssessmentsIsInvoked_thenNothingIsReplaced() {
        PassportAssessmentEntity passportedAssessment = TestEntityDataBuilder.getPassportAssessmentEntity();
        passportedAssessment.setId(TEST_ID);
        passportedAssessment.getRepOrder().setId(null);
        assessmentReplacementService.replacePreviousAssessments(passportedAssessment);
        verifyNoInteractions(financialAssessmentRepository, hardshipReviewRepository, passportAssessmentRepository);
    }

    @Test
    void givenPassportedAssessmentMissingRepOrder_whenReplacePreviousAssessmentsIsInvoked_thenNothingIsReplaced() {
        PassportAssessmentEntity passportedAssessment = TestEntityDataBuilder.getPassportAssessmentEntity();
        passportedAssessment.setId(TEST_ID);
        passportedAssessment.setRepOrder(null);
        assessmentReplacementService.replacePreviousAssessments(passportedAssessment);
        verifyNoInteractions(financialAssessmentRepository, hardshipReviewRepository, passportAssessmentRepository);
    }

    @Test
    void givenPassportedAssessmentMissingAssessmentId_whenReplacePreviousAssessmentsIsInvoked_thenNothingIsReplaced() {
        PassportAssessmentEntity passportedAssessment = TestEntityDataBuilder.getPassportAssessmentEntity();
        passportedAssessment.setId(null);
        assessmentReplacementService.replacePreviousAssessments(passportedAssessment);
        verifyNoInteractions(financialAssessmentRepository, hardshipReviewRepository, passportAssessmentRepository);
    }

    @Test
    void givenPassportedAssessmentMissingAssessmentIdAndMissingRepOrder_whenReplacePreviousAssessmentsIsInvoked_thenNothingIsReplaced() {
        PassportAssessmentEntity passportedAssessment = TestEntityDataBuilder.getPassportAssessmentEntity();
        passportedAssessment.setId(null);
        passportedAssessment.setRepOrder(null);
        assessmentReplacementService.replacePreviousAssessments(passportedAssessment);
        verifyNoInteractions(financialAssessmentRepository, hardshipReviewRepository, passportAssessmentRepository);
    }
}


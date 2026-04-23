package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource(value = "idPopulationFailureTestData")
    void givenFinancialAssessmentMissingIds_whenReplacePreviousAssessmentsIsInvoked_thenNothingIsReplaced(Integer assessmentId, boolean hasRepOrderEntity, Integer repId) {
        FinancialAssessmentEntity financialAssessment = TestEntityDataBuilder.getFinancialAssessmentEntity();
        financialAssessment.setId(assessmentId);
        if(!hasRepOrderEntity){
            financialAssessment.setRepOrder(null);
        } else {
            financialAssessment.getRepOrder().setId(repId);
        }
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

    @ParameterizedTest
    @MethodSource(value = "idPopulationFailureTestData")
    void givenPassportedAssessmentMissingIds_whenReplacePreviousAssessmentsIsInvoked_thenNothingIsReplaced(Integer assessmentId, boolean hasRepOrderEntity, Integer repId) {
        PassportAssessmentEntity passportedAssessment = TestEntityDataBuilder.getPassportAssessmentEntity();
        passportedAssessment.setId(assessmentId);
        if(!hasRepOrderEntity){
            passportedAssessment.setRepOrder(null);
        } else {
            passportedAssessment.getRepOrder().setId(repId);
        }
        assessmentReplacementService.replacePreviousAssessments(passportedAssessment);

        verifyNoInteractions(financialAssessmentRepository, hardshipReviewRepository, passportAssessmentRepository);
    }

    /**
     * Basic matrix representing the following values:
     * <ul>
     * <li>{@code assessmentId} - {@code Integer} - The value to set the AssessmentId to</li>
     * <li>{@code hasRepOrderEntity} - {@code Boolean} - determines if the RepOrderEntity should be present on the assessment.</li>
     * <li>{@code repOrderId} - {@code Integer} - The value to set the RepOrderId to</li>
     * </ul>
     * Covers the different variations of the three being populated. With the exception of if {@code hasRepOrderEntity = false}, {@code repOrderId} will be ignored.
     */
    private static Stream<Arguments> idPopulationFailureTestData() {
        return Stream.of(
                Arguments.of(TEST_ID, true, null ),
                Arguments.of(TEST_ID, false, null ),
                Arguments.of(null, true, REP_ID ),
                Arguments.of(null, true, null ),
                Arguments.of(null, false, null)
        );
    }
}
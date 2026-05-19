package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.specification.FinancialAssessmentSpecification;
import gov.uk.courtdata.assessment.specification.PassportAssessmentSpecification;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.hardship.specification.HardshipSpecification;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutstandingAssessmentService {
    private final PassportAssessmentRepository passportAssessmentRepository;
    private final FinancialAssessmentRepository financialAssessmentRepository;
    private final HardshipReviewRepository hardshipReviewRepository;

    public static final String MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND =
            "An incomplete means assessment is associated with the current application";
    public static final String MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND =
            "An incomplete passport assessment is associated with the current application";
    public static final String MSG_OUTSTANDING_HARDSHIP_ASSESSMENT_FOUND =
            "An incomplete hardship assessment is associated with the current application";

    /**
     * Checks that the RepOrder with repId has no outstanding assessments which will stop creation of a new Assessment.
     * If any blocking assessments are present, will return a list containing the messages.
     * @param repId Id of the RepOrder to check. Will not check if null.
     * @return List of ErrorMessage.
     */
    public List<ErrorMessage> checkForOutstandingAssessments(Integer repId) {
        var errorList = new ArrayList<ErrorMessage>();
        if (repId != null) {
            hasOutstandingFinancialAssessments(repId).ifPresent(errorList::add);
            hasOutstandingPassportedAssessment(repId).ifPresent(errorList::add);
            hasOutstandingHardshipAssessment(repId).ifPresent(errorList::add);
        }
        return errorList;
    }

    /**
     * Legacy mechanism for checking that the RepOrder with repId has no outstanding assessments which will stop creation of a new Assessment.
     * Is used for processes which cannot handle a list of errors, and expects the validator to exception instead.
     * <br>Will generally be used for v1 endpoints.
     * @param repId Id of the RepOrder to check. Will not check if null.
     */
    public void legacyCheckForOutstandingAssessments(Integer repId) {
        if (repId != null) {
            hasOutstandingFinancialAssessments(repId).ifPresent(this::throwValidationException);
            hasOutstandingPassportedAssessment(repId).ifPresent(this::throwValidationException);
            hasOutstandingHardshipAssessment(repId).ifPresent(this::throwValidationException);
        }
    }

    /** Helper method to throw Validation Exception for the passed in ErrorMessage */
    private void throwValidationException(ErrorMessage message) {
        throw new ValidationException(message.message());
    }

    private Optional<ErrorMessage> hasOutstandingFinancialAssessments(Integer repId) {
        if (financialAssessmentRepository.exists(FinancialAssessmentSpecification.hasRepId(repId)
                .and(FinancialAssessmentSpecification.isCurrent())
                .and(FinancialAssessmentSpecification.isValid())
                .and(FinancialAssessmentSpecification.isInProgress()))) {
            return Optional.of(new ErrorMessage("", MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND));
        }
        return Optional.empty();
    }

    private Optional<ErrorMessage> hasOutstandingPassportedAssessment(Integer repId) {
        if (passportAssessmentRepository.exists(PassportAssessmentSpecification.hasRepId(repId)
                .and(PassportAssessmentSpecification.isCurrent())
                .and(PassportAssessmentSpecification.isValid())
                .and(PassportAssessmentSpecification.isInProgress()))) {
            return Optional.of(new ErrorMessage("", MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND));
        }
        return Optional.empty();
    }

    private Optional<ErrorMessage> hasOutstandingHardshipAssessment(Integer repId) {
        if (hardshipReviewRepository.exists(HardshipSpecification.hasRepId(repId)
                .and(HardshipSpecification.isCurrent())
                .and(HardshipSpecification.isValid())
                .and(HardshipSpecification.isInProgress()))) {
            return Optional.of(new ErrorMessage("", MSG_OUTSTANDING_HARDSHIP_ASSESSMENT_FOUND));
        }
        return Optional.empty();
    }
}

package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.specification.FinancialAssessmentSpecification;
import gov.uk.courtdata.assessment.specification.PassportAssessmentSpecification;
import gov.uk.courtdata.hardship.specification.HardshipSpecification;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.util.Optional;
import java.util.stream.Stream;

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
     * Will return an Optional containing the error message if a blocking assessment is found.
     * @param repId Id of the RepOrder to check. Will not check if null.
     * @return Optional containing either the error message, or empty is no validation fails.
     */
    public Optional<ErrorMessage> checkForOutstandingAssessments(Integer repId) {
        if (repId == null) {
            return Optional.empty();
        }
        return Stream.of(
                        validateNoOutstandingFinancialAssessments(repId),
                        validateNoOutstandingPassportedAssessments(repId),
                        validateNoOutstandingHardshipAssessments(repId))
                .flatMap(Optional::stream)
                .findFirst();
    }

    private Optional<ErrorMessage> validateNoOutstandingFinancialAssessments(Integer repId) {
        if (financialAssessmentRepository.exists(FinancialAssessmentSpecification.hasRepId(repId)
                .and(FinancialAssessmentSpecification.isCurrent())
                .and(FinancialAssessmentSpecification.isValid())
                .and(FinancialAssessmentSpecification.isInProgress()))) {
            return Optional.of(new ErrorMessage("", MSG_OUTSTANDING_MEANS_ASSESSMENT_FOUND));
        }
        return Optional.empty();
    }

    private Optional<ErrorMessage> validateNoOutstandingPassportedAssessments(Integer repId) {
        if (passportAssessmentRepository.exists(PassportAssessmentSpecification.hasRepId(repId)
                .and(PassportAssessmentSpecification.isCurrent())
                .and(PassportAssessmentSpecification.isValid())
                .and(PassportAssessmentSpecification.isInProgress()))) {
            return Optional.of(new ErrorMessage("", MSG_OUTSTANDING_PASSPORT_ASSESSMENT_FOUND));
        }
        return Optional.empty();
    }

    private Optional<ErrorMessage> validateNoOutstandingHardshipAssessments(Integer repId) {
        if (hardshipReviewRepository.exists(HardshipSpecification.hasRepId(repId)
                .and(HardshipSpecification.isCurrent())
                .and(HardshipSpecification.isValid())
                .and(HardshipSpecification.isInProgress()))) {
            return Optional.of(new ErrorMessage("", MSG_OUTSTANDING_HARDSHIP_ASSESSMENT_FOUND));
        }
        return Optional.empty();
    }
}

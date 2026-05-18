package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.specification.FinancialAssessmentSpecification;
import gov.uk.courtdata.assessment.specification.PassportAssessmentSpecification;
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

    public List<ErrorMessage> checkForOutstandingAssessments(final Integer repId) {
        var errorList = new ArrayList<ErrorMessage>();
        hasOutstandingFinancialAssessments(repId).ifPresent(errorList::add);
        hasOutstandingPassportedAssessment(repId).ifPresent(errorList::add);
        hasOutstandingHardshipAssessment(repId).ifPresent(errorList::add);
        return errorList;
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
                .and(HardshipSpecification.isInProgress()))) {
            return Optional.of(new ErrorMessage("", MSG_OUTSTANDING_HARDSHIP_ASSESSMENT_FOUND));
        }
        return Optional.empty();
    }
}

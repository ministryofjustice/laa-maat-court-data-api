package gov.uk.courtdata.hearing.crowncourt.validator;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.RepOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static gov.uk.courtdata.enums.CrownCourtAppealOutcome.isAppeal;
import static gov.uk.courtdata.enums.CrownCourtCaseType.caseTypeForAppeal;
import static gov.uk.courtdata.enums.CrownCourtCaseType.caseTypeForTrial;
import static gov.uk.courtdata.enums.CrownCourtTrialOutcome.isTrial;

@Component
@RequiredArgsConstructor
public class CaseTypeValidator {

    private final RepOrderRepository repOrderRepository;

    public void validate(final HearingResulted hearingResulted, String crownCourtOutCome) {

        Optional<RepOrderEntity> repOrderEntity =
                repOrderRepository.findById(hearingResulted.getMaatId());

        if (repOrderEntity.isPresent()) {

            RepOrderEntity repOrder = repOrderEntity.get();
            String caseType = repOrder.getCatyCaseType();

            if (isTrial(crownCourtOutCome) && !caseTypeForTrial(caseType)) {

                throw new ValidationException("Crown Court - Case type not valid for Trial.");
            }

            if (isAppeal(crownCourtOutCome) && !caseTypeForAppeal(caseType)) {

                throw new ValidationException("Crown Court  - Case type not valid for Appeal.");
            }
        }
    }
}

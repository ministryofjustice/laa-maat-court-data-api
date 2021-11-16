package gov.uk.courtdata.hearing.crowncourt.validator;

import gov.uk.courtdata.entity.CrownCourtOutComeEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.CrownCourtOutcomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CrownCourtOutComesValidator {

    private final CrownCourtOutcomeRepository crownCourtOutcomeRepository;

    //todo: change to a string value
    public void validate(final HearingResulted hearingResulted) {

        final String ccOutcome = hearingResulted.getCcOutComeData().getCcOutcome();

        boolean isValidOutCome = crownCourtOutcomeRepository
                .findAll()
                .stream()
                .map(CrownCourtOutComeEntity::getOutcome)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()).contains(ccOutcome);
        if (!isValidOutCome) {
            throw new MAATCourtDataException("Invalid Crown Court Outcome : "
                    .concat(ccOutcome.concat(" is passed in for MAAT ID: " + hearingResulted.getMaatId())));
        }
    }
}

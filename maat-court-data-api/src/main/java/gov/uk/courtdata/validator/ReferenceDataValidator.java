package gov.uk.courtdata.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.CourtHouseCodesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 *<code>ReferenceDataValidator</code>
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ReferenceDataValidator implements IValidator<Void, CaseDetails> {


    private final CourtHouseCodesRepository courtHouseCodesRepository;

    /**
     * @param caseDetailsJson
     * @return
     * @throws ValidationException
     */
    @Override
    public Optional<Void> validate(final CaseDetails caseDetailsJson) throws ValidationException {


        //TODO: is it to validate all the court location in the session list exists?
        String courtLocation = caseDetailsJson.getSessions().iterator().next().getCourtLocation();

        log.info(caseDetailsJson.getSessions().iterator().toString());

        final int count = courtHouseCodesRepository.getCount(courtLocation);

        if (count == 0) {
            throw new ValidationException("Court code not found.");
        }

        return Optional.empty();
    }
}

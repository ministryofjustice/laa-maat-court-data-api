package gov.uk.courtdata.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.CourtHouseCodesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;


/**
 * <code>ReferenceDataValidator</code>
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
    public Optional<Void> validate(final CaseDetails caseDetailsJson)  {


        caseDetailsJson.getSessions().forEach(ses ->
        {
            final int count = courtHouseCodesRepository.getCount(ses.getCourtLocation());

            if (count == 0) {
                throw new ValidationException(format("Court location not found %s", ses.getCourtLocation()));
            }
        });


        return Optional.empty();
    }
}

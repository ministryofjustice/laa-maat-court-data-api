package gov.uk.courtdata.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *<code>LinkExistsValidator</code> validate maatid has no link established.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class LinkExistsValidator implements IValidator<Void, CaseDetails> {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;


    /**
     * @param caseDetailsJson
     * @return
     * @throws ValidationException
     */
    @Override
    public Optional<Void> validate(CaseDetails caseDetailsJson) throws ValidationException {

        /**
         * The Maat Id is not linked already.
         */
        final int linkCount = wqLinkRegisterRepository.getCountByMaatId(caseDetailsJson.getMaatId());

        if (linkCount > 0)
            throw new ValidationException("MaatId already linked to the application.");

        /**
         * The case must have not registry entries or none if wasn't previously removed
         */
//        final int cpLibraIdCount = wqLinkRegisterRepository
//                .getCountByIdAndCjsCourt(caseDetailsJson.getCpLibraId(),
//                        String.valueOf(caseDetailsJson.getCjsAreaCode()));
//
//        if (cpLibraIdCount > 0) {
//            throw new ValidationException("The case was'nt unlinked");
//        }

        return Optional.empty();
    }
}

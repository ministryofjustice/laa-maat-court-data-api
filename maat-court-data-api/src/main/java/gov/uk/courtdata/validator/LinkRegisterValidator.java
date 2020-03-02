package gov.uk.courtdata.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkRegisterValidator {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    public void validateMAATId(Integer maatId) {

        final int linkCount = wqLinkRegisterRepository.getCountByMaatId(maatId);

        if (linkCount == 0) {
            throw new ValidationException("There No Link Available for MAAT ID : " + maatId);
        } else if (linkCount > 1) {
            throw new ValidationException("There are multiple Links found for MAAT ID : " + maatId);
        }

    }

}






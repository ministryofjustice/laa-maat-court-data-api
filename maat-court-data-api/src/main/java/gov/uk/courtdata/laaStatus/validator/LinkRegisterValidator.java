package gov.uk.courtdata.laaStatus.validator;

import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.repository.WqLinkRegisterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkRegisterValidator {

    private final WqLinkRegisterRepository wqLinkRegisterRepository;

    public void validate(Integer maatId) {

        final int linkCount = wqLinkRegisterRepository.getCountByMaatId(maatId);

        if (linkCount == 0) {
            throw new MaatCourtDataException("There is No Link Available for this MAAT ID : " + maatId);
        } else if (linkCount > 1) {
            throw new MaatCourtDataException("There are multiple Links found for this MAAT ID : " + maatId);
        }

    }

}






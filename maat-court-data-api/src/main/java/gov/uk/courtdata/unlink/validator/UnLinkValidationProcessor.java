package gov.uk.courtdata.unlink.validator;

import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.link.validator.LinkExistsValidator;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class UnLinkValidationProcessor {

    private final MaatIdValidator maatIdValidator;
    private final LinkExistsValidator linkExistsValidator;
    private final ReasonValidator reasonValidator;

    /**
     * Validate the Unlink request.
     * @param unlink
     */
    public void validate(Unlink unlink) {

        if (unlink == null) {
            throw new MAATCourtDataException("Unlink Request is empty");
        }
        //check if maat-id is not null
        maatIdValidator.validate(unlink.getMaatId());
        //check if the maat-id already have an active link
        linkExistsValidator.validate(unlink.getMaatId());
        reasonValidator.validate(unlink.getReasonId());
    }

}

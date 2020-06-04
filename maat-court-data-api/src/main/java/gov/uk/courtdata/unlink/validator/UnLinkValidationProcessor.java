package gov.uk.courtdata.unlink.validator;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.link.validator.LinkExistsValidator;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component

public class UnLinkValidationProcessor {

    private final MaatIdValidator maatIdValidator;
    private final LinkExistsValidator linkExistsValidator;
    private final ReasonValidator reasonValidator;
    private final UserIdValidator userIdValidator;


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
        userIdValidator.validate(unlink.getUserId());
    }

    /**
     * Check if maat-id has any active link.
     * @param linkRegisterEntities
     * @param maatId
     */
    public void validateWQLinkRegister(List<WqLinkRegisterEntity> linkRegisterEntities, Integer maatId) {

        if (linkRegisterEntities == null || linkRegisterEntities.isEmpty()) {
            throw new MAATCourtDataException("There is No link established for MAAT ID : " + maatId);
        }
        if (linkRegisterEntities.size() > 1) {
            throw new MAATCourtDataException("There are multiple links found for  MAAT ID : " + maatId);
        }
    }

}

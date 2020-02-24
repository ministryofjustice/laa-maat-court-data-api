package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.model.Unlink;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnlinkValidator {

    public void validateRequest(Unlink unlink) {


        if (unlink == null) {
            throw new MaatCourtDataException("Unlink Request is empty");
        }
        if (unlink.getMaatId() == null) {
            throw new MaatCourtDataException("There is No Valid MAAT ID found");
        }
        if (unlink.getUserId() == null) {
            throw new MaatCourtDataException("There is No Valid User ID found");
        }

        if (unlink.getReasonId() == null) {
            throw new MaatCourtDataException("There is No Valid Reason ID found");
        }
    }

    public void validateWQLinkRegister(List<WqLinkRegisterEntity> linkRegisterEntities, Integer maatId) {

        if (linkRegisterEntities == null || linkRegisterEntities.isEmpty()) {
            throw new MaatCourtDataException("There is No link established for MAAT ID : " + maatId);
        }
        if (linkRegisterEntities.size() > 1) {
            throw new MaatCourtDataException("There are multiple links found for  MAAT ID : " + maatId);
        }
    }

}


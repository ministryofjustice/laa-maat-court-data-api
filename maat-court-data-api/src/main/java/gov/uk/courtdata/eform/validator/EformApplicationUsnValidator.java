package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.dto.EformStagingDTO;
import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.entity.EformsStagingEntity;
import gov.uk.courtdata.exception.USNValidationException;
import org.springframework.stereotype.Component;

@Component
public class EformApplicationUsnValidator {

    public void validate() {


    }

    public boolean validate(EformsStagingEntity eformsStagingEntity, EformStagingDTO eformStagingDTO, EformStagingRepository eformStagingRepository) {
        if (eformStagingRepository.existsById(eformsStagingEntity.getUsn())) {
            return true;
        } else {
            throw new USNValidationException("The USN number entered is not valid.");
        }
    }
}

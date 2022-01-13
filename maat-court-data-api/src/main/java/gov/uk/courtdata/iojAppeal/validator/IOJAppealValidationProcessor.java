package gov.uk.courtdata.iojAppeal.validator;

import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.iojAppeal.UpdateIOJAppeal;
import gov.uk.courtdata.repository.IOJAppealRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IOJAppealValidationProcessor {

    private final IOJAppealRepository iojAppealRepository;

    public Optional<Void> validate(UpdateIOJAppeal iojAppeal){
        var iojAppealEntity = getIOJAppealFromDB(iojAppeal);

        validateModifiedDateHasNotChanged(iojAppeal, iojAppealEntity);

        validateStatusIsNotComplete(iojAppealEntity);

        return Optional.empty();
    }
    private IOJAppealEntity getIOJAppealFromDB(UpdateIOJAppeal iojAppeal) {
       return iojAppealRepository.findById(iojAppeal.getId()).orElseThrow(()->new ValidationException("IOJ Appeal does not exist in Db. ID: " + iojAppeal.getId()));
    }

    private void validateModifiedDateHasNotChanged(UpdateIOJAppeal iojAppeal, IOJAppealEntity iojAppealEntity){
        if (ObjectUtils.notEqual(iojAppeal.getDateModified(), iojAppealEntity.getDateModified())){
            throw new ValidationException("IoJ Appeal has been modified by another user. IOJAppeal ID: "+ iojAppealEntity.getId());
        }
    }

    private void validateStatusIsNotComplete(IOJAppealEntity iojAppealEntity){
        if (StringUtils.equalsIgnoreCase("COMPLETE",iojAppealEntity.getIapsStatus())){
            throw new ValidationException("User cannot modify a complete ioj appeal. IOJAppeal ID: "+ iojAppealEntity.getId());
        }
    }
}

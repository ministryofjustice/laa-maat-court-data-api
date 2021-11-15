package gov.uk.courtdata.helper;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class RepOrderCPDataHelper {

    private final RepOrderCPDataRepository repOrderCPDataRepository;

    public Integer getMaatIdByDefendantId(String defendantId){

        Optional<RepOrderCPDataEntity> repOrderCPDataEntity = repOrderCPDataRepository.findByDefendantId(defendantId);

            if (repOrderCPDataEntity.isPresent() ) {
                 return repOrderCPDataEntity.get().getRepOrderId();
            }

        throw new ValidationException(defendantId + " defendantId is invalid and No MAAT-Id associated with this.");
    }
}
package gov.uk.courtdata.iojAppeal.service;

import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.iojAppeal.impl.IOJAppealImpl;
import gov.uk.courtdata.iojAppeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.model.CreateIOJAppeal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IOJAppealService {

    private final IOJAppealImpl iojAppealImpl;
    private final IOJAppealMapper iojAppealMapper;

    public IOJAppealDTO find(Integer iojAppealId) {
        var iojAppealEntity = iojAppealImpl.find(iojAppealId);

        return buildIOJAppealDTOFrom(iojAppealEntity);
    }

    public IOJAppealDTO create(CreateIOJAppeal iojAppeal) {
        log.info("Create IOJ Appeal - Transaction Processing - Start");
        var iojAppealDTO =  iojAppealMapper.toIOJAppealDTO(iojAppeal);
        //TODO: Create new IOJ Appeal record -> look at FinancialAssessmentImpl.create() to see how it is done
        //TODO: UPDATE all previous records look at Matt's FinancialAssessmentService.create() function to see how it is done
        log.info("Create IOJ Appeal - Transaction Processing - end");
        return null;
    }

    private IOJAppealDTO buildIOJAppealDTOFrom(IOJAppealEntity iojAppealEntity) {
        return iojAppealMapper.toIOJAppealDTO(iojAppealEntity);
    }


}

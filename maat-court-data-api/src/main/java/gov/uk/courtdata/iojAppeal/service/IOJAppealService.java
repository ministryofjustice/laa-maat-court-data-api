package gov.uk.courtdata.iojAppeal.service;

import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.iojAppeal.impl.IOJAppealImpl;
import gov.uk.courtdata.iojAppeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.model.CreateIOJAppeal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IOJAppealService {

    private final IOJAppealImpl iojAppealImpl;
    private final IOJAppealMapper iojAppealMapper;

    public IOJAppealDTO find(Integer iojAppealId) {
        var iojAppealEntity = iojAppealImpl.find(iojAppealId);

        return iojAppealMapper.toIOJAppealDTO(iojAppealEntity);
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public IOJAppealDTO create(CreateIOJAppeal iojAppeal) {
        log.info("Create IOJ Appeal - Transaction Processing - Start");
        var iojAppealDTO =  iojAppealMapper.toIOJAppealDTO(iojAppeal);

        log.info("Creating new IOJAppeal record");
        var iojAppealEntity = iojAppealImpl.create(iojAppealDTO);

        log.info("Update previous IOJ Appeal records and set them to replaced");
        iojAppealImpl.setOldIOJAppealReplaced(iojAppealEntity.getRepId(), iojAppealEntity.getId());

        log.info("Create IOJ Appeal - Transaction Processing - end");
        return iojAppealMapper.toIOJAppealDTO(iojAppealEntity);
    }
}

package gov.uk.courtdata.iojappeal.service;

import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.iojappeal.impl.IOJAppealImpl;
import gov.uk.courtdata.iojappeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.model.iojAppeal.CreateIOJAppeal;
import gov.uk.courtdata.model.iojAppeal.UpdateIOJAppeal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IOJAppealService {

    private final IOJAppealImpl iojAppealImpl;
    private final IOJAppealMapper iojAppealMapper;

    @Transactional(readOnly = true)
    public IOJAppealDTO find(Integer iojAppealId) {
        IOJAppealEntity iojAppealEntity = iojAppealImpl.find(iojAppealId);
        if (iojAppealEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No IOJ Appeal found for ID: %s", iojAppealId));
        }
        return iojAppealMapper.toIOJAppealDTO(iojAppealEntity);
    }

    @Transactional(readOnly = true)
    public IOJAppealDTO findByRepId(int repId) {
        IOJAppealEntity iojAppealEntity = iojAppealImpl.findByRepId(repId);
        if (iojAppealEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No IOJ Appeal found for REP ID: %s", repId));
        }
        return iojAppealMapper.toIOJAppealDTO(iojAppealEntity);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public IOJAppealDTO create(CreateIOJAppeal iojAppeal) {
        log.info("Create IOJ Appeal - Transaction Processing - Start");
        var iojAppealDTO = iojAppealMapper.toIOJAppealDTO(iojAppeal);

        log.info("Creating new IOJAppeal record");
        var iojAppealEntity = iojAppealImpl.create(iojAppealDTO);

        log.info("Update previous IOJ Appeal records and set them to replaced");

        iojAppealImpl.setOldIOJAppealsReplaced(iojAppealEntity.getRepOrder().getId(), iojAppealEntity.getId());

        log.info("Create IOJ Appeal - Transaction Processing - end");
        return iojAppealMapper.toIOJAppealDTO(iojAppealEntity);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public IOJAppealDTO update(UpdateIOJAppeal iojAppeal) {
        log.info("Update IOJ Appeal - Transaction Processing - Start");
        var iojAppealDTO = iojAppealMapper.toIOJAppealDTO(iojAppeal);

        log.info("Updating IOJAppeal record");
        var updatedIOJAppealEntity = iojAppealImpl.update(iojAppealDTO);

        log.info("Update IOJ Appeal - Transaction Processing - end");
        return iojAppealMapper.toIOJAppealDTO(updatedIOJAppealEntity);
    }

    @Transactional(readOnly = true)
    public IOJAppealDTO findCurrentPassedAppealByRepId(int repId) {
        IOJAppealEntity iojAppealEntity = iojAppealImpl.findCurrentPassedByRepId(repId);
        if (iojAppealEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No IOJ Appeal found for REP ID: %s", repId));
        }
        return iojAppealMapper.toIOJAppealDTO(iojAppealEntity);
    }
}

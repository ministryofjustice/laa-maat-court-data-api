package gov.uk.courtdata.iojappeal.service;

import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
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
public class IOJAppealV1Service {

    private final IOJAppealPersistenceService iojAppealPersistenceService;
    private final IOJAppealMapper iojAppealMapper;

    @Transactional(readOnly = true)
    public IOJAppealDTO find(Integer iojAppealId) {
        IOJAppealEntity iojAppealEntity = iojAppealPersistenceService.find(iojAppealId);
        if (iojAppealEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No IoJ Appeal found for ID: %s", iojAppealId));
        }
        return iojAppealMapper.toIOJAppealDTO(iojAppealEntity);
    }

    @Transactional(readOnly = true)
    public IOJAppealDTO findByRepId(int repId) {
        IOJAppealEntity iojAppealEntity = iojAppealPersistenceService.findByRepId(repId);
        if (iojAppealEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No IoJ Appeal found for REP ID: %s", repId));
        }
        return iojAppealMapper.toIOJAppealDTO(iojAppealEntity);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public IOJAppealDTO create(CreateIOJAppeal iojAppeal) {
        log.info("Create IoJ Appeal - Transaction Processing - Start");
        IOJAppealDTO iojAppealDTO = iojAppealMapper.toIOJAppealDTO(iojAppeal);

        log.info("Creating new IoJAppeal record");
        IOJAppealEntity iojAppealEntity = iojAppealMapper.toIojAppealEntity(iojAppealDTO);
        iojAppealPersistenceService.save(iojAppealEntity);

        log.info("Update previous IoJ Appeal records and set them to replaced");

        iojAppealPersistenceService.setOldIOJAppealsReplaced(iojAppealEntity.getRepOrder().getId(), iojAppealEntity.getId());

        log.info("Create IoJ Appeal - Transaction Processing - end");
        return iojAppealMapper.toIOJAppealDTO(iojAppealEntity);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public IOJAppealDTO update(UpdateIOJAppeal iojAppeal) {
        log.info("Update IoJ Appeal - Transaction Processing - Start");
        var iojAppealDTO = iojAppealMapper.toIOJAppealDTO(iojAppeal);

        log.info("Updating IoJAppeal record");
        var updatedIOJAppealEntity = iojAppealPersistenceService.update(iojAppealDTO);

        log.info("Update IoJ Appeal - Transaction Processing - end");
        return iojAppealMapper.toIOJAppealDTO(updatedIOJAppealEntity);
    }

    @Transactional(readOnly = true)
    public IOJAppealDTO findCurrentPassedAppealByRepId(int repId) {
        IOJAppealEntity iojAppealEntity = iojAppealPersistenceService.findCurrentPassedByRepId(repId);
        if (iojAppealEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No IoJ Appeal found for REP ID: %s", repId));
        }
        return iojAppealMapper.toIOJAppealDTO(iojAppealEntity);
    }
}

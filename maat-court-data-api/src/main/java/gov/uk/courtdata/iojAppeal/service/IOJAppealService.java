package gov.uk.courtdata.iojAppeal.service;

import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.iojAppeal.impl.IOJAppealImpl;
import gov.uk.courtdata.iojAppeal.mapper.IOJAppealMapper;
import gov.uk.courtdata.model.iojAppeal.CreateIOJAppeal;
import gov.uk.courtdata.model.iojAppeal.UpdateIOJAppeal;
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

    @Transactional(rollbackFor = RuntimeException.class)
    public IOJAppealDTO create(CreateIOJAppeal iojAppeal) {
        log.info("Create IOJ Appeal - Transaction Processing - Start");
        var iojAppealDTO =  iojAppealMapper.toIOJAppealDTO(iojAppeal);

        log.info("Creating new IOJAppeal record");
        var iojAppealEntity = iojAppealImpl.create(iojAppealDTO);

        log.info("Update previous IOJ Appeal records and set them to replaced");

        iojAppealImpl.setOldIOJAppealsReplaced(iojAppealEntity.getRepId(), iojAppealEntity.getId());

        log.info("Create IOJ Appeal - Transaction Processing - end");
        return iojAppealMapper.toIOJAppealDTO(iojAppealEntity);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public IOJAppealDTO update(UpdateIOJAppeal iojAppeal) {
        log.info("Update IOJ Appeal - Transaction Processing - Start");
        var iojAppealDTO =  iojAppealMapper.toIOJAppealDTO(iojAppeal);

        log.info("Updating IOJAppeal record");
        var updatedIOJAppealEntity = iojAppealImpl.update(iojAppealDTO);

        log.info("Update IOJ Appeal - Transaction Processing - end");
        return iojAppealMapper.toIOJAppealDTO(updatedIOJAppealEntity);
    }
}

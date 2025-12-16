package gov.uk.courtdata.iojappeal.service;

import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.iojappeal.mapper.IOJAppealMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealResponse;
import uk.gov.justice.laa.crime.common.model.ioj.ApiGetIojAppealResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class IOJAppealV2Service {

    private final IOJAppealPersistenceService iojAppealPersistenceService;
    private final IOJAppealMapper iojAppealMapper;

    @Transactional(rollbackFor = RuntimeException.class)
    public ApiCreateIojAppealResponse create(ApiCreateIojAppealRequest apiCreateIojAppealRequest) {
        log.info("Create IoJ Appeal - Transaction Processing - Start");
        IOJAppealEntity iojAppealEntity = iojAppealMapper.toIojAppealEntity(apiCreateIojAppealRequest);
        iojAppealPersistenceService.create(iojAppealEntity);

        log.info("Update previous IoJ Appeal records and set them to replaced");
        iojAppealPersistenceService.setOldIOJAppealsReplaced(iojAppealEntity.getRepOrder().getId(), iojAppealEntity.getId());

        log.info("Create IoJ Appeal - Transaction Processing - end");
        return iojAppealMapper.toApiCreateIojAppealResponse(iojAppealEntity);
    }

    @Transactional(readOnly = true)
    public ApiGetIojAppealResponse find(int iojAppealId) {
        IOJAppealEntity iojAppealEntity = iojAppealPersistenceService.find(iojAppealId);
        if (iojAppealEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No IoJ Appeal found for ID: %s", iojAppealId));
        }
        return iojAppealMapper.toApiGetIojAppealResponse(iojAppealEntity);
    }
}

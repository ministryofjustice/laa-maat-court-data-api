package gov.uk.courtdata.reporder.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.impl.RepOrderImpl;
import gov.uk.courtdata.reporder.mapper.RepOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@XRayEnabled
@RequiredArgsConstructor
public class RepOrderService {

    private final RepOrderImpl repOrderImpl;
    private final RepOrderMapper repOrderMapper;

    @Transactional(readOnly = true)
    public RepOrderDTO find(Integer repId) {
        RepOrderEntity repOrderEntity = repOrderImpl.find(repId);
        if (repOrderEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No Rep Order found for ID: %s", repId));
        }
        return repOrderMapper.RepOrderEntityToRepOrderDTO(repOrderEntity);
    }

    @Transactional
    public void updateAppDateCompleted(final UpdateAppDateCompleted updateAppDateCompleted) {
        log.info("update app date completed - Transaction Processing - Start");
        repOrderImpl.updateAppDateCompleted(updateAppDateCompleted.getRepId(), updateAppDateCompleted.getAssessmentDateCompleted());
        log.info("update app date completed  - Transaction Processing - End");
    }

    @Transactional
    public void updateRepOrder(final UpdateRepOrder updateRepOrder) {
        log.info("update rep order - Transaction Processing - Start");
        RepOrderEntity repOrderEntity = repOrderImpl.find(updateRepOrder.getRepId());
        repOrderMapper.updateRepOrderToRepOrderEntity(updateRepOrder, repOrderEntity);
        repOrderImpl.updateRepOrder(repOrderEntity);
        log.info("update rep order  - Transaction Processing - End");
    }
}

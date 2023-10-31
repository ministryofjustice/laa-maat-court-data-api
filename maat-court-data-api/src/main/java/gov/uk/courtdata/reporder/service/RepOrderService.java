package gov.uk.courtdata.reporder.service;

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
@RequiredArgsConstructor
public class RepOrderService {

    private final RepOrderImpl repOrderImpl;
    private final RepOrderMapper repOrderMapper;

    public RepOrderEntity findByRepId(Integer repId) {
        RepOrderEntity repOrder;
        repOrder = repOrderImpl.find(repId);
        if (repOrder == null) {
            throw new RequestedObjectNotFoundException(String.format("No Rep Order found for ID: %s", repId));
        }
        return repOrder;
    }

    @Transactional(readOnly = true)
    public RepOrderDTO find(Integer repId, boolean hasSentenceOrderDate) {
        RepOrderEntity repOrder;
        if (hasSentenceOrderDate) {
            repOrder = repOrderImpl.findWithSentenceOrderDate(repId);
        } else {
            repOrder = repOrderImpl.find(repId);
        }

        if (repOrder == null) {
            throw new RequestedObjectNotFoundException(String.format("No Rep Order found for ID: %s", repId));
        }
        return repOrderMapper.repOrderEntityToRepOrderDTO(repOrder);
    }

    @Transactional
    public RepOrderDTO updateDateCompleted(final UpdateAppDateCompleted updateAppDateCompleted) {
        log.info("update app date completed - Transaction Processing - Start");
        return repOrderMapper.repOrderEntityToRepOrderDTO(repOrderImpl
                .updateAppDateCompleted(updateAppDateCompleted.getRepId(), updateAppDateCompleted.getAssessmentDateCompleted()));
    }

    @Transactional
    public RepOrderDTO update(final UpdateRepOrder updateRepOrder) {
        log.info("update rep order - Transaction Processing - Start");
        RepOrderEntity repOrderEntity = repOrderImpl.find(updateRepOrder.getRepId());
        repOrderMapper.updateRepOrderToRepOrderEntity(updateRepOrder, repOrderEntity);
        return repOrderMapper.repOrderEntityToRepOrderDTO(repOrderImpl.updateRepOrder(repOrderEntity));
    }

    @Transactional
    public boolean exists(Integer repId) {
        log.info("Retrieve rep Order Count With Sentence Order Date - Transaction Processing - Start");
        return repOrderImpl.countWithSentenceOrderDate(repId) > 0;
    }
}

package gov.uk.courtdata.assessment.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.assessment.impl.RepOrderImpl;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@XRayEnabled
@RequiredArgsConstructor
public class RepOrdersService {
    private final RepOrderImpl repOrderImpl;

    @Transactional
    public void updateAppDateCompleted(final UpdateAppDateCompleted updateAppDateCompleted) {
        log.info("update app date completed - Transaction Processing - Start");
         RepOrderEntity repOrderEntity = repOrderImpl.findRepOrder(updateAppDateCompleted.getRepId());
        if (null != repOrderEntity) {
            repOrderImpl.updateAppDateCompleted(updateAppDateCompleted.getRepId(), updateAppDateCompleted.getAssessmentDateCompleted());
        } else {
            throw new RequestedObjectNotFoundException(String.format("No Rep order found for ID: %s", updateAppDateCompleted.getRepId()));
        }
        log.info("update app date completed  - Transaction Processing - End");
    }


}

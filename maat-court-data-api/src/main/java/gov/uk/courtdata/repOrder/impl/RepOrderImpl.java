package gov.uk.courtdata.repOrder.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.repository.RepOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@XRayEnabled
@RequiredArgsConstructor
public class RepOrderImpl {

    private final RepOrderRepository repOrderRepository;

    public RepOrderEntity find(Integer repId) {
        return repOrderRepository.findById(repId).orElse(null);
    }

    public void updateAppDateCompleted(final Integer repId, final LocalDateTime assessmentDateCompleted) {
        RepOrderEntity repOrderEntity = repOrderRepository.getById(repId);
        repOrderEntity.setAssessmentDateCompleted(assessmentDateCompleted);
        repOrderRepository.saveAndFlush(repOrderEntity);
    }

    public void updateRepOrder(UpdateRepOrder updateRepOrder) {
        RepOrderEntity repOrderEntity = repOrderRepository.getById(updateRepOrder.getRepId());
        repOrderEntity.setSentenceOrderDate(updateRepOrder.getSentenceOrderDate());
        repOrderEntity.setUserModified(updateRepOrder.getUserModified());
        repOrderEntity.setDateModified(LocalDateTime.now());
        repOrderRepository.saveAndFlush(repOrderEntity);
    }
}

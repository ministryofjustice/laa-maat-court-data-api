package gov.uk.courtdata.reporder.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.repository.RepOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static gov.uk.courtdata.reporder.specification.RepOrderSpecification.hasId;
import static gov.uk.courtdata.reporder.specification.RepOrderSpecification.hasSentenceOrderDate;

@Slf4j
@Component
@XRayEnabled
@RequiredArgsConstructor
public class RepOrderImpl {

    private final RepOrderRepository repOrderRepository;

    public RepOrderEntity find(Integer repId) {
        return repOrderRepository.findById(repId).orElse(null);
    }

    public RepOrderEntity findWithSentenceOrderDate(Integer repId) {
        return repOrderRepository.findOne(hasId(repId).and(hasSentenceOrderDate())).orElse(null);
    }

    public void updateAppDateCompleted(final Integer repId, final LocalDateTime assessmentDateCompleted) {
        RepOrderEntity repOrderEntity = repOrderRepository.getReferenceById(repId);
        repOrderEntity.setAssessmentDateCompleted(assessmentDateCompleted);
        repOrderRepository.saveAndFlush(repOrderEntity);
    }

    public void updateRepOrder(RepOrderEntity repOrderEntity) {
        repOrderRepository.saveAndFlush(repOrderEntity);
    }

    public long countWithSentenceOrderDate(Integer repId) {
        return repOrderRepository.count(hasId(repId).and(hasSentenceOrderDate()));
    }
}

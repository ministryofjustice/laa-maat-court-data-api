package gov.uk.courtdata.reporder.impl;

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
@RequiredArgsConstructor
public class RepOrderImpl {

    private final RepOrderRepository repOrderRepository;

    public RepOrderEntity find(Integer repId) {
        return repOrderRepository.findById(repId).orElse(null);
    }

    public RepOrderEntity findWithSentenceOrderDate(Integer repId) {
        return repOrderRepository.findOne(hasId(repId).and(hasSentenceOrderDate())).orElse(null);
    }

    public RepOrderEntity updateAppDateCompleted(final Integer repId, final LocalDateTime assessmentDateCompleted) {
        RepOrderEntity repOrderEntity = repOrderRepository.getReferenceById(repId);
        repOrderEntity.setAssessmentDateCompleted(assessmentDateCompleted.toLocalDate());
        return repOrderRepository.saveAndFlush(repOrderEntity);
    }

    public RepOrderEntity createRepOrder(RepOrderEntity repOrderEntity) {
        return repOrderRepository.saveAndFlush(repOrderEntity);
    }

    public RepOrderEntity updateRepOrder(RepOrderEntity repOrderEntity) {
        return repOrderRepository.saveAndFlush(repOrderEntity);
    }

    public long countWithSentenceOrderDate(Integer repId) {
        return repOrderRepository.count(hasId(repId).and(hasSentenceOrderDate()));
    }

    public void delete(Integer repId) {
        repOrderRepository.deleteById(repId);
    }
}

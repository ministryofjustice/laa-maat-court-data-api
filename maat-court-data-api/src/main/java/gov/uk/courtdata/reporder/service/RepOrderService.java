package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.model.CreateRepOrder;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.impl.RepOrderImpl;
import gov.uk.courtdata.reporder.mapper.RepOrderMapper;
import gov.uk.courtdata.repository.RepOrderRepository;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepOrderService {

    private final RepOrderImpl repOrderImpl;
    private final RepOrderMapper repOrderMapper;
    private final RepOrderRepository repOrderRepository;

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
    public RepOrderDTO create(final CreateRepOrder createRepOrder) {
        log.info("create rep order - Transaction Processing - Start");
        RepOrderEntity repOrderEntity = new RepOrderEntity();
        repOrderEntity.setDateCreated(LocalDate.now());
        repOrderMapper.createRepOrderToRepOrderEntity(createRepOrder, repOrderEntity);
        RepOrderEntity createdRepOrderEntity = repOrderImpl.createRepOrder(repOrderEntity);
        return repOrderMapper.repOrderEntityToRepOrderDTO(createdRepOrderEntity);
    }

    @Transactional
    public RepOrderDTO update(final UpdateRepOrder updateRepOrder) {
        log.info("update rep order - Transaction Processing - Start");
        RepOrderEntity repOrderEntity = repOrderImpl.find(updateRepOrder.getRepId());
        repOrderMapper.updateRepOrderToRepOrderEntity(updateRepOrder, repOrderEntity);
        return repOrderMapper.repOrderEntityToRepOrderDTO(repOrderImpl.updateRepOrder(repOrderEntity));
    }

    @Transactional
    public void update(Integer repId, Map<String, Object> repOrder) {
        log.info("RepOrderService::update - Start");
      RepOrderEntity currentRepOrder = repOrderRepository.findById(repId)
          .orElseThrow(() -> new RequestedObjectNotFoundException(
              String.format("Rep Order not found for id %d", repId)));

        if (currentRepOrder != null) {
            repOrder.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(RepOrderEntity.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, currentRepOrder, value);
            });
            repOrderRepository.save(currentRepOrder);
        } else {
          throw new RequestedObjectNotFoundException("Rep Order not found for id " + repId);
        }
    }

    @Transactional
    public boolean exists(Integer repId) {
        log.info("Retrieve rep Order Count With Sentence Order Date - Transaction Processing - Start");
        return repOrderImpl.countWithSentenceOrderDate(repId) > 0;
    }

    @Transactional
    public void delete(Integer repId) {
        repOrderImpl.delete(repId);
    }

    @Transactional(readOnly = true)
    public AssessorDetails findIOJAssessorDetails(int repId) {
        Optional<RepOrderEntity> repOrderOptional = repOrderRepository.findById(repId);

        if (repOrderOptional.isEmpty()) {
            String message = "Unable to find AssessorDetails for repId: [%d]".formatted(repId);
            throw new RequestedObjectNotFoundException(message);
        }

        return repOrderMapper.createIOJAssessorDetails(repOrderOptional.get());
    }

    public List<Integer> findEligibleForFdcDelayedPickup(int delayPeriod, LocalDate dateReceived, int numRecords){
        return repOrderImpl.findEligibleForFdcDelayedPickup(delayPeriod, dateReceived, numRecords);
    }

    public List<Integer> findEligibleForFdcFastTracking(int delayPeriod, LocalDate dateReceived, int numRecords){
        return repOrderImpl.findEligibleForFdcFastTracking(delayPeriod, dateReceived, numRecords);
    }
}

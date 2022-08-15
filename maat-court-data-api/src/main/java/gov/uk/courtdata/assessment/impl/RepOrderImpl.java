package gov.uk.courtdata.assessment.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.repository.RepOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class RepOrderImpl {

    private final RepOrderRepository repOrderRepository;

    public RepOrderEntity findRepOrder(Integer repId) {
        return repOrderRepository.getById(repId);
    }

    public void updateAppDateCompleted(final Integer repId, final LocalDateTime assessmentDateCompleted) {
        repOrderRepository.updateAppDateCompleted(repId, assessmentDateCompleted);
    }
}

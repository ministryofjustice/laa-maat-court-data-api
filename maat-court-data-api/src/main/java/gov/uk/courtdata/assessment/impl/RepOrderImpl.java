package gov.uk.courtdata.assessment.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.repository.RepOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class RepOrderImpl {

    private final RepOrderRepository repOrderRepository;

    public RepOrderEntity findRepOrder(Integer repId) {
        return repOrderRepository.getById(repId);
    }
}

package gov.uk.courtdata.repOrder.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.RepOrderMvoRegEntity;
import gov.uk.courtdata.repository.RepOrderMvoRegRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@XRayEnabled
@RequiredArgsConstructor
public class RepOrderMvoRegImpl {

    private final RepOrderMvoRegRepository repOrderMvoRegRepository;

    public RepOrderMvoRegEntity findRepOrderMvoRegByRepId(Integer repId) {
        return repOrderMvoRegRepository.findRepOrderMvoRegByRepId(repId).orElse(null);
    }
}

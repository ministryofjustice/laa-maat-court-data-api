package gov.uk.courtdata.wqoffence.impl;

import gov.uk.courtdata.repository.WQOffenceRepository;
import com.amazonaws.xray.spring.aop.XRayEnabled;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@XRayEnabled
@RequiredArgsConstructor
public class WQOffenceImpl {

    private final WQOffenceRepository wqOffenceRepository;

    public Integer getNewOffenceCount(Integer caseId, String offenceId) {
        return wqOffenceRepository.getNewOffenceCount(caseId, offenceId);
    }
}

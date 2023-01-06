package gov.uk.courtdata.offence.impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.repository.OffenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@XRayEnabled
@RequiredArgsConstructor
public class OffenceImpl {

    private final OffenceRepository offenceRepository;

    public List<OffenceEntity> findByCaseId(Integer caseId) {
        return offenceRepository.findByCaseId(caseId);
    }

    public Integer getNewOffenceCount(Integer caseId, String offenceId) {
        return offenceRepository.getNewOffenceCount(caseId, offenceId);
    }

}

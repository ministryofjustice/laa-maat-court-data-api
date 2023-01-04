package gov.uk.courtdata.offence.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.dto.OffenceDTO;
import gov.uk.courtdata.offence.impl.OffenceImpl;
import gov.uk.courtdata.offence.mapper.OffenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class OffenceService {

    private final OffenceImpl offenceImpl;
    private final OffenceMapper offenceMapper;

    @Transactional(readOnly = true)
    public List<OffenceDTO> findByCaseId(Integer caseId) {
        log.info("OffenceService - findByCaseId - Start");
        return offenceMapper.offenceEntityToOffenceDTO(offenceImpl.findByCaseId(caseId));
    }

    @Transactional(readOnly = true)
    public Integer getNewOffenceCount(Integer caseId, String offenceId) {
        log.info("OffenceService - getNewOffenceCount - Start");
        return offenceImpl.getNewOffenceCount(caseId, offenceId);
    }

}

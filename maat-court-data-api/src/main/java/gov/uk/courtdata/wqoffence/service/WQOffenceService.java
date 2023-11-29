package gov.uk.courtdata.wqoffence.service;

import gov.uk.courtdata.wqoffence.impl.WQOffenceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WQOffenceService {

    private final WQOffenceImpl wqOffenceImpl;

    @Transactional(readOnly = true)
    public Integer getNewOffenceCount(Integer caseId, String offenceId) {
        log.info("WQOffenceService - getNewOffenceCount - Start");
        return wqOffenceImpl.getNewOffenceCount(caseId, offenceId);
    }
}

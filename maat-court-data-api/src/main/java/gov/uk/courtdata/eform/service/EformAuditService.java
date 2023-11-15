package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.exception.USNExceptionUtil;
import gov.uk.courtdata.eform.repository.EformAuditRepository;
import gov.uk.courtdata.eform.repository.entity.EformsAudit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EformAuditService {

    private final EformAuditRepository eformAuditRepository;

    @Transactional(readOnly = true)
    public EformsAudit retrieve(int usn) {
        return eformAuditRepository.findByUsn(usn)
                .orElseThrow(() -> USNExceptionUtil.nonexistent(usn));
    }
}

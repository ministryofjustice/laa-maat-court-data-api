package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.repository.EformAuditRepository;
import gov.uk.courtdata.eform.repository.entity.EformsAudit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EformAuditService {

    private final EformAuditRepository eformAuditRepository;

    @Transactional(readOnly = true)
    public EformsAudit retrieve(Integer usn) {
        return eformAuditRepository.findByUsn(usn).orElse(EformsAudit.builder().build());
    }

    @Transactional()
    public void create(EformsAudit eformsAudit) {
        eformAuditRepository.save(eformsAudit);
    }

    @Transactional()
    public void delete(Integer usn) {
        eformAuditRepository.deleteAllByUsn(usn);
    }
}

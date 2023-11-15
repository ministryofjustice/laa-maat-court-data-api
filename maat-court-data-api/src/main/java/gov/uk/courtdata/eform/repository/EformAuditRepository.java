package gov.uk.courtdata.eform.repository;

import gov.uk.courtdata.eform.repository.entity.EformsAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EformAuditRepository extends JpaRepository<EformsAudit,Integer> {

    public Optional<EformsAudit> findByUsn(int usn);
}

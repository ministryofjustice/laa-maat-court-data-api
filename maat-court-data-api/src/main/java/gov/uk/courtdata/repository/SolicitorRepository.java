package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.SolicitorEntity;
import gov.uk.courtdata.model.id.CaseTxnId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitorRepository extends JpaRepository<SolicitorEntity, CaseTxnId> {
}
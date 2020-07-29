package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.SessionEntity;
import gov.uk.courtdata.model.id.CaseTxnId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, CaseTxnId> {
}

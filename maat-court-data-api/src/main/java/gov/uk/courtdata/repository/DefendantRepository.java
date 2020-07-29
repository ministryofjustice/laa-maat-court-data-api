package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.DefendantEntity;
import gov.uk.courtdata.model.id.CaseTxnId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefendantRepository extends JpaRepository<DefendantEntity, CaseTxnId> {

}

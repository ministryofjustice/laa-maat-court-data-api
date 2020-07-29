package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ProceedingEntity;
import gov.uk.courtdata.model.id.ProceedingMaatId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProceedingRepository extends JpaRepository<ProceedingEntity, ProceedingMaatId> {
}

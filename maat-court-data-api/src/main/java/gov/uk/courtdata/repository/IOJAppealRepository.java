package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.IOJAppealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOJAppealRepository extends JpaRepository<IOJAppealEntity, Integer> {
}

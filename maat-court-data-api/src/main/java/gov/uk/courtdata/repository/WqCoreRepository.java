package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.WqCoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WqCoreRepository extends JpaRepository<WqCoreEntity,Integer> {

}
